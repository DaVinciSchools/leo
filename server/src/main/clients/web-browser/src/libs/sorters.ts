import {pl_types, user_x_management} from 'pl-pb';
import {DeepReadOnly, toLong} from './misc';
import IOption = pl_types.ProjectInputCategory.IOption;
import IClassX = pl_types.IClassX;
import IAssignment = pl_types.IAssignment;
import IProjectDefinition = pl_types.IProjectDefinition;
import ISchool = pl_types.ISchool;
import IDistrict = pl_types.IDistrict;
import IProject = pl_types.IProject;
import State = pl_types.ProjectDefinition.State;
import IKnowledgeAndSkill = pl_types.IKnowledgeAndSkill;
import Type = pl_types.KnowledgeAndSkill.Type;
import ITag = pl_types.ITag;

export const TEXT_SORTER = (
  a: DeepReadOnly<string | null | undefined>,
  b: DeepReadOnly<string | null | undefined>
) => (a ?? '').localeCompare(b ?? '');

export const OPTION_SORTER = (
  a: DeepReadOnly<IOption | null | undefined>,
  b: DeepReadOnly<IOption | null | undefined>
) =>
  (a?.category ?? '').localeCompare(b?.category ?? '') ||
  (a?.name ?? '').localeCompare(b?.name ?? '') ||
  (a?.shortDescr ?? '').localeCompare(b?.shortDescr ?? '');

export const KNOWLEDGE_AND_SKILL_SORTER = (
  a: DeepReadOnly<IKnowledgeAndSkill | null | undefined>,
  b: DeepReadOnly<IKnowledgeAndSkill | null | undefined>
) =>
  (a?.type ?? Type.UNSET_TYPE) - (b?.type ?? Type.UNSET_TYPE) ||
  (a?.category ?? '').localeCompare(b?.category ?? '') ||
  (a?.name ?? '').localeCompare(b?.name ?? '') ||
  (a?.shortDescr ?? '').localeCompare(b?.shortDescr ?? '');

export const DISTRICT_SORTER = (
  a: DeepReadOnly<IDistrict | null | undefined>,
  b: DeepReadOnly<IDistrict | null | undefined>
) => (a?.name ?? '').localeCompare(b?.name ?? '');

export const SCHOOL_SORTER = (
  a: DeepReadOnly<ISchool | null | undefined>,
  b: DeepReadOnly<ISchool | null | undefined>
) =>
  DISTRICT_SORTER(a?.district, b?.district) ||
  (a?.name ?? '').localeCompare(b?.name ?? '') ||
  (a?.address ?? '').localeCompare(b?.address ?? '');

export const CLASS_X_SORTER = (
  a: DeepReadOnly<IClassX | null | undefined>,
  b: DeepReadOnly<IClassX | null | undefined>
) =>
  SCHOOL_SORTER(a?.school, b?.school) ||
  (a?.name ?? '').localeCompare(b?.name ?? '');

export const ASSIGNMENT_SORTER = (
  a: DeepReadOnly<IAssignment | null | undefined>,
  b: DeepReadOnly<IAssignment | null | undefined>
) =>
  CLASS_X_SORTER(a?.classX, b?.classX) ||
  (a?.name ?? '').localeCompare(b?.name ?? '');

export const PROJECT_SORTER = (
  a: DeepReadOnly<IProject | null | undefined>,
  b: DeepReadOnly<IProject | null | undefined>
) =>
  (a?.name ?? '').localeCompare(b?.name ?? '') ||
  (a?.shortDescr ?? '').localeCompare(b?.shortDescr ?? '');

export const PROJECT_POST_COMMENT_SORTER = (
  a: DeepReadOnly<pl_types.IProjectPostComment | null | undefined>,
  b: DeepReadOnly<pl_types.IProjectPostComment | null | undefined>
) => toLong(b?.postTimeMs ?? 0).compare(toLong(a?.postTimeMs ?? 0));

export const REVERSE_DATE_THEN_PROJECT_SORTER = (
  a: DeepReadOnly<IProject | null | undefined>,
  b: DeepReadOnly<IProject | null | undefined>
) =>
  (b?.id ?? 0) - (a?.id ?? 0) ||
  (a?.name ?? '').localeCompare(b?.name ?? '') ||
  (a?.shortDescr ?? '').localeCompare(b?.shortDescr ?? '');

export const PROJECT_DEFINITION_SORTER = (
  a: DeepReadOnly<IProjectDefinition | null | undefined>,
  b: DeepReadOnly<IProjectDefinition | null | undefined>
) =>
  (b?.state === State.FAILED ? -1 : 1) - (a?.state === State.FAILED ? -1 : 1) ||
  (b?.state === State.PROCESSING ? -1 : 1) -
    (a?.state === State.PROCESSING ? -1 : 1) ||
  (b?.template === true ? 1 : -1) - (a?.template === true ? 1 : -1) ||
  (a?.name ?? '').localeCompare(b?.name ?? '');

export const TAG_SORTER = (
  a: DeepReadOnly<ITag | null | undefined>,
  b: DeepReadOnly<ITag | null | undefined>
) =>
  (a?.userXId ?? 0) - (b?.userXId ?? 0) ||
  (a?.text ?? '').localeCompare(b?.text ?? '');

export const USER_X_SORTER = (
  a: DeepReadOnly<pl_types.IUserX | null | undefined>,
  b: DeepReadOnly<pl_types.IUserX | null | undefined>
) =>
  (a?.lastName ?? '').localeCompare(b?.lastName ?? '') ||
  (a?.firstName ?? '').localeCompare(b?.firstName ?? '') ||
  (a?.emailAddress ?? '').localeCompare(b?.emailAddress ?? '');

export const FULL_USER_X_SORTER = (
  a: DeepReadOnly<user_x_management.IFullUserXDetails | null | undefined>,
  b: DeepReadOnly<user_x_management.IFullUserXDetails | null | undefined>
) => USER_X_SORTER(a?.userX, b?.userX);
