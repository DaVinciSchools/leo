import {PropsWithChildren, ReactNode} from 'react';
import {styled} from 'styled-components';

const Title = styled.div`
  font-size: 24px;
  margin: ${props => props.theme.spacing(2)} 0;
`;

export function DefaultPage(
  props: PropsWithChildren<{
    title: ReactNode;
  }>
) {
  return (
    <>
      <Title>{props.title}</Title>
      {props.children}
    </>
  );
}
