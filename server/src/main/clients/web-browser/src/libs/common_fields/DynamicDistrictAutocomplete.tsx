import '../global.scss';
import {DeepReadOnly, writableForProto} from '../misc';
import {DynamicLoadAutocomplete} from './DynamicLoadAutocomplete';
import {createService} from '../protos';
import {FormField} from '../form_utils/forms';
import {district_management, pl_types} from 'pl-pb';
import {CSSProperties} from 'react';
import IGetDistrictsRequest = district_management.IGetDistrictsRequest;
import IDistrict = pl_types.IDistrict;
import DistrictManagementService = district_management.DistrictManagementService;

export function DynamicDistrictAutocomplete<
  Multiple extends boolean | undefined = false
>(
  props: DeepReadOnly<{
    label: string;
    districtField: FormField<DeepReadOnly<IDistrict>, Multiple>;
    renderTagStyle?: (
      option?: DeepReadOnly<IDistrict> | null | undefined
    ) => CSSProperties | undefined;
  }>
) {
  function loadMoreOptions(page: number, pageSize: number, searchText: string) {
    return createService(DistrictManagementService, 'DistrictManagementService')
      .getDistricts(
        writableForProto(
          Object.assign({}, {
            page,
            pageSize,
            searchText,
          } as IGetDistrictsRequest)
        )
      )
      .then(response => response.districts ?? []);
  }

  return (
    <DynamicLoadAutocomplete
      formField={props.districtField}
      label={props.label}
      placeholder="Search by District Name"
      getId={option => option.id ?? 0}
      getOptionLabel={option => option.name ?? 'Unnamed District'}
      loadMoreOptions={loadMoreOptions}
      renderTagStyle={props.renderTagStyle}
    />
  );
}
