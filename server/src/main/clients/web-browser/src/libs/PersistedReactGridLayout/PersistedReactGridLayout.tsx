import './PersistedReactGridLayout.scss';

import {
  CSSProperties,
  PropsWithChildren,
  ReactNode,
  useEffect,
  useRef,
  useState,
} from 'react';
import ReactGridLayout, {ItemCallback} from 'react-grid-layout';

export const DRAGGABLE_CLASS_NAME = 'persisted-react-grid-layout-draggable';
export const NOT_DRAGGABLE_CLASS_NAME =
  'persisted-react-grid-layout-not-draggable';

export function PersistedReactGridLayout(
  props: PropsWithChildren<{
    id: string;
    panels: {
      id: string;
      panel: ReactNode;
      layout: {x: number; y: number; w: number; h: number};
      static?: boolean;
    }[];
    cols: number;
    rows: number;
    padding?: {x: number; y: number};
    gap?: {x: number; y: number};
    style?: Partial<CSSProperties>;
    onDragStart?: ItemCallback;
    onDrag?: ItemCallback;
    onDragStop?: ItemCallback;
  }>
) {
  // User the div container to resize the react-grid-layout (RGL).
  const divContainerRef = useRef<HTMLDivElement>(null);
  // '| null' is necessary to make lastDivContainer mutable.
  const lastDivContainer = useRef<HTMLDivElement | null>(null);

  // These are the calculated values as determined by the div container.
  const [rowHeight, setRowHeight] = useState(1);
  const [rglWidth, setRglWidth] = useState(1);

  // This is the layout extracted from the props.panels. It will be persisted across instances of this page.
  const [layout] = useState(
    props.panels.map(p =>
      Object.assign({i: p.id, static: p.static === true}, p.layout)
    )
  );

  // This takes the dimensions of the div container and adjusts those of the RGL so that the diagram will remain consistent.
  function setRglDimensions(divElement: HTMLDivElement) {
    const targetElement = divElement.children[0];

    setRglWidth(targetElement.clientWidth);

    setRowHeight(
      (targetElement.clientHeight -
        (props.rows - 1) * (props?.gap?.y ?? 0) -
        2 * (props?.padding?.y ?? 0)) /
        props.rows
    );
  }

  // Detects changes in size to the div container and triggers an update.
  const resizeObserver = new ResizeObserver(() => {
    if (divContainerRef.current != null) {
      setRglDimensions(divContainerRef.current);
    }
  });

  // Detects the creation of the div container and triggers an update.
  useEffect(() => {
    if (lastDivContainer.current != null) {
      resizeObserver.unobserve(lastDivContainer.current);
      lastDivContainer.current = null;
    }
    if (divContainerRef.current != null) {
      lastDivContainer.current = divContainerRef.current;
      resizeObserver.observe(divContainerRef.current);
      setRglDimensions(divContainerRef.current);
    }
  }, [divContainerRef.current]);

  return (
    <>
      <div
        className="persisted-react-grid-layout-container"
        style={props.style}
        ref={divContainerRef}
      >
        <ReactGridLayout
          layout={layout}
          cols={props.cols}
          rowHeight={rowHeight}
          width={rglWidth}
          style={{
            // These have to be expressed as style properties.
            width: '100%',
            height: '100%',
          }}
          className="persisted-react-grid-layout"
          margin={[props?.gap?.x ?? 0, props?.gap?.y ?? 0]}
          containerPadding={[props?.padding?.x ?? 0, props?.padding?.y ?? 0]}
          draggableHandle={'.' + DRAGGABLE_CLASS_NAME}
          draggableCancel={'.' + NOT_DRAGGABLE_CLASS_NAME}
          onDragStart={props.onDragStart}
          onDrag={props.onDrag}
          onDragStop={props.onDragStop}
        >
          {props.panels.map(p => (
            <div key={p.id}>{p.panel}</div>
          ))}
        </ReactGridLayout>
      </div>
    </>
  );
}
