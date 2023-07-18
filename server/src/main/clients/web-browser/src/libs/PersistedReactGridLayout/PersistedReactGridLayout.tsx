import './PersistedReactGridLayout.scss';

import {PropsWithChildren, ReactNode, useEffect, useRef, useState} from 'react';
import ReactGridLayout from 'react-grid-layout';

export const persistedGridDraggable = 'persisted-grid-draggable';
export const persistedGridNotDraggable = 'persisted-grid-not-draggable';

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
  }>
) {
  // User the div container to resize the react-grid-layout (RGL).
  const divContainerRef = useRef<HTMLDivElement>(null);
  const [lastDivContainer, setLastDivContainer] =
    useState<HTMLDivElement | null>();

  // These are the calculated values as determined by the div container.
  const [rowHeight, setRowHeight] = useState(1);
  const [rglWidth, setRglWidth] = useState(1);
  const [rglHeight, setRglHeight] = useState(1);

  // This is the layout extracted from the props.panels. It will be persisted across instances of this page.
  const [layout] = useState(
    props.panels.map(p =>
      Object.assign({i: p.id, static: p.static === true}, p.layout)
    )
  );

  // This takes the dimensions of the div container and adjusts those of the RGL so that the diagram will remain consistent.
  function setRglDimensions(divElement: HTMLDivElement) {
    setRglWidth(divElement.clientWidth);
    const rowHeight =
      (divElement.clientHeight -
        (props.rows - 1) * (props?.gap?.y ?? 0) -
        2 * (props?.padding?.y ?? 0)) /
      props.rows;
    setRowHeight(rowHeight);
    setRglHeight(
      Math.max.apply(
        null,
        props.panels.map(panel => panel.layout.y + panel.layout.h)
      ) * rowHeight
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
    if (divContainerRef.current != null) {
      resizeObserver.observe(divContainerRef.current);
      setRglDimensions(divContainerRef.current);
    } else if (lastDivContainer != null) {
      resizeObserver.unobserve(lastDivContainer);
    }
    setLastDivContainer(divContainerRef.current);
  }, [divContainerRef.current]);

  return (
    <>
      <div
        style={{
          width: '100%',
          height: '100%',
        }}
        ref={divContainerRef}
      >
        <ReactGridLayout
          className="layout"
          layout={layout}
          cols={props.cols}
          rowHeight={rowHeight - 1}
          width={rglWidth - 2}
          style={{width: rglWidth, height: rglHeight}}
          margin={[props?.gap?.x ?? 0, props?.gap?.y ?? 0]}
          containerPadding={[props?.padding?.x ?? 0, props?.padding?.y ?? 0]}
          draggableHandle={'.' + persistedGridDraggable}
          draggableCancel={'.' + persistedGridNotDraggable}
        >
          {props.panels.map(p => (
            <div key={p.id}>{p.panel}</div>
          ))}
        </ReactGridLayout>
      </div>
    </>
  );
}
