import {pl_types} from 'pl-pb';
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

export const TEXT_SORTER = (a: DeepReadOnly<string>, b: DeepReadOnly<string>) =>
  a.localeCompare(b);

export const OPTION_SORTER = (
  a: DeepReadOnly<IOption>,
  b: DeepReadOnly<IOption>
) =>
  (a.category ?? '').localeCompare(b.category ?? '') ||
  (a.name ?? '').localeCompare(b.name ?? '') ||
  (a.shortDescr ?? '').localeCompare(b.shortDescr ?? '');

export const KNOWLEDGE_AND_SKILL_SORTER = (
  a: DeepReadOnly<IKnowledgeAndSkill>,
  b: DeepReadOnly<IKnowledgeAndSkill>
) =>
  (a.type ?? Type.UNSET) - (b.type ?? Type.UNSET) ||
  (a.category ?? '').localeCompare(b.category ?? '') ||
  (a.name ?? '').localeCompare(b.name ?? '') ||
  (a.shortDescr ?? '').localeCompare(b.shortDescr ?? '');

export const DISTRICT_SORTER = (
  a: DeepReadOnly<IDistrict>,
  b: DeepReadOnly<IDistrict>
) => (a.name ?? '').localeCompare(b.name ?? '');

export const SCHOOL_SORTER = (
  a: DeepReadOnly<ISchool>,
  b: DeepReadOnly<ISchool>
) =>
  DISTRICT_SORTER(a.district ?? {}, b.district ?? {}) ||
  (a.name ?? '').localeCompare(b.name ?? '') ||
  (a.address ?? '').localeCompare(b.address ?? '');

export const CLASS_X_SORTER = (
  a: DeepReadOnly<IClassX>,
  b: DeepReadOnly<IClassX>
) =>
  SCHOOL_SORTER(a.school ?? {}, b.school ?? {}) ||
  (a.name ?? '').localeCompare(b.name ?? '');

export const ASSIGNMENT_SORTER = (
  a: DeepReadOnly<IAssignment>,
  b: DeepReadOnly<IAssignment>
) =>
  CLASS_X_SORTER(a.classX ?? {}, b.classX ?? {}) ||
  (a.name ?? '').localeCompare(b.name ?? '');

export const PROJECT_SORTER = (
  a: DeepReadOnly<IProject>,
  b: DeepReadOnly<IProject>
) =>
  (a.name ?? '').localeCompare(b.name ?? '') ||
  (a.shortDescr ?? '').localeCompare(b.shortDescr ?? '');

export const PROJECT_POST_COMMENT_SORTER = (
  a: DeepReadOnly<pl_types.IProjectPostComment>,
  b: DeepReadOnly<pl_types.IProjectPostComment>
) => toLong(b.postTimeMs ?? 0).compare(toLong(a.postTimeMs ?? 0));

export const REVERSE_DATE_THEN_PROJECT_SORTER = (
  a: DeepReadOnly<IProject>,
  b: DeepReadOnly<IProject>
) =>
  (b.id ?? 0) - (a.id ?? 0) ||
  (a.name ?? '').localeCompare(b.name ?? '') ||
  (a.shortDescr ?? '').localeCompare(b.shortDescr ?? '');

export const PROJECT_DEFINITION_SORTER = (
  a: DeepReadOnly<IProjectDefinition>,
  b: DeepReadOnly<IProjectDefinition>
) =>
  (b.state === State.FAILED ? -1 : 1) - (b.state === State.FAILED ? -1 : 1) ||
  (b.state === State.PROCESSING ? -1 : 1) -
    (b.state === State.PROCESSING ? -1 : 1) ||
  (b.template === true ? 1 : -1) - (a.template === true ? 1 : -1) ||
  (a.name ?? '').localeCompare(b.name ?? '');

export const TAG_SORTER = (a: DeepReadOnly<ITag>, b: DeepReadOnly<ITag>) =>
  (a.userXId ?? 0) - (b.userXId ?? 0) ||
  (a.text ?? '').localeCompare(b.text ?? '');

export const USER_X_SORTER = (
  a: DeepReadOnly<pl_types.IUserX>,
  b: DeepReadOnly<pl_types.IUserX>
) =>
  (a.lastName ?? '').localeCompare(b.lastName ?? '') ||
  (a.firstName ?? '').localeCompare(b.firstName ?? '');
