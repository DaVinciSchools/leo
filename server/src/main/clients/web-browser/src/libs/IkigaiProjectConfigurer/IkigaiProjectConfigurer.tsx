import '../global.scss';

import {CSSProperties} from 'react';
import {VISIBLE_ALPHA} from '../../Ikigai/Ikigai';
import Markdown from 'react-markdown';
import {getCategoryId, ProjectInput} from '../ProjectBuilder/ProjectBuilder';
import {DeepReadOnly, modifyInDeepReadOnly} from '../misc';
import {styled} from 'styled-components';
import {Checkbox} from '@mui/material';

const Category = styled.div`
  border: 1px solid ${props => props.theme.palette.grey[400]};
  margin-bottom: ${props => props.theme.spacing(2)};
`;

const CategoryHeader = styled.div<{
  $highlightHue?: number;
  selected: boolean;
}>`
  align-items: center;
  background: ${props =>
    props.selected
      ? `hsla(${props.$highlightHue}, 100%, 75%, ${VISIBLE_ALPHA})`
      : props.theme.palette.grey[200]};
  cursor: pointer;
  border-bottom: 1px solid ${props => props.theme.palette.grey[200]};
  display: flex;
  font-weight: bold;
`;

const CategoryBody = styled.div`
  padding: ${props => props.theme.spacing(1)};
`;

const CategoryList = styled.div`
  height: 100%;
  position: absolute;
`;

export function IkigaiProjectConfigurer(
  props: DeepReadOnly<{
    inputs: ProjectInput[];
    setInputs: (newInputs: DeepReadOnly<ProjectInput[]>) => void;
    style?: Partial<CSSProperties>;
  }>
) {
  function handleSelect(input: ProjectInput) {
    props.setInputs(
      modifyInDeepReadOnly(
        props.inputs,
        input,
        i2 => (i2.selected = !i2.selected),
        i2 => getCategoryId(i2.input?.category)
      )
    );
  }

  return (
    <CategoryList>
      {props.inputs.map(input => (
        <Category key={getCategoryId(input.input.category).toString()}>
          <CategoryHeader
            selected={input.selected}
            $highlightHue={input.highlightHue}
            onClick={() => handleSelect(input)}
          >
            <Checkbox
              checked={input.selected}
              onChange={() => {
                handleSelect(input);
              }}
            />
            <div>{input.input.category?.name ?? '[Unknown Category]'}</div>
          </CategoryHeader>
          <CategoryBody>
            <Markdown className="global-markdown">
              {input.input?.category?.shortDescr ?? '[No Short Description]'}
            </Markdown>
          </CategoryBody>
        </Category>
      ))}
    </CategoryList>
  );
}
