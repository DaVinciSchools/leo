import '../global.scss';
import {DeepReadOnly, writableForProto} from '../misc';
import {DynamicLoadAutocomplete} from './DynamicLoadAutocomplete';
import {createService} from '../protos';
import {FormField} from '../form_utils/forms';
import {class_x_management_service, pl_types} from 'pl-pb';
import {CSSProperties} from 'react';
import IGetClassXsRequest = class_x_management_service.IGetClassXsRequest;
import IClassX = pl_types.IClassX;
import ClassXManagementService = class_x_management_service.ClassXManagementService;

export function DynamicClassXAutocomplete<
  Multiple extends boolean | undefined = false
>(
  props: DeepReadOnly<{
    label: string;
    baseRequest: IGetClassXsRequest;
    classXField: FormField<DeepReadOnly<IClassX>, Multiple>;
    renderTagStyle?: (
      option?: DeepReadOnly<IClassX> | null | undefined
    ) => CSSProperties | undefined;
  }>
) {
  function loadMoreOptions(page: number, pageSize: number, searchText: string) {
    return createService(ClassXManagementService, 'ClassXManagementService')
      .getClassXs(
        writableForProto(
          Object.assign({}, props.baseRequest, {
            page,
            pageSize,
            searchText,
          } as IGetClassXsRequest)
        )
      )
      .then(response => response.classXs ?? []);
  }

  return (
    <DynamicLoadAutocomplete
      formField={props.classXField}
      label={props.label}
      placeholder="Search by Class Name"
      getId={option => option.id ?? 0}
      getOptionLabel={option => option.name ?? 'Unnamed Class'}
      loadMoreOptions={loadMoreOptions}
      renderTagStyle={props.renderTagStyle}
      context={props.baseRequest}
    />
  );
}
