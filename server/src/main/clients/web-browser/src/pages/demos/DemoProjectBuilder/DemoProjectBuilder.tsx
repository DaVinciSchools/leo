import './DemoProjectBuilder.scss';
import {ProjectBuilder} from '../../../libs/ProjectBuilder/ProjectBuilder';
import {styled} from 'styled-components';
import {Header} from '../../../libs/Header';

const Wrapper = styled.div`
  display: flex;
  flex-direction: column;
  height: 100vh;
  overflow: auto;
`;

export function DemoProjectBuilder() {
  return (
    <Wrapper>
      <Header />
      <ProjectBuilder
        noCategoriesText={'Select categories on the left'}
        isDemoPage={true}
      />
    </Wrapper>
  );
}
