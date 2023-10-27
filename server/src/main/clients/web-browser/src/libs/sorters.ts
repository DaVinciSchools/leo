import {pl_types} from 'pl-pb';
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
import {DeepReadonly, toLong} from './misc';

export const TEXT_SORTER = (a: string, b: string) => a.localeCompare(b);

export const OPTION_SORTER = (
  a: DeepReadonly<IOption>,
  b: DeepReadonly<IOption>
) =>
  (a.category ?? '').localeCompare(b.category ?? '') ||
  (a.name ?? '').localeCompare(b.name ?? '') ||
  (a.shortDescr ?? '').localeCompare(b.shortDescr ?? '');

export const KNOWLEDGE_AND_SKILL_SORTER = (
  a: DeepReadonly<IKnowledgeAndSkill>,
  b: DeepReadonly<IKnowledgeAndSkill>
) =>
  (a.type ?? Type.UNSET) - (b.type ?? Type.UNSET) ||
  (a.category ?? '').localeCompare(b.category ?? '') ||
  (a.name ?? '').localeCompare(b.name ?? '') ||
  (a.shortDescr ?? '').localeCompare(b.shortDescr ?? '');

export const DISTRICT_SORTER = (
  a: DeepReadonly<IDistrict>,
  b: DeepReadonly<IDistrict>
) => (a.name ?? '').localeCompare(b.name ?? '');

export const SCHOOL_SORTER = (
  a: DeepReadonly<ISchool>,
  b: DeepReadonly<ISchool>
) =>
  DISTRICT_SORTER(a.district ?? {}, b.district ?? {}) ||
  (a.name ?? '').localeCompare(b.name ?? '') ||
  (a.address ?? '').localeCompare(b.address ?? '');

export const CLASS_X_SORTER = (
  a: DeepReadonly<IClassX>,
  b: DeepReadonly<IClassX>
) =>
  SCHOOL_SORTER(a.school ?? {}, b.school ?? {}) ||
  (a.name ?? '').localeCompare(b.name ?? '');

export const ASSIGNMENT_SORTER = (
  a: DeepReadonly<IAssignment>,
  b: DeepReadonly<IAssignment>
) =>
  CLASS_X_SORTER(a.classX ?? {}, b.classX ?? {}) ||
  (a.name ?? '').localeCompare(b.name ?? '');

export const PROJECT_SORTER = (
  a: DeepReadonly<IProject>,
  b: DeepReadonly<IProject>
) =>
  (a.name ?? '').localeCompare(b.name ?? '') ||
  (a.shortDescr ?? '').localeCompare(b.shortDescr ?? '');

export const PROJECT_POST_COMMENT_SORTER = (
  a: DeepReadonly<pl_types.IProjectPostComment>,
  b: DeepReadonly<pl_types.IProjectPostComment>
) => toLong(b.postTimeMs ?? 0).compare(toLong(a.postTimeMs ?? 0));

export const REVERSE_DATE_THEN_PROJECT_SORTER = (
  a: DeepReadonly<IProject>,
  b: DeepReadonly<IProject>
) =>
  (b.id ?? 0) - (a.id ?? 0) ||
  (a.name ?? '').localeCompare(b.name ?? '') ||
  (a.shortDescr ?? '').localeCompare(b.shortDescr ?? '');

export const PROJECT_DEFINITION_SORTER = (
  a: DeepReadonly<IProjectDefinition>,
  b: DeepReadonly<IProjectDefinition>
) =>
  (b.state === State.FAILED ? -1 : 1) - (b.state === State.FAILED ? -1 : 1) ||
  (b.state === State.PROCESSING ? -1 : 1) -
    (b.state === State.PROCESSING ? -1 : 1) ||
  (b.template === true ? 1 : -1) - (a.template === true ? 1 : -1) ||
  (a.name ?? '').localeCompare(b.name ?? '');

export const TAG_SORTER = (a: DeepReadonly<ITag>, b: DeepReadonly<ITag>) =>
  (a.userXId ?? 0) - (b.userXId ?? 0) ||
  (a.text ?? '').localeCompare(b.text ?? '');

export const USER_X_SORTER = (a: pl_types.IUserX, b: pl_types.IUserX) =>
  (a.lastName ?? '').localeCompare(b.lastName ?? '') ||
  (a.firstName ?? '').localeCompare(b.firstName ?? '');
