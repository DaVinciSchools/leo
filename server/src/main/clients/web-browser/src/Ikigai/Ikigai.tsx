import './Ikigai.scss';
import {
  CSSProperties,
  PropsWithChildren,
  useEffect,
  useRef,
  useState,
} from 'react';
import {IkigaiCategory} from '../IkigaiCategory/IkigaiCategory';
import {doTransition, overshootTransition} from '../libs/transitions';
import {SpinButton} from './SpinButton/SpinButton';
import PromiseQueue from '../libs/PromiseQueue';

enum VisibleState {
  WAITING_FOR_RENDER,
  RENDERED,
  VISIBLE,
}

export const VISIBLE_ALPHA = 0.2;

function getSize(element: HTMLDivElement | null) {
  const width =
    element?.getBoundingClientRect()?.width ?? element?.clientWidth ?? 0;
  const height =
    element?.getBoundingClientRect()?.height ?? element?.clientHeight ?? 0;
  return {
    width: width,
    height: height,
  };
}

export function Ikigai(
  props: PropsWithChildren<{
    id: string;
    categoryDiameter: (width: number, height: number) => number;
    distanceToCategoryCenter: (width: number, height: number) => number;
    radians: number;
    radiansOffset?: number;
    enabled: boolean;
    processing?: boolean;
    showSpinButton: boolean;
    onSpinClick: () => void;
    categoryElementIds: string[];
    style?: Partial<CSSProperties>;
  }>
) {
  const [promises] = useState(new PromiseQueue());

  const showLongDurationMs = 750;
  const showShortDurationMs = 325;
  const processingStepDurationMs = 750;
  const processingStepDelayMs = 250;
  const processingStepIncrement = Math.PI / 4;
  const radiansOffset =
    props.radiansOffset != null
      ? props.radiansOffset
      : (2 * Math.PI) / props.categoryElementIds.length / 2;

  const [categoryDiameter, setCategoryDiameter] = useState(0);
  const [distanceToCategoryCenter, setDistanceToCategoryCenter] = useState(0);
  const [radians, setRadians] = useState(props.radians);
  const [backgroundAlpha, setBackgroundAlpha] = useState(0);
  const [contentAlpha, setContentAlpha] = useState(0);

  // Set the size and visibility based on the DIV container's dimensions.
  const containerRef = useRef<HTMLDivElement>(null);

  // Show the initial rendering after a short delay. The delay is to wait
  // for rendering and re-rendering to complete.
  const visibleState = useRef(VisibleState.WAITING_FOR_RENDER);
  const waitForFirstRenderingTimerId = useRef<NodeJS.Timeout>();

  function needsSpinUpdate() {
    // Cancel any existing timer.
    if (waitForFirstRenderingTimerId.current != null) {
      clearTimeout(waitForFirstRenderingTimerId.current);
      waitForFirstRenderingTimerId.current = undefined;
    }

    if (visibleState.current === VisibleState.WAITING_FOR_RENDER) {
      // Delay a little to let the initial rendering finish.
      waitForFirstRenderingTimerId.current = setTimeout(() => {
        if (visibleState.current === VisibleState.WAITING_FOR_RENDER) {
          visibleState.current = VisibleState.RENDERED;
          const size = getSize(containerRef.current);
          showDiagram(size.width, size.height);
        }
      }, 500);
    } else {
      const size = getSize(containerRef.current);
      showDiagram(size.width, size.height);
    }
  }

  // Rerender on container change or resize.
  const [resizeObserver] = useState(
    new ResizeObserver(() => {
      const size = getSize(containerRef.current);
      setDistanceToCategoryCenter(
        props.distanceToCategoryCenter(size.width, size.height)
      );
      setCategoryDiameter(props.categoryDiameter(size.width, size.height));
    })
  );
  const [resizeObserving, setResizeObserving] = useState<
    HTMLElement | undefined
  >();
  useEffect(() => {
    /* eslint-disable-next-line eqeqeq */
    if (containerRef.current != resizeObserving) {
      if (resizeObserving != null) {
        resizeObserver.unobserve(resizeObserving);
        setResizeObserving(undefined);
      }
      if (containerRef.current != null) {
        setResizeObserving(containerRef.current);
        resizeObserver.observe(containerRef.current);
      }
      needsSpinUpdate();
    }
  }, [containerRef.current]);

  // Determine the diagram dimensions.
  const size = getSize(containerRef.current);

  function showDiagram(width: number, height: number) {
    switch (visibleState.current) {
      case VisibleState.WAITING_FOR_RENDER:
        return;
      case VisibleState.RENDERED: {
        if (props.categoryElementIds.length === 0) {
          return;
        }

        // Use the long animation.
        promises.enqueue(
          () =>
            doTransition(
              showLongDurationMs,
              {
                setFn: setRadians,
                begin: (props.radiansOffset ?? props.radians) - 4 * Math.PI,
                end: props.radiansOffset ?? props.radians,
              },
              {
                setFn: setDistanceToCategoryCenter,
                begin: 0,
                end: props.distanceToCategoryCenter(width, height),
              },
              {
                setFn: setBackgroundAlpha,
                begin: 0,
                end: VISIBLE_ALPHA,
              },
              {
                setFn: setContentAlpha,
                begin: 0,
                end: 1,
              },
              {
                setFn: setCategoryDiameter,
                begin: 0,
                end: props.categoryDiameter(width, height),
              }
            ).finally(() => {
              visibleState.current = VisibleState.VISIBLE;
            }),
          props.categoryElementIds.toString()
        );
        return;
      }
      case VisibleState.VISIBLE: {
        // Use the quick animation.
        setDistanceToCategoryCenter(
          props.distanceToCategoryCenter(width, height)
        );
        setCategoryDiameter(props.categoryDiameter(width, height));
        promises.enqueue(
          () =>
            doTransition(showShortDurationMs, {
              setFn: setRadians,
              begin: (props.radiansOffset ?? props.radians) - 2 * Math.PI,
              end: props.radiansOffset ?? props.radians,
            }),
          props.categoryElementIds.toString()
        );
        return;
      }
    }
  }

  function doProcessingStep(startRadians: number) {
    if (props.processing !== true) {
      return;
    }

    promises.enqueue(() =>
      doTransition(processingStepDurationMs, {
        setFn: setRadians,
        begin: startRadians - processingStepIncrement,
        end: startRadians,
        fractionFn: overshootTransition(-0.15, 0.7),
      }).finally(() => {
        setTimeout(() => {
          doProcessingStep(startRadians + processingStepIncrement);
        }, processingStepDelayMs);
      })
    );
  }

  useEffect(() => {
    needsSpinUpdate();
  }, [props.categoryElementIds]);

  useEffect(() => {
    if (props.processing === true) {
      doProcessingStep(Math.PI / 4 - 0.00001);
    }
  }, [props.processing]);

  // Default to using the animation values.
  let useCategoryDiameter: number = categoryDiameter;
  let useDistanceToCategoryCenter: number = distanceToCategoryCenter;

  // But, use the current values when not animating.
  if (
    visibleState.current === VisibleState.VISIBLE &&
    props.processing !== true
  ) {
    useCategoryDiameter = props.categoryDiameter(size.width, size.height);
    useDistanceToCategoryCenter = props.distanceToCategoryCenter(
      size.width,
      size.height
    );
  }

  return (
    <div
      ref={containerRef}
      style={Object.assign(
        {},
        {
          left: 0,
          top: 0,
          width: '100%',
          height: '100%',
          position: 'relative',
          overflow: 'visible',
        },
        props.style ?? {}
      )}
    >
      {props.categoryElementIds.map((categoryElementId, index) => (
        <IkigaiCategory
          id={props.id + '.' + categoryElementId}
          key={categoryElementId}
          diameter={useCategoryDiameter}
          maxDiameter={props.categoryDiameter(size.width, size.height)}
          hue={index * (360 / props.categoryElementIds.length)}
          backgroundAlpha={backgroundAlpha}
          contentAlpha={contentAlpha}
          radians={
            radians +
            ((2 * Math.PI) / props.categoryElementIds.length) * index +
            radiansOffset
          }
          distance={useDistanceToCategoryCenter}
          categoryElementId={categoryElementId}
        />
      ))}
      <span
        style={{
          display: contentAlpha > 0 ? undefined : 'none',
        }}
      >
        {props.children}
      </span>
      <SpinButton
        id={props.id + '.spinButton'}
        diameter={useCategoryDiameter / 3}
        enabled={props.showSpinButton}
        onClick={props.onSpinClick}
      />
    </div>
  );
}
