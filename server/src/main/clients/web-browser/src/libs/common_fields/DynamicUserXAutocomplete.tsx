import '../global.scss';
import {user_x_management} from 'pl-pb';
import {Checkbox} from '@mui/material';
import {DeepReadOnly, writableForProto} from '../misc';
import {CSSProperties} from 'react';
import {DynamicLoadAutocomplete} from './DynamicLoadAutocomplete';
import {createService} from '../protos';
import {UserXAvatar} from '../UserXAvatar/UserXAvatar';
import {FormField} from '../form_utils/forms';
import IFullUserXDetails = user_x_management.IFullUserXDetails;
import IGetUserXsRequest = user_x_management.IGetUserXsRequest;
import UserXManagementService = user_x_management.UserXManagementService;

export function DynamicUserXAutocomplete<
  Multiple extends boolean | undefined = false
>(
  props: DeepReadOnly<{
    label: string;
    baseRequest: IGetUserXsRequest;
    userXField: FormField<DeepReadOnly<IFullUserXDetails>, Multiple>;
    renderTagStyle?: (
      option?: DeepReadOnly<IFullUserXDetails> | null | undefined
    ) => CSSProperties | undefined;
  }>
) {
  function loadMoreOptions(page: number, pageSize: number, searchText: string) {
    return createService(UserXManagementService, 'UserXManagementService')
      .getUserXs(
        writableForProto(
          Object.assign({}, props.baseRequest, {
            page,
            pageSize,
            firstLastEmailSearchText: searchText,
          } as IGetUserXsRequest)
        )
      )
      .then(response => response.userXs ?? []);
  }

  return (
    <DynamicLoadAutocomplete<IFullUserXDetails, Multiple>
      formField={props.userXField}
      label={props.label}
      placeholder="Search by Name or Email Address"
      getId={option => option.userX?.id ?? 0}
      loadMoreOptions={loadMoreOptions}
      getOptionLabel={option => {
        const first = option.userX?.firstName ?? '';
        const last = option.userX?.lastName ?? '';
        const email = option.userX?.emailAddress ?? '';
        return `${last}, ${first} ${email}`;
      }}
      renderOption={(params, option, {selected}) => (
        <li
          {...params}
          className="global-flex-row"
          style={{alignItems: 'center', cursor: 'pointer'}}
        >
          {props.userXField.multiple && (
            <Checkbox style={{marginRight: -6}} checked={selected} />
          )}
          <UserXAvatar userX={option.userX} size="2em" />
          <span
            style={{
              textOverflow: 'ellipsis',
              whiteSpace: 'nowrap',
            }}
          >
            {option.userX?.lastName ?? ''}, {option.userX?.firstName ?? ''}{' '}
            <span style={{fontWeight: 'lighter', fontStyle: 'italic'}}>
              {option.userX?.emailAddress ?? ''}
            </span>
          </span>
        </li>
      )}
      renderTagLabel={
        props.userXField.multiple
          ? option => (
              <>
                {(option.userX?.lastName ?? '') +
                  ', ' +
                  (option.userX?.firstName ?? '')}
              </>
            )
          : undefined
      }
      renderTagStyle={props.renderTagStyle}
      context={props.baseRequest}
    />
  );
}
