import '../global.scss';

import {CSSProperties} from 'react';
import {VISIBLE_ALPHA} from '../../Ikigai/Ikigai';
import Markdown from 'react-markdown';
import {getCategoryId, ProjectInput} from '../ProjectBuilder/ProjectBuilder';
import {DeepReadOnly, modifyInDeepReadOnly} from '../misc';
import {styled} from 'styled-components';
import {Button, Checkbox, Chip} from '@mui/material';
import {pl_types} from 'pl-pb';
import ValueType = pl_types.ProjectInputCategory.ValueType;

const Category = styled.div`
  padding-bottom: ${props => props.theme.spacing(2)};
`;

const CategoryContent = styled.div`
  border: 1px solid ${props => props.theme.palette.grey[400]};
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
  display: flex;
  gap: ${props => props.theme.spacing(1)};
  flex-direction: column;
`;

const NoValues = styled.div`
  color: ${props => props.theme.palette.grey[400]};
  text-align: center;
`;

const CategoryList = styled.div`
  height: 100%;
  position: absolute;
  max-width: 100%;
`;

const ChipList = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: 8px 8px;
`;

export function IkigaiProjectConfigurer(
  props: DeepReadOnly<{
    inputs: ProjectInput[];
    setInputs: (newInputs: DeepReadOnly<ProjectInput[]>) => void;
    onClickEditInputValues: (input: ProjectInput) => void;
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
      {props.inputs.map(input => {
        let hasValues = false;
        switch (input.input.category?.valueType ?? ValueType.UNSET_VALUE_TYPE) {
          case pl_types.ProjectInputCategory.ValueType.FREE_TEXT:
            hasValues = !!input.input.freeTexts?.length;
            break;
          default:
            hasValues = !!input.input.selectedIds?.length;
        }

        return (
          <Category key={getCategoryId(input.input.category).toString()}>
            <CategoryContent>
              <CategoryHeader
                selected={input.selected}
                $highlightHue={input.highlightHue}
                onClick={() => handleSelect(input)}
              >
                <Checkbox
                  color="secondary"
                  checked={input.selected}
                  onChange={() => {
                    handleSelect(input);
                  }}
                />
                <div>{input.input.category?.name ?? '[Unknown Category]'}</div>
              </CategoryHeader>
              <CategoryBody>
                <Markdown className="global-markdown">
                  {input.input?.category?.shortDescr ??
                    '[No Short Description]'}
                </Markdown>
                {hasValues ? (
                  <ChipList>
                    {input.input.freeTexts?.map((freeText, i) => {
                      return (
                        <Chip
                          key={`${freeText}-${i}`}
                          sx={{
                            height: 'auto',
                            '& .MuiChip-label': {
                              display: 'block',
                              whiteSpace: 'normal',
                              padding: '8px 12px',
                            },
                          }}
                          label={freeText}
                        />
                      );
                    })}
                    {input.input.selectedIds?.map(id => {
                      const option = input.input.category?.options?.find(
                        option => option.id === id
                      );

                      return (
                        <Chip
                          key={id}
                          sx={{
                            height: 'auto',
                            '& .MuiChip-label': {
                              display: 'block',
                              whiteSpace: 'normal',
                              padding: '8px 12px',
                            },
                          }}
                          label={option?.name}
                        />
                      );
                    })}
                  </ChipList>
                ) : (
                  <NoValues>No values</NoValues>
                )}
                <Button
                  sx={{
                    alignSelf: 'end',
                  }}
                  onClick={() => {
                    props.onClickEditInputValues(input);
                  }}
                  variant="outlined"
                  size="small"
                  disabled={!input.selected}
                  color="secondary"
                >
                  Edit
                </Button>
              </CategoryBody>
            </CategoryContent>
          </Category>
        );
      })}
    </CategoryList>
  );
}
