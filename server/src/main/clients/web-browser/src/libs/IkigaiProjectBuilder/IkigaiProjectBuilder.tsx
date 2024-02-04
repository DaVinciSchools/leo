import '../global.scss';
import './IkigaiProjectBuilder.scss';

import {PaperProps} from '@mui/material';
import {CSSProperties, ReactNode} from 'react';
import {Ikigai} from '../../Ikigai/Ikigai';
import {DeepReadOnly} from '../misc';
import {getCategoryId, ProjectInput} from '../ProjectBuilder/ProjectBuilder';
import Markdown from 'react-markdown';

export const MODAL_STYLE: Partial<CSSProperties> = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: '65%',
  height: '65%',
};

export const MODAL_PAPER_PROPS: PaperProps = {
  elevation: 24,
};

export function IkigaiProjectBuilder(
  props: DeepReadOnly<{
    inputs: ProjectInput[];
    setInput: (input: DeepReadOnly<ProjectInput>) => void;
    noInputsText?: ReactNode;
    inputDiameter: (width: number, height: number) => number;
    distanceToInputCenter: (width: number, height: number) => number;
    enabled: boolean;
    onInputClick: (input: ProjectInput) => void;
    onSpinClick: (inputs: DeepReadOnly<ProjectInput[]>) => void;
    style?: Partial<CSSProperties>;
  }>
) {
  return (
    <div style={props.style ?? {}} className="ikigai-project-builder">
      {(props?.inputs?.length ?? 0) > 0 && (
        <Ikigai
          id="ikigai-project-builder"
          categoryDiameter={(width, height) =>
            (Math.min(width, height) / 2) * 0.95
          }
          distanceToCategoryCenter={(width, height) =>
            (Math.min(width, height) / 4) * 0.85
          }
          radians={0}
          enabled={true}
          processing={false}
          categoryElementIds={props?.inputs?.map(input =>
            String(getCategoryId(input?.input?.category))
          )}
          showSpinButton={
            props?.inputs?.length > 0 &&
            props?.inputs?.every(
              input =>
                (input?.input?.freeTexts?.length ?? 0) > 0 ||
                (input?.input?.selectedIds?.length ?? 0) > 0
            )
          }
          onSpinClick={() => props.onSpinClick(props.inputs)}
          radiansOffset={0}
        >
          {props.inputs?.map(input => {
            return (
              <div
                id={getCategoryId(input.input?.category).toString()}
                key={getCategoryId(input.input?.category).toString()}
                onClick={() => {
                  props.onInputClick(input);
                }}
                className="ikigai-project-builder-panel"
              >
                <div className="ikigai-project-builder-title">
                  <Markdown className="global-markdown">
                    {input.input.category?.name ?? ''}
                  </Markdown>
                </div>
              </div>
            );
          })}
        </Ikigai>
      )}
      {((props?.inputs ?? []).length ?? 0) === 0 && (
        <div className="ikigai-project-builder-no-categories">
          <span>
            {props.noInputsText ??
              'There are no categories for the Ikigai diagram.'}
          </span>
        </div>
      )}
    </div>
  );
}
