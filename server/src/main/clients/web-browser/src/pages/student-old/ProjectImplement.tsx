import {useSearchParams} from 'react-router-dom';
import {Card, HeaderSidebar} from './utils/Layout';

function App() {
  const [searchParams] = useSearchParams();

  return (
    <main>
      <h2>Project Implementation</h2>
      <HeaderSidebar>
        <header>
          <h3>Project Idea</h3>
          <p>{searchParams.get('description')}</p>
        </header>
        <section className="sidebar">
          <h3>Implementation Steps</h3>
          <ol>
            <li>Lorem</li>
            <li>Ipsum</li>
            <li>Dolor</li>
            <li>Sit</li>
            <li>Amet</li>
            <li>Orange</li>
            <li>Pufferfish</li>
          </ol>
        </section>
        <section>
          <h3>Professional Connections</h3>
          <div className="tiles">
            <Card
              title="John Doe"
              imageSrc="/images/profile.png"
              description="A doe"
              path="/student/connection-profile/john"
            />
            <Card
              title="John Doe"
              imageSrc="/images/profile.png"
              description="A doe"
              path="/student/connection-profile/john"
            />
            <Card
              title="John Doe"
              imageSrc="/images/profile.png"
              description="A doe"
              path="/student/connection-profile/john"
            />
          </div>
        </section>
      </HeaderSidebar>
    </main>
  );
}

export default App;
