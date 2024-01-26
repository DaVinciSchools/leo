import '../global.scss';
import {DeepReadOnly, writableForProto} from '../misc';
import {DynamicLoadAutocomplete} from './DynamicLoadAutocomplete';
import {createService} from '../protos';
import {FormField} from '../form_utils/forms';
import {assignment_management, pl_types} from 'pl-pb';
import {CSSProperties} from 'react';
import IAssignment = pl_types.IAssignment;
import AssignmentManagementService = assignment_management.AssignmentManagementService;
import IGetAssignmentsRequest = assignment_management.IGetAssignmentsRequest;

export function DynamicAssignmentAutocomplete<
  Multiple extends boolean | undefined = false
>(
  props: DeepReadOnly<{
    label: string;
    baseRequest: IGetAssignmentsRequest;
    assignmentField: FormField<DeepReadOnly<IAssignment>, Multiple>;
    renderTagStyle?: (option: DeepReadOnly<IAssignment>) => CSSProperties;
  }>
) {
  function loadMoreOptions(page: number, pageSize: number, searchText: string) {
    return createService(
      AssignmentManagementService,
      'AssignmentManagementService'
    )
      .getAssignments(
        writableForProto(
          Object.assign({}, props.baseRequest, {
            page,
            pageSize,
            searchText,
          } as IGetAssignmentsRequest)
        )
      )
      .then(response => response.assignments ?? []);
  }

  return (
    <DynamicLoadAutocomplete
      formField={props.assignmentField}
      label={props.label}
      placeholder="Search by Assignment Name"
      getId={option => option.id ?? 0}
      getOptionLabel={option => option.name ?? 'Unnamed Assignment'}
      loadMoreOptions={loadMoreOptions}
      renderTagStyle={props.renderTagStyle}
      context={props.baseRequest}
    />
  );
}
