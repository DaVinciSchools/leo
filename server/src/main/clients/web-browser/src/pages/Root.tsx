import {styled} from 'styled-components';
import {Link} from 'react-router-dom';
import {useNavigate} from 'react-router';
import {Button, Container} from '@mui/material';

const Footer = styled.div`
  background: black;
  color: white;
  padding: ${props => props.theme.spacing(4)};
`;

const Hero = styled.div`
  display: flex;
  place-items: center;
  max-width: 1440px;
  padding: ${props => props.theme.spacing(6)};
  margin: 0 auto;
  font-size: 20px;
  margin-bottom: ${props => props.theme.spacing(16)};

  h1 {
    font-size: 54px;
    margin-bottom: ${props => props.theme.spacing(2)};
  }

  p {
    color: #424242;
    font-size: 20px;
    margin: 0 ${props => props.theme.spacing(6)}
      ${props => props.theme.spacing(2)} 0;
  }
`;

const HeroSubheader = styled.div`
  font-weight: bold;
  margin-bottom: ${props => props.theme.spacing(4)};
`;

const Header = styled.div`
  display: flex;
  padding: ${props => props.theme.spacing(3)};
  gap: ${props => props.theme.spacing(2)};
  place-items: center;
`;

const BodySection = styled.section`
  display: flex;
  place-items: center;
  gap: ${props => props.theme.spacing(10)};
  margin-bottom: ${props => props.theme.spacing(16)};

  img {
    max-width: 440px;
  }
`;

const GridBodySectionContent = styled.section`
  display: flex;
  place-items: center;
  align-items: stretch;
  gap: ${props => props.theme.spacing(4)};
  font-size: 18px;
  margin-bottom: ${props => props.theme.spacing(16)};

  p {
    margin: 0;
  }
`;

const BodySectionHeader = styled.h2`
  margin-bottom: ${props => props.theme.spacing(4)};
`;

const BodySectionContent = styled.section`
  font-size: 20px;

  p {
    margin: 0;
  }
`;

const BodyBlock = styled.div`
  box-shadow: 3px 3px 10px 0px rgba(0, 0, 0, 0.25);
  border-radius: 20px;
  flex: 1;
  padding: ${props => props.theme.spacing(3)};

  div {
    font-size: 22px;
    font-weight: 600;
  }
`;

export function Root() {
  const navigate = useNavigate();

  return (
    <header>
      <Header>
        <Link style={{flexGrow: 1}} to="/">
          <img src="/images/logo-orange-on-white.svg" />
        </Link>

        <Link to="/users/login.html">
          <Button variant="outlined" color="warning">
            Log in
          </Button>
        </Link>
        <Button
          onClick={() => navigate('/demos/project-builder.html')}
          variant="contained"
          color="warning"
        >
          Try it out
        </Button>
      </Header>
      <Hero>
        <div>
          <h1>
            Engaging Students.
            <br />
            Empowering Teachers.
          </h1>
          <HeroSubheader>
            Unlock Your Students' Full Potential With Project Leo
          </HeroSubheader>
          <p>
            Designed in collaboration with the students and faculty at Da Vinci
            Schools, Project Leo offers essential tools to integrate
            personalized Project Based Learning in any classroom. Our
            AI-enhanced platform fosters student creativity, allowing them to
            build inspiring projects while receiving valuable feedback from
            teachers, professionals, and peers.
          </p>
          <Button
            onClick={() => navigate('/demos/project-builder.html')}
            variant="contained"
            color="warning"
            size="large"
          >
            Try it out
          </Button>
        </div>
        <img src="/images/landing/hero.png" />
      </Hero>
      <Container>
        <BodySection>
          <BodySectionContent>
            <BodySectionHeader>What is Project Leo?</BodySectionHeader>
            <p>
              Project Leo is an AI-powered classroom platform designed for
              students and teachers engaged in project-based learning,
              portfolio, or similar curricula. Our platform draws inspiration
              from the ikigai philosophy to engage students in their educational
              journey. Project Leo also allows teachers to incorporate
              differentiation more easily into their classes, thus helping
              students find their own unique paths to success.
            </p>
          </BodySectionContent>
          <img src="/images/landing/group.png" />
        </BodySection>
        <BodySection>
          <div>
            <BodySectionContent>
              <BodySectionHeader>Why Ikigai?</BodySectionHeader>
              <p>
                Project Leo utilizes the ikigai philosophy to engage students in
                their educational journey. By leveraging this approach with a
                school’s specific standards, we transform assignments into a
                meaningful exploration of students' personal and professional
                goals. Our approach is not just about completing tasks; it's
                about supporting students as they uncover and cultivate
                individual passions and interests. Learn more about our use of
                the ikigai approach here.
              </p>
            </BodySectionContent>
          </div>
          <img src="/images/landing/ikigai.png" />
        </BodySection>
        <div>
          <BodySectionHeader>Why Project Leo?</BodySectionHeader>
          <GridBodySectionContent>
            <BodyBlock>
              <div>For Students</div>
              <p>
                Project Leo actively assists students in designing, researching,
                and creating projects across all classroom subjects. A key
                advantage of Project Leo in project-based learning is its
                ability to nurture students' passion by focusing on subjects and
                topics that intrigue them personally. Project Leo's structured
                guidance leads students through project phases, ensuring
                consistent progress without always needing direct teacher
                intervention. Students enjoy the independence Project Leo
                fosters, coupled with personalized, timely feedback from
                teachers, all within a user-friendly platform.
              </p>
            </BodyBlock>
            <BodyBlock>
              <div>For Teachers</div>
              <p>
                Project Leo offers a dynamic framework for teachers, enabling
                the customization of classroom experiences to meet the
                individual needs and talents of each student. Our intuitive
                dashboard tracks each student's progress, allowing for a
                simplified feedback process for the teacher. Furthermore, the AI
                component of Project Leo efficiently handles routine inquiries,
                freeing teachers to focus on more complex educational
                challenges.
              </p>
            </BodyBlock>
          </GridBodySectionContent>
        </div>
        <BodySection>
          <div>
            <BodySectionContent>
              <BodySectionHeader>Our Mission</BodySectionHeader>
              <p>
                Project Leo is committed to empowering teachers and engaging
                students through a personalized and dynamic learning experience.
                We aim to promote collaboration and accountability by enabling
                students to generate their own projects, showcase their
                progress, and receive feedback from teachers and peers. Our goal
                is to foster a love of lifelong learning by providing a platform
                for students to explore their passions, build new skills, and
                prepare for future careers.
              </p>
            </BodySectionContent>
          </div>
          <img src="/images/landing/kids-jumping.png" />
        </BodySection>
      </Container>
      <Footer>
        <img src="/images/landing/logo-footer.svg" />
      </Footer>
    </header>
  );
}
