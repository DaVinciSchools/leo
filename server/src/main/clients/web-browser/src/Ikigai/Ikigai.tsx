import './Ikigai.scss';
import {createRef, PropsWithChildren, useEffect, useRef, useState} from 'react';
import {IkigaiCategory} from '../IkigaiCategory/IkigaiCategory';
import {doTransition, overshootTransition} from '../utils/transitions';
import {SpinButton, SpinButtonFunctions} from './SpinButton/SpinButton';

enum VisibleState {
  WAITING_FOR_RENDER,
  RENDERED,
  ANIMATING,
  VISIBLE,
}

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
    categoryElementIds: (string | undefined)[];
  }>
) {
  const visibleAlpha = 0.2;
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

  const spinButton = createRef<SpinButtonFunctions>();
  const [spinButtonEnabled, setSpinButtonEnabled] = useState(false);

  // Set the size and visibility based on the DIV container's dimensions.
  const containerRef = useRef<HTMLDivElement>(null);

  // Force a rerender... of the diagram dimensions when the container
  // dimensions have changed.
  const forceRerenderCounter = useRef(1);
  const setForceRerenderState = useState(forceRerenderCounter.current)[1];

  // Show the initial rendering after a short delay. The delay is to wait
  // for rendering and re-rendering to complete.
  const visibleState = useRef(VisibleState.WAITING_FOR_RENDER);
  const waitForFirstRenderingTimerId = useRef<NodeJS.Timeout>();

  function rerenderNeeded() {
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
        }
        setForceRerenderState(forceRerenderCounter.current++);
      }, 500);
    } else {
      setForceRerenderState(forceRerenderCounter.current++);
    }
  }

  // Rerender on container change or resize.
  const [resizeObserver] = useState(new ResizeObserver(rerenderNeeded));
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
      rerenderNeeded();
    }
  }, [containerRef.current]);

  // Determine the diagram dimensions.
  const size = getSize(containerRef.current);
  const centerX = size.width / 2;
  const centerY = size.height / 2;

  function showDiagram(width: number, height: number): Promise<void> {
    switch (visibleState.current) {
      case VisibleState.WAITING_FOR_RENDER:
      case VisibleState.ANIMATING:
        return Promise.resolve();
      case VisibleState.RENDERED: {
        // Use the long animation.
        visibleState.current = VisibleState.ANIMATING;
        return (
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
              end: visibleAlpha,
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
          ) as Promise<void>
        ).finally(() => {
          visibleState.current = VisibleState.VISIBLE;
        });
      }
      case VisibleState.VISIBLE: {
        // Use the quick animation.
        visibleState.current = VisibleState.ANIMATING;
        setDistanceToCategoryCenter(
          props.distanceToCategoryCenter(width, height)
        );
        setCategoryDiameter(props.categoryDiameter(width, height));
        return (
          doTransition(
            showShortDurationMs,
            {
              setFn: setRadians,
              begin: (props.radiansOffset ?? props.radians) - 2 * Math.PI,
              end: props.radiansOffset ?? props.radians,
            },
            {
              setFn: setBackgroundAlpha,
              begin: 0,
              end: visibleAlpha,
            },
            {
              setFn: setContentAlpha,
              begin: 0,
              end: 1,
            }
          ) as Promise<void>
        ).finally(() => {
          visibleState.current = VisibleState.VISIBLE;
        });
      }
    }
  }

  useEffect(() => {
    if (props.showSpinButton) {
      spinButton.current
        ?.show(showLongDurationMs)
        .finally(() => setSpinButtonEnabled(true));
    } else {
      spinButton.current
        ?.hide(showLongDurationMs)
        .finally(() => setSpinButtonEnabled(false));
    }
  }, [props.showSpinButton]);

  function doProcessingStep(startRadians: number) {
    if (props.processing !== true) {
      return;
    }

    doTransition(processingStepDurationMs, {
      setFn: setRadians,
      begin: startRadians - processingStepIncrement,
      end: startRadians,
      fractionFn: overshootTransition(-0.15, 0.7),
    }).finally(() => {
      setTimeout(() => {
        doProcessingStep(startRadians + processingStepIncrement);
      }, processingStepDelayMs);
    });
  }

  useEffect(() => {
    if (props.categoryElementIds.length > 0) {
      const size = getSize(containerRef.current);
      showDiagram(size.width, size.height).finally(() => {
        visibleState.current = VisibleState.VISIBLE;
        rerenderNeeded();
      });
    }
  }, [props.categoryElementIds]);

  useEffect(() => {
    if (props.processing !== false) {
      doProcessingStep(Math.PI / 4 - 0.00001);
    }
  }, [props.processing]);

  // Default to using the animation values.
  let useCategoryDiameter: number = categoryDiameter;
  let useDistanceToCategoryCenter: number = distanceToCategoryCenter;

  // But, use the current values when not animating.
  if (visibleState.current === VisibleState.VISIBLE && !props.processing) {
    useCategoryDiameter = props.categoryDiameter(size.width, size.height);
    useDistanceToCategoryCenter = props.distanceToCategoryCenter(
      size.width,
      size.height
    );
  }

  return (
    <div
      ref={containerRef}
      style={{
        left: 0,
        top: 0,
        width: '100%',
        height: '100%',
        position: 'relative',
      }}
    >
      {props.categoryElementIds.map((categoryElementId, index) => (
        <IkigaiCategory
          id={props.id + '.' + categoryElementId}
          key={categoryElementId}
          center={{x: centerX, y: centerY}}
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
          display: useCategoryDiameter > 0 ? undefined : 'none',
        }}
      >
        {props.children}
      </span>
      <SpinButton
        id={props.id + '.spinButton'}
        origin={{x: centerX, y: centerY}}
        diameter={useCategoryDiameter / 3}
        enabled={spinButtonEnabled && props.enabled}
        onClick={props.onSpinClick}
        ref={spinButton}
      />
    </div>
  );
}
