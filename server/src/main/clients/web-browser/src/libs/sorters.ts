import {pl_types} from '../generated/protobuf-js';
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

export const TEXT_SORTER = (a: string, b: string) => a.localeCompare(b);

export const OPTION_SORTER = (a: IOption, b: IOption) =>
  (a.category ?? '').localeCompare(b.category ?? '') ||
  (a.name ?? '').localeCompare(b.name ?? '') ||
  (a.shortDescr ?? '').localeCompare(b.shortDescr ?? '');

export const KNOWLEDGE_AND_SKILL_SORTER = (
  a: IKnowledgeAndSkill,
  b: IKnowledgeAndSkill
) =>
  (a.type ?? Type.UNSET) - (b.type ?? Type.UNSET) ||
  (a.category ?? '').localeCompare(b.category ?? '') ||
  (a.name ?? '').localeCompare(b.name ?? '') ||
  (a.shortDescr ?? '').localeCompare(b.shortDescr ?? '');

export const DISTRICT_SORTER = (a: IDistrict, b: IDistrict) =>
  (a.name ?? '').localeCompare(b.name ?? '');

export const SCHOOL_SORTER = (a: ISchool, b: ISchool) =>
  DISTRICT_SORTER(a.district ?? {}, b.district ?? {}) ||
  (a.name ?? '').localeCompare(b.name ?? '') ||
  (a.address ?? '').localeCompare(b.address ?? '');

export const CLASS_X_SORTER = (a: IClassX, b: IClassX) =>
  SCHOOL_SORTER(a.school ?? {}, b.school ?? {}) ||
  (a.name ?? '').localeCompare(b.name ?? '');

export const ASSIGNMENT_SORTER = (a: IAssignment, b: IAssignment) =>
  CLASS_X_SORTER(a.classX ?? {}, b.classX ?? {}) ||
  (a.name ?? '').localeCompare(b.name ?? '');

export const PROJECT_SORTER = (a: IProject, b: IProject) =>
  (a.name ?? '').localeCompare(b.name ?? '') ||
  (a.shortDescr ?? '').localeCompare(b.shortDescr ?? '');

export const REVERSE_DATE_THEN_PROJECT_SORTER = (a: IProject, b: IProject) =>
  (b.id ?? 0) - (a.id ?? 0) ||
  (a.name ?? '').localeCompare(b.name ?? '') ||
  (a.shortDescr ?? '').localeCompare(b.shortDescr ?? '');

export const PROJECT_DEFINITION_SORTER = (
  a: IProjectDefinition,
  b: IProjectDefinition
) =>
  (b.state === State.FAILED ? -1 : 1) - (b.state === State.FAILED ? -1 : 1) ||
  (b.state === State.PROCESSING ? -1 : 1) -
    (b.state === State.PROCESSING ? -1 : 1) ||
  (b.template === true ? 1 : -1) - (a.template === true ? 1 : -1) ||
  (a.name ?? '').localeCompare(b.name ?? '');

export const TAG_SORTER = (a: ITag, b: ITag) =>
  (a.userXId ?? 0) - (b.userXId ?? 0) ||
  (a.text ?? '').localeCompare(b.text ?? '');

export const USER_X_SORTER = (a: pl_types.IUserX, b: pl_types.IUserX) =>
  (a.lastName ?? '').localeCompare(b.lastName ?? '') ||
  (a.firstName ?? '').localeCompare(b.firstName ?? '');
