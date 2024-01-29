import '../global.scss';
import {DeepReadOnly, writableForProto} from '../misc';
import {DynamicLoadAutocomplete} from './DynamicLoadAutocomplete';
import {createService} from '../protos';
import {FormField} from '../form_utils/forms';
import {pl_types, school_management} from 'pl-pb';
import {CSSProperties} from 'react';
import IGetSchoolsRequest = school_management.IGetSchoolsRequest;
import ISchool = pl_types.ISchool;
import SchoolManagementService = school_management.SchoolManagementService;

export function DynamicSchoolAutocomplete<
  Multiple extends boolean | undefined = false
>(
  props: DeepReadOnly<{
    label: string;
    baseRequest: IGetSchoolsRequest;
    schoolField: FormField<DeepReadOnly<ISchool>, Multiple>;
    renderTagStyle?: (
      option?: DeepReadOnly<ISchool> | null | undefined
    ) => CSSProperties | undefined;
  }>
) {
  function loadMoreOptions(page: number, pageSize: number, searchText: string) {
    return createService(SchoolManagementService, 'SchoolManagementService')
      .getSchools(
        writableForProto(
          Object.assign({}, props.baseRequest, {
            page,
            pageSize,
            searchText,
          } as IGetSchoolsRequest)
        )
      )
      .then(response => response.schools ?? []);
  }

  return (
    <DynamicLoadAutocomplete
      formField={props.schoolField}
      label={props.label}
      placeholder="Search by School Name"
      getId={option => option.id ?? 0}
      getOptionLabel={option => option.name ?? 'Unnamed School'}
      loadMoreOptions={loadMoreOptions}
      renderTagStyle={props.renderTagStyle}
      context={props.baseRequest}
    />
  );
}
