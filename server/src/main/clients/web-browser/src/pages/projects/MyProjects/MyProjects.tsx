import './MyProjects.scss';
import '../../../libs/global.scss';

import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {GlobalStateContext} from '../../../libs/GlobalState';
import {createService} from '../../../libs/protos';
import {
  assignment_management,
  pl_types,
  post_service,
  project_management,
  tag_service,
} from '../../../generated/protobuf-js';
import {useContext, useEffect, useRef, useState} from 'react';

import IProject = pl_types.IProject;
import ProjectManagementService = project_management.ProjectManagementService;
import {useDelayedAction} from '../../../libs/delayed_action';
import {replaceInPlace} from '../../../libs/misc';
import {ASSIGNMENT_SORTER, PROJECT_SORTER} from '../../../libs/sorters';
import {useFormFields} from '../../../libs/forms';
import {ProjectsAutocomplete} from '../../../libs/common_fields/ProjectsAutocomplete';
import IAssignment = pl_types.IAssignment;
import AssignmentManagementService = assignment_management.AssignmentManagementService;
import {TabbedSwiper} from '../../../libs/TabbedSwiper/TabbedSwiper';
import {ProjectEditor} from '../../../libs/ProjectEditor/ProjectEditor';
import {PostEditor} from '../../../libs/PostEditor/PostEditor';
import TagService = tag_service.TagService;
import PostService = post_service.PostService;
import IProjectPost = pl_types.IProjectPost;
import {Button} from '@mui/material';
import {Add, Clear} from '@mui/icons-material';
import ITag = pl_types.ITag;
import {PostsFeed} from '../../../libs/PostsFeed/PostsFeed';

enum TabValue {
  OVERVIEW,
  EDIT_PROJECT,
  CREATE_POST,
  VIEW_POSTS,
}

