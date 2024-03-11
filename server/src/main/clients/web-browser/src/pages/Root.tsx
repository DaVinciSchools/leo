import {styled} from 'styled-components';
import {Link} from 'react-router-dom';
import {useNavigate} from 'react-router';
import {Button, Container} from '@mui/material';

const Footer = styled.div`
  background: black;
  color: white;
  padding: ${props => props.theme.spacing(4)};
`;

const HeroContainer = styled(Container)`
  display: flex;
  flex-direction: column;
  place-items: center;
  gap: ${props => props.theme.spacing(4)};

  h1 {
    font-size: 1.5em;
  }

  img {
    max-width: 100%;
    display: none;
  }

  ${props => props.theme.breakpoints.up('sm')} {
    flex-direction: row;
    gap: ${props => props.theme.spacing(10)};
    margin-bottom: ${props => props.theme.spacing(16)};
    margin-top: ${props => props.theme.spacing(12)};

    h1 {
      font-size: 64px;
      line-height: 1.2;
      margin-bottom: 24px;
    }

    p {
      font-size: 24px;
    }
  }

  ${props => props.theme.breakpoints.up('lg')} {
    img {
      display: block;
      width: 600px;
    }
  }
`;

const HeroSubheader = styled.div`
  font-weight: bold;

  ${props => props.theme.breakpoints.up('sm')} {
    margin-bottom: ${props => props.theme.spacing(4)};
    font-size: 24px;
  }
`;

const HeroButton = styled(Button)`
  ${props => props.theme.breakpoints.up('lg')} {
    font-size: 1.3rem;
  }
`;

const Header = styled.div`
  display: flex;
  flex-direction: column;
  padding: ${props => props.theme.spacing(3)};
  gap: ${props => props.theme.spacing(2)};
  place-items: center;

  ${props => props.theme.breakpoints.up('sm')} {
    flex-direction: row;
  }
`;

const HeaderLogo = styled(Link)`
  flex-grow: 1;
`;

const HeaderActionContainer = styled.div`
  display: flex;
  gap: ${props => props.theme.spacing(2)};
`;

const BodySection = styled.section`
  display: flex;
  flex-direction: column;
  place-items: center;
  gap: ${props => props.theme.spacing(10)};
  margin-bottom: ${props => props.theme.spacing(16)};

  img {
    width: 440px;
    max-width: 100%;
  }

  ${props => props.theme.breakpoints.up('sm')} {
    flex-direction: row;
  }
`;

const GridBodySectionContent = styled.section`
  display: flex;
  flex-direction: column;
  place-items: center;
  align-items: stretch;
  gap: ${props => props.theme.spacing(4)};
  margin-bottom: ${props => props.theme.spacing(16)};

  ${props => props.theme.breakpoints.up('sm')} {
    flex-direction: row;
    font-size: 18px;
  }
`;

const BodySectionHeader = styled.h2`
  ${props => props.theme.breakpoints.up('sm')} {
    font-size: 48px;
    margin-bottom: ${props => props.theme.spacing(4)};
  }
`;

const BodySectionContent = styled.section`
  p {
    margin: 0;
  }

  ${props => props.theme.breakpoints.up('sm')} {
    p {
      font-size: 20px;
    }
  }
`;

const BodyBlock = styled.div`
  box-shadow: 3px 3px 10px 0px rgba(0, 0, 0, 0.25);
  border-radius: 20px;
  flex: 1;
  padding: ${props => props.theme.spacing(3)};

  div {
    font-weight: 600;
  }

  p {
    margin: 0;
  }

  ${props => props.theme.breakpoints.up('sm')} {
    div {
      font-size: 22px;
    }

    p {
      font-size: 20px;
    }
  }
`;

export function Root() {
  const navigate = useNavigate();

  return (
    <>
      <Header>
        <HeaderLogo to="/">
          <img src="/images/logo-orange-on-white.svg" />
        </HeaderLogo>
        <HeaderActionContainer>
          <Link to="/users/login.html">
            <Button variant="outlined">Log in</Button>
          </Link>
          <Link to="/demos/project-builder.html">
            <Button variant="contained">Try it out</Button>
          </Link>
        </HeaderActionContainer>
      </Header>
      <HeroContainer maxWidth="xl">
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
          <HeroButton
            onClick={() => navigate('/demos/project-builder.html')}
            variant="contained"
            size="large"
          >
            Try it out
          </HeroButton>
        </div>
        <img src="/images/landing/hero.png" />
      </HeroContainer>
      <Container>
        <BodySection>
          <BodySectionContent>
            <BodySectionHeader>What is Project Leo?</BodySectionHeader>
            <p>
              Project Leo is an innovative educational tool designed to inspire
              both teachers and students. With Project Leo, teachers can easily
              incorporate differentiation into their classes, helping students
              find their own unique paths to success. By removing the rigidity
              of traditional classrooms, Project Leo gives students the freedom
              to pursue their passions and explore their own interests. By
              engaging students and empowering them to take control of their own
              learning, Project Leo helps to give school a purpose and prepares
              them for their future careers.
            </p>
          </BodySectionContent>
          <img src="/images/landing/group.png" />
        </BodySection>
        <div>
          <BodySectionHeader>How does it work?</BodySectionHeader>
          <GridBodySectionContent style={{flexDirection: 'column'}}>
            <BodyBlock>
              <div>Students generate a unique project</div>
              <p>
                Project Leo allows students to take ownership of their learning
                by generating personalized projects based on their interests.
                Using AI technology, Project Leo aligns students' interests with
                class objectives and possible career paths, creating a truly
                customized and engaging learning experience.
              </p>
            </BodyBlock>
            <BodyBlock>
              <div>Teachers and professionals review and provide feedback</div>
              <p>
                Teachers and professionals in related fields can monitor student
                progress and provide valuable feedback throughout the project
                process. This collaborative approach promotes a supportive
                learning environment where students can receive guidance and
                support from multiple sources, including those with specialized
                knowledge and expertise. With this approach, students are able
                to work closely with both their teachers and industry
                professionals, fostering both academic and personal growth.
              </p>
            </BodyBlock>
            <BodyBlock>
              <div>Project Leo outputs final results</div>
              <p>
                Project Leo generates a summary of project results for both
                students and teachers, providing a clear understanding of
                project outcomes and serving as a tool for continuous
                improvement. Students can use the information to reflect on
                their learning and set future goals, while teachers can easily
                grade projects and provide feedback to students, promoting a
                collaborative and effective learning environment.
              </p>
            </BodyBlock>
            <BodyBlock>
              <div>Move on to the next project</div>
              <p>
                Project Leo generates an unlimited amount of engaging project
                ideas, allowing students to explore new topics and areas of
                interest, while building upon existing skills. This feature
                promotes a love of lifelong learning, while keeping students
                engaged and motivated to continue exploring and learning.
              </p>
            </BodyBlock>
          </GridBodySectionContent>
        </div>
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
    </>
  );
}
