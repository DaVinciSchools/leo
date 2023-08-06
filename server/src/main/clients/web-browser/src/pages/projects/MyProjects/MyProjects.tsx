import './MyProjects.scss';
import {useContext, useEffect, useRef, useState} from 'react';
import {
  createService,
  pl_types,
  project_management,
} from '../../../libs/protos';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {sendToLogin} from '../../../libs/authentication';
import {Dropdown} from 'antd';
import {ProjectPage} from '../../../libs/ProjectPage/ProjectPage';
import IProject = pl_types.IProject;
import ProjectManagementService = project_management.ProjectManagementService;
import {ItemType} from 'antd/es/menu/hooks/useItems';
import IProjectPost = pl_types.IProjectPost;
import {GlobalStateContext} from '../../../libs/GlobalState';

export function MyProjects() {
  const global = useContext(GlobalStateContext);
  if (!global.user) {
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
      .getProjects({userXId: global.user?.userXId, activeOnly: true})
      .then(response => {
        setProjects(response.projects);
      })
      .catch(global.setError);
  }, []);

  useEffect(() => {
    if (project == null) {
      projectId.current = undefined;
      setPosts(undefined);
    } else {
      projectId.current = project.id!;
      service
        .getProjectPosts({projectId: project!.id!})
        .then(response => {
          if (project.id! === projectId.current) {
            setPosts(response.projectPosts);
          }
        })
        .catch(reason => global.setError({error: reason, reload: false}));
    }
  }, [project]);

  function updateProject(project: IProject, modifications: IProject) {
    service.updateProject({id: project.id!, modifications: modifications});

    const newProjects = [...projects];
    Object.assign(project, modifications);
    setProjects(newProjects);
  }

  function postMessage(name: string, messageHtml: string) {
    service
      .postMessage({
        projectId: project!.id!,
        name: name,
        messageHtml: messageHtml,
      })
      .then(response => {
        if (project!.id! === projectId.current) {
          const newPosts: IProjectPost[] = [
            ...(posts ?? []),
            {
              id: response.projectPostId!,
              name: name,
              messageHtml: messageHtml,
              user: global.user,
            },
          ];
          setPosts(newPosts);
        }
      })
      .catch(reason => global.setError({error: reason, reload: false}));
  }

  function deletePost(post: pl_types.IProjectPost) {
    service
      .deletePost({id: post.id!})
      .then(() => {
        if (project!.id! === projectId.current) {
          const newPosts: IProjectPost[] = [...(posts ?? [])].filter(
            p => p.id !== post.id
          );
          setPosts(newPosts);
        }
      })
      .catch(reason => global.setError({error: reason, reload: false}));
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
              longDescrHtml={project.longDescrHtml!}
              posts={posts}
              milestones={[]}
              updateProject={modifications =>
                updateProject(project, modifications)
              }
              onDeletePost={post => deletePost(post)}
              onSubmitPost={postMessage}
              editable={true}
            />
          ) : (
            <></>
          )}
        </div>
      </DefaultPage>
    </>
  );
}