export function MyProjects() {
  const global = useContext(GlobalStateContext);

  const [activeTab, setActiveTab] = useState<TabValue>(TabValue.OVERVIEW);

  // Project editor tab.

  const [sortedProjects, setSortedProjects] = useState<readonly IProject[]>([]);
  const [selectedProject, setSelectedProject] = useState<IProject | null>(null);
  const [sortedAssignments, setSortedAssignments] = useState<
    readonly IAssignment[]
  >([]);
  const projectForm = useFormFields({
    onChange: () => projectAutoSaveProject.trigger(),
    disabled: selectedProject == null,
  });
  const [projectSaveStatus, setProjectSaveStatus] = useState<string>('');
  const projectAutoSaveProject = useDelayedAction(
    () => {
      setProjectSaveStatus('Modified');
      if (selectedProject != null) {
        const newProject = projectForm.getValuesObject(true, selectedProject);
        setSortedProjects(
          replaceInPlace(sortedProjects.slice(), newProject, e => e?.id).sort(
            PROJECT_SORTER
          )
        );
      }
    },
    () => {
      setProjectSaveStatus('Saving...');
      if (selectedProject != null && projectForm.verifyOk(true)) {
        return createService(
          ProjectManagementService,
          'ProjectManagementService'
        )
          .updateProject({
            project: projectForm.getValuesObject(true, selectedProject),
          })
          .then(() => {
            setProjectSaveStatus('Saved');
          })
          .catch(global.setError);
      } else {
        setProjectSaveStatus('Invalid values, Not saved');
      }
      return;
    },
    1500
  );

  // Post editor tab.

  const [sortedTags, setSortedTags] = useState<readonly string[]>([]);
  const postForm = useFormFields({
    onChange: () => postAutoSavePost.trigger(),
    disabled: selectedProject == null,
  });
  const postId = useRef<number | undefined>(undefined);
  const postBeingEdited = useRef<boolean>(true);
  const [postSaveStatus, setPostSaveStatus] = useState<string>('');
  const postAutoSavePost = useDelayedAction(
    () => {
      setPostSaveStatus('Modified');
    },
    () => {
      setPostSaveStatus('Saving...');
      if (selectedProject != null && postForm.verifyOk(true)) {
        const projectPost: IProjectPost = postForm.getValuesObject(true);
        projectPost.id = postId.current;
        projectPost.beingEdited = postBeingEdited.current;
        projectPost.project = selectedProject;
        projectPost.tags = (projectPost.tags as string[])?.map(e => {
          return {
            text: e,
            userXId: global.userX?.userXId,
          } as ITag;
        });
        projectPost.name = projectPost.name ?? '';
        return createService(PostService, 'PostService')
          .upsertProjectPost({
            projectPost: projectPost,
          })
          .then(response => {
            postId.current = response.projectPostId ?? undefined;
            setPostSaveStatus('Saved');
          })
          .catch(global.setError);
      } else {
        setPostSaveStatus('Invalid values, Not saved');
      }
      return;
    },
    1500
  );

  // View posts tab.

  const [projectPosts, setProjectPosts] = useState<IProjectPost[]>([]);

  // Hidden form to track project selection.

  const hiddenForm = useFormFields({
    onChange: () => {
      setSelectedProject(hiddenProject.getValue() ?? null);
    },
  });
  const hiddenProject = hiddenForm.useAutocompleteFormField<IProject | null>(
    'project'
  );

  // Initialize the data.

  useEffect(() => {
    // Projects
    createService(ProjectManagementService, 'ProjectManagementService')
      .getProjects({userXId: global.userX?.userXId, activeOnly: true})
      .then(response => {
        setSortedProjects(response.projects.sort(PROJECT_SORTER));
        setSelectedProject(null);
      })
      .catch(global.setError);

    // Assignments
    createService(AssignmentManagementService, 'AssignmentManagementService')
      .getAssignments({
        teacherId: global.userX?.teacherId,
        studentId: global.userX?.studentId,
      })
      .then(response => {
        setSortedAssignments(response.assignments.sort(ASSIGNMENT_SORTER));
      });

    // Previously used tags.
    createService(TagService, 'TagService')
      .getAllPreviousTags({userXId: global.userX?.userXId})
      .then(response => {
        setSortedTags(
          [...new Set(response.tags.map(e => e.text ?? ''))].sort()
        );
      })
      .catch(global.setError);
  }, [global.userX]);

  useEffect(() => {
    if (selectedProject == null) {
      projectForm.setValuesObject({});
      postForm.setValuesObject({});
      postId.current = undefined;
      postBeingEdited.current = true;
      setProjectPosts([]);
    } else {
      projectForm.setValuesObject(selectedProject);
      postForm.setValuesObject({});
      postId.current = undefined;
      postBeingEdited.current = true;
      createService(PostService, 'PostService')
        .getProjectPosts({
          projectIds: [selectedProject?.id ?? 0],
          beingEdited: true,
        })
        .then(response => {
          postId.current = response.projectPosts[0]?.id ?? undefined;
          postBeingEdited.current =
            response.projectPosts[0]?.beingEdited ?? true;

          postForm.setValuesObject(
            Object.assign({}, response.projectPosts[0] ?? {}, {
              tags: (response.projectPosts[0]?.tags ?? [])
                .map(tag => tag.text ?? '')
                .filter(e => e.length > 0),
            })
          );
        })
        .catch(global.setError);
      createService(PostService, 'PostService')
        .getProjectPosts({
          projectIds: selectedProject ? [selectedProject.id ?? 0] : [],
          includeComments: true,
          includeTags: true,
          beingEdited: false,
        })
        .then(response => {
          console.log(response.projectPosts);
          setProjectPosts(response.projectPosts);
        })
        .catch(global.setError);
    }
  }, [selectedProject]);

  if (!global.requireUserX(userX => userX?.isAuthenticated)) {
    return <></>;
  }

  return (
    <>
      <DefaultPage title="My Projects">
        <div
          className="global-flex-row"
          style={{gap: '1em', alignItems: 'center', justifyContent: 'stretch'}}
        >
          <span style={{fontWeight: 'bold', whiteSpace: 'nowrap'}}>
            Select Project
          </span>
          <ProjectsAutocomplete
            sortedProjects={sortedProjects}
            formField={hiddenProject}
            style={{width: '100%'}}
          />
          <span style={{whiteSpace: 'nowrap'}}>
            {{
              [TabValue.OVERVIEW]: <></>,
              [TabValue.EDIT_PROJECT]: <>{projectSaveStatus}</>,
              [TabValue.CREATE_POST]: <>{postSaveStatus}</>,
              [TabValue.VIEW_POSTS]: <></>,
            }[activeTab] || <></>}
          </span>
        </div>
        <div style={{height: '100%'}}>
          <TabbedSwiper
            tabs={[
              {
                key: TabValue.OVERVIEW,
                label: 'Overview',
                content: (
                  <>
                    <div className="global-flex-column">TODO</div>
                  </>
                ),
              },
              {
                key: TabValue.EDIT_PROJECT,
                label: 'Edit Project',
                content: (
                  <>
                    <div className="global-flex-column">
                      <ProjectEditor
                        projectForm={projectForm}
                        sortedAssignments={sortedAssignments}
                      />
                    </div>
                  </>
                ),
              },
              {
                key: TabValue.CREATE_POST,
                label: 'Create Post',
                content: (
                  <>
                    <div className="global-flex-column">
                      <PostEditor sortedTags={sortedTags} postForm={postForm} />
                      <div className="global-form-buttons">
                        <Button
                          variant="contained"
                          disabled={selectedProject == null}
                          startIcon={<Add />}
                          onClick={() => {
                            postAutoSavePost.forceDelayedAction(
                              () => {
                                postId.current = undefined;
                                postBeingEdited.current = true;
                                postForm.setValuesObject({});
                              },
                              () => {
                                postBeingEdited.current = false;
                              }
                            );
                          }}
                        >
                          Post Message
                        </Button>
                        <Button
                          variant="contained"
                          disabled={selectedProject == null}
                          color="warning"
                          startIcon={<Clear />}
                          onClick={() => postForm.setValuesObject({})}
                        >
                          Reset Form
                        </Button>
                      </div>
                    </div>
                  </>
                ),
              },
              {
                key: TabValue.VIEW_POSTS,
                label: 'View Posts',
                content: (
                  <>
                    <PostsFeed posts={projectPosts} />
                  </>
                ),
              },
            ]}
            onTabChange={setActiveTab}
          />
        </div>
      </DefaultPage>
    </>
  );
}
