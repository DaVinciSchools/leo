import '../global.scss';

import {CSSProperties} from 'react';
import {Layout} from 'react-grid-layout';
import {DeepReadOnly} from '../misc';
import {getCategoryId, ProjectInput} from '../ProjectBuilder/ProjectBuilder';
import {PersistedReactGridLayout} from '../PersistedReactGridLayout/PersistedReactGridLayout';
import {TitledPaper} from '../TitledPaper/TitledPaper';
import Markdown from 'react-markdown';
import {VISIBLE_ALPHA} from '../../Ikigai/Ikigai';

export function IkigaiProjectConfigurer(props: {
  inputs: ProjectInput[];
  setInputs: (inputs: ProjectInput[]) => void;
  style?: Partial<CSSProperties>;
}) {
  function handleDrag(layout: DeepReadOnly<Layout[]>) {
    let needsUpdate = false;
    const sortedLayout = layout.slice().sort((a, b) => a.y - b.y);
    sortedLayout.forEach((l, index) => {
      needsUpdate =
        needsUpdate ||
        getCategoryId(props.inputs[index]?.input?.category) !== parseInt(l.i);
    });
    if (needsUpdate) {
      const inputsById = new Map(
        props.inputs.map(i => [getCategoryId(i.input?.category), i])
      );
      props.setInputs(sortedLayout.map(l => inputsById.get(parseInt(l.i))!));
    }
  }

  return (
    <PersistedReactGridLayout
      id="category-grid"
      cols={1}
      rows={3}
      padding={{x: 5, y: 0}}
      gap={{x: 0, y: 16}}
      panels={props.inputs.map((i, index) => ({
        id: getCategoryId(i.input?.category).toString(),
        panel: (
          <TitledPaper
            title={
              <Markdown className="global-markdown">
                {i.input?.category?.name ?? 'undefined'}
              </Markdown>
            }
            headerColor={
              i.selected
                ? `hsla(${i.highlightHue}, 100%, 75%, ${VISIBLE_ALPHA}`
                : undefined
            }
            highlightColor={
              i.selected ? `hsla(${i.highlightHue}, 100%, 75%, 100%)` : 'grey'
            }
            draggableCursorType="ns-resize"
            bodyStyle={{
              display: 'flex',
              flexFlow: 'column nowrap',
              justifyContent: 'space-between',
            }}
            icon={
              <>
                <span
                  style={{
                    whiteSpace: 'nowrap',
                    color: 'black',
                    fontWeight: 'normal',
                  }}
                >
                  Include:&nbsp;
                  <input
                    type="checkbox"
                    checked={i.selected}
                    onChange={() => {
                      // TODO: Fix this hack.
                      props.inputs.forEach(i => {
                        if (
                          getCategoryId(i.input?.category) ===
                          getCategoryId(props.inputs[index].input?.category)
                        ) {
                          i.selected = !i.selected;
                        }
                      });
                      props.setInputs(props.inputs);
                    }}
                  />
                </span>
              </>
            }
          >
            <Markdown className="global-markdown">
              {i.input?.category?.shortDescr ?? 'undefined'}
            </Markdown>
          </TitledPaper>
        ),
        layout: {x: 0, y: index, w: 1, h: 1},
      }))}
      style={Object.assign(
        {},
        {width: '100%', height: '100%'},
        props.style ?? {}
      )}
      onDrag={handleDrag}
    />
  );
}
