import './MyProjects.scss';
import {useEffect, useRef, useState} from 'react';
import {
  createService,
  pl_types,
  project_management,
} from '../../../libs/protos';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {getCurrentUser, sendToLogin} from '../../../libs/authentication';
import {Dropdown} from 'antd';
import {ProjectPage} from '../../../libs/ProjectPage/ProjectPage';
import IProject = pl_types.IProject;
import ProjectManagementService = project_management.ProjectManagementService;
import {ItemType} from 'antd/es/menu/hooks/useItems';
import IProjectPost = pl_types.IProjectPost;

export function MyProjects() {
  const user = getCurrentUser();
  if (user == null) {
    return sendToLogin();
  }

  const projectId = useRef<number | undefined>();
  const [projects, setProjects] = useState<IProject[]>([]);
  const [project, setProject] = useState<IProject | undefined>();
  const [posts, setPosts] = useState<IProjectPost[] | undefined>();

  const service = createService(
    ProjectManagementService,
    'ProjectManagementService'
  );

  useEffect(() => {
    service
      .getProjects({userXId: user!.id, activeOnly: true})
      .then(response => {
        setProjects(response.projects);
      });
  }, []);

  useEffect(() => {
    if (project == null) {
      projectId.current = undefined;
      setPosts(undefined);
    } else {
      projectId.current = project.id!;
      service.getProjectPosts({projectId: project!.id!}).then(response => {
        if (project.id! === projectId.current) {
          setPosts(response.projectPosts);
        }
      });
    }
  }, [project]);

  function updateProject(project: IProject, modifications: IProject) {
    service.updateProject({id: project.id!, modifications: modifications});

    const newProjects = [...projects];
    Object.assign(project, modifications);
    setProjects(newProjects);
  }

  function postMessage(title: string, message: string) {
    service
      .postMessage({projectId: project!.id!, title: title, message: message})
      .then(response => {
        if (project!.id! === projectId.current) {
          const newPosts: IProjectPost[] = [
            ...(posts ?? []),
            {
              id: response.projectPostId!,
              title: title,
              message: message,
              user: user!,
            },
          ];
          setPosts(newPosts);
        }
      });
  }

  function deletePost(post: pl_types.IProjectPost) {
    service.deletePost({id: post.id!}).then(() => {
      if (project!.id! === projectId.current) {
        const newPosts: IProjectPost[] = [...(posts ?? [])].filter(
          p => p.id !== post.id
        );
        setPosts(newPosts);
      }
    });
  }

  return (
    <>
      <DefaultPage title="My Projects">
        <div style={{width: '100%'}}>
          <Dropdown.Button
            menu={{
              items: projects.map(project => ({
                key: project.id,
                label: (
                  <div
                    className="project-menu"
                    onClick={() => setProject(project)}
                    style={{width: '100%'}}
                  >
                    <span className="name">{project.name!}</span>
                    <span className="descr">{project.shortDescr!}</span>
                  </div>
                ),
              })) as ItemType[],
            }}
            style={{width: '100%'}}
          >
            {project != null ? <>{project.name!}</> : <>Select project...</>}
          </Dropdown.Button>
          {project != null ? (
            <ProjectPage
              id={project.id!}
              key={project.id!}
              name={project.name!}
              shortDescr={project.shortDescr!}
              longDescr={project.longDescr!}
              posts={posts}
              updateProject={modifications =>
                updateProject(project, modifications)
              }
              onDeletePost={post => deletePost(post)}
              onSubmitPost={postMessage}
            />
          ) : (
            <></>
          )}
        </div>
      </DefaultPage>
    </>
  );
}
