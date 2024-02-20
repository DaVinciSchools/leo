import {Link} from 'react-router-dom';
import {styled} from 'styled-components';

const Wrapper = styled.div`
  display: flex;
  padding: ${props => props.theme.spacing(3)};
  gap: ${props => props.theme.spacing(2)};
  place-items: center;
`;

export function Header() {
  return (
    <Wrapper>
      <Link style={{flexGrow: 1}} to="/">
        <img src="/images/logo-orange-on-white.svg" />
      </Link>
    </Wrapper>
  );
}
