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

export const TEXT_SORTER = (a: string, b: string) => a.localeCompare(b);

export const OPTION_SORTER = (a: IOption, b: IOption) =>
  (a.category ?? '').localeCompare(b.category ?? '') ||
  (a.name ?? '').localeCompare(b.name ?? '') ||
  (a.shortDescr ?? '').localeCompare(b.shortDescr ?? '');

export const KNOWLEDGE_AND_SKILL_SORTER = (
  a: IKnowledgeAndSkill,
  b: IKnowledgeAndSkill
) =>
  (a.category ?? '').localeCompare(b.category ?? '') ||
  (a.name ?? '').localeCompare(b.name ?? '') ||
  (a.shortDescr ?? '').localeCompare(b.shortDescr ?? '');

export const CLASS_SORTER = (a: IClassX, b: IClassX) =>
  (a.name ?? '').localeCompare(b.name ?? '');

export const ASSIGNMENT_SORTER = (a: IAssignment, b: IAssignment) =>
  (a.name ?? '').localeCompare(b.name ?? '');

export const CLASS_THEN_ASSIGNMENT_SORTER = (a: IAssignment, b: IAssignment) =>
  CLASS_SORTER(a.classX ?? {}, b.classX ?? {}) || ASSIGNMENT_SORTER(a, b);

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

export const SCHOOL_SORTER = (a: ISchool, b: ISchool) =>
  (a.name ?? '').localeCompare(b.name ?? '') ||
  (a.address ?? '').localeCompare(b.address ?? '');

export const DISTRICT_SORTER = (a: IDistrict, b: IDistrict) =>
  (a.name ?? '').localeCompare(b.name ?? '');
