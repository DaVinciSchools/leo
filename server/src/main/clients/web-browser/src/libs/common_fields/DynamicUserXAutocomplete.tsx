import '../global.scss';
import {user_x_management} from 'pl-pb';
import {Checkbox} from '@mui/material';
import {DeepReadOnly, toProto} from '../misc';
import {CSSProperties} from 'react';
import {DynamicLoadAutocomplete} from './DynamicLoadAutocomplete';
import {createService} from '../protos';
import {UserXAvatar} from '../UserXAvatar/UserXAvatar';
import IFullUserXDetails = user_x_management.IFullUserXDetails;
import IGetUserXsRequest = user_x_management.IGetUserXsRequest;
import UserXManagementService = user_x_management.UserXManagementService;

export function DynamicUserXAutocomplete<
  Multiple extends boolean | undefined = false,
  TMultiple = Multiple extends true
    ? IFullUserXDetails[]
    : IFullUserXDetails | null
>(
  props: DeepReadOnly<{
    multiple?: Multiple;
    label: string;
    baseRequest: IGetUserXsRequest;
    value: TMultiple;
    onChange: (userXs: DeepReadOnly<TMultiple>) => void;
    renderTagStyle?: (option: DeepReadOnly<IFullUserXDetails>) => CSSProperties;
  }>
) {
  function loadMoreUserXs(page: number, pageSize: number, searchText: string) {
    return createService(UserXManagementService, 'UserXManagementService')
      .getUserXs(
        toProto(
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
    <DynamicLoadAutocomplete<IFullUserXDetails, Multiple, TMultiple>
      multiple={props.multiple}
      label={props.label}
      placeholder="Search by Name or Email Address"
      getId={userX => userX.userX?.id ?? 0}
      value={props.value}
      onChange={props.onChange}
      loadMoreOptions={loadMoreUserXs}
      getOptionLabel={option => {
        const first = option.userX?.firstName ?? '';
        const last = option.userX?.lastName ?? '';
        const email = option.userX?.emailAddress ?? '';
        return `${last}, ${first} ${email}`;
      }}
      renderOption={(params, option, {selected}) => (
        <li
          {...params}
          key={option.userX?.id ?? 0}
          className="global-flex-row"
          style={{alignItems: 'center', cursor: 'pointer'}}
        >
          {props.multiple && (
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
        props.multiple
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
    />
  );
}
