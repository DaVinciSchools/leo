import './Root.scss';

import {Link} from 'react-router-dom';
import {useNavigate} from 'react-router';

export function Root() {
  const navigate = useNavigate();

  // This is the main landing page.

  return (
    <>
      <header>
        <Link to="/" className="header-section header-section-left">
          <img src="/images/logo-white-on-orange.svg" />
        </Link>
        <div
          className="header-section header-section-center"
          style={{whiteSpace: 'nowrap'}}
        >
          <a href="#what_is_project_leo" className="nav-link">
            About
          </a>
          <a href="#our_mission" className="nav-link">
            Our Mission
          </a>
          <Link
            to="/projects/projects.html"
            className="nav-link"
            reloadDocument={true}
          >
            Projects
          </Link>
        </div>
        <div className="header-section header-section-right">
          <Link to="/users/login.html">
            <button className="primary">Login</button>
          </Link>
          <button
            className="light"
            onClick={() => navigate('/demos/project-builder.html')}
          >
            Try it out!
          </button>
        </div>
      </header>
      <main>
        <section
          className="primary-section"
          style={{display: 'flex', alignItems: 'flex-start', gap: '15px'}}
        >
          <div>
            <h1>Engaging Students. Empowering Teachers.</h1>
            <div className="subheading">
              Unlock Your Classroom's Full Potential with Project Leo
            </div>
            <div className="summary">
              Designed with the teachers and students at Da Vinci Schools,
              Project Leo provides the tools necessary to bring personalized
              Project Based Learning into every classroom. Our AI-enhanced
              learning platform gives students the freedom to build projects
              that truly inspire them while getting feedback from teachers,
              professionals, and peers throughout the process. Leverage the
              latest AI tools in a safe way to give your students a more
              purposeful and engaging learning experience.
            </div>
            <button
              className="light"
              onClick={() => navigate('/demos/project-builder.html')}
            >
              Try it out!
            </button>
          </div>
          <img src="/images/landing/girl_at_board.jpg" />
        </section>
        <img src="/images/landing/swoop.svg" style={{width: '100%'}} />
        <section className="details-with-image">
          <div id="what_is_project_leo">
            <h2>What is Project Leo?</h2>
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
          </div>
          <img src="/images/landing/group.png" />
        </section>
        <section className="details-with-image">
          <img src="/images/placeholder.jpg" />
          <div>
            <h2>Why Project Leo?</h2>
            <p>
              Project Leo not only provides project ideas that align with school
              learning objectives but also helps students develop career skills.
              By incorporating the students' interests, market demands, and
              skills, Project Leo offers a unique opportunity for students to
              explore their passions while preparing for their future careers.
              This approach empowers students to take ownership of their
              learning and enables them to develop a clear understanding of how
              their education can contribute to their professional goals.
            </p>
          </div>
        </section>
        <section className="details">
          <div>
            <h2>How does it work?</h2>
            <div className="timeline-with-image">
              <div>
                <h3>Students generate a unique project</h3>
                <p>
                  Project Leo allows students to take ownership of their
                  learning by generating personalized projects based on their
                  interests. Using AI technology, Project Leo aligns students'
                  interests with class objectives and possible career paths,
                  creating a truly customized and engaging learning experience.
                </p>
              </div>
              <div>
                <img src="/images/landing/woman-lightbulb.png" />
              </div>
            </div>
            <div className="timeline-with-image">
              <img src="/images/landing/man-at-desk.png" />
              <div>
                <h3>Students post their progress</h3>
                <p>
                  Project Leo allows students to take ownership of their
                  learning by generating personalized projects based on their
                  interests. Using AI technology, Project Leo aligns students'
                  interests with class objectives and possible career paths,
                  creating a truly customized and engaging learning experience.
                </p>
              </div>
            </div>
            <div className="timeline-with-image">
              <div>
                <h3>Teachers and professionals review and provide feedback</h3>
                <p>
                  Teachers and professionals in related fields can monitor
                  student progress and provide valuable feedback throughout the
                  project process. This collaborative approach promotes a
                  supportive learning environment where students can receive
                  guidance and support from multiple sources, including those
                  with specialized knowledge and expertise. With this approach,
                  students are able to work closely with both their teachers and
                  industry professionals, fostering both academic and personal
                  growth.
                </p>
              </div>
              <img src="/images/landing/man-messaging.png" />
            </div>
            <div className="timeline-with-image">
              <img src="/images/landing/people-computer.png" />
              <div>
                <h3>Project Leo outputs final results</h3>
                <p>
                  Project Leo generates a summary of project results for both
                  students and teachers, providing a clear understanding of
                  project outcomes and serving as a tool for continuous
                  improvement. Students can use the information to reflect on
                  their learning and set future goals, while teachers can easily
                  grade projects and provide feedback to students, promoting a
                  collaborative and effective learning environment.
                </p>
              </div>
            </div>
            <div className="timeline-with-image">
              <div>
                <h3>Move on to the next project</h3>
                <p>
                  Project Leo generates an unlimited amount of engaging project
                  ideas, allowing students to explore new topics and areas of
                  interest, while building upon existing skills. This feature
                  promotes a love of lifelong learning, while keeping students
                  engaged and motivated to continue exploring and learning.
                </p>
              </div>
              <img src="/images/landing/woman-on-books.png" />
            </div>
          </div>
        </section>
        <section className="details">
          <div id="our_mission">
            <h2>Our Mission</h2>
            <p>
              At Project Leo, our mission is to empower teachers and students by
              providing a personalized and engaging learning experience. We aim
              to promote collaboration and accountability by enabling students
              to generate their own projects, showcase their progress, and
              receive feedback from teachers. Our goal is to foster a love of
              lifelong learning by providing a platform for students to explore
              their passions, build new skills, and prepare for future careers.
            </p>
          </div>
          <img src="/images/landing/kids-jumping.png" />
        </section>
        <section className="details">
          <div>
            <h2>Contact Us</h2>
            <p>
              Interested in learning more about Project Leo? Are you
              experiencing any site issues? While you are exploring, please send
              all feedback and issues to Steven Eno -{' '}
              <a href="mailto:seno@davincischools.org">
                seno@davincischools.org
              </a>
            </p>
          </div>
        </section>
      </main>
      <footer>
        Images by <Link to="https://www.freepik.com/">Freepik</Link>.
      </footer>
    </>
  );
}
