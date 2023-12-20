import '../global.scss';
import {user_x_management} from 'pl-pb';
import {Checkbox, Chip} from '@mui/material';
import {DeepReadOnly, toProto} from '../misc';
import {ReactNode} from 'react';
import {AutocompleteRenderGetTagProps} from '@mui/material/Autocomplete/Autocomplete';
import {DynamicLoadAutocomplete} from './DynamicLoadAutocomplete';
import {createService} from '../protos';
import IFullUserXDetails = user_x_management.IFullUserXDetails;
import IGetUserXsRequest = user_x_management.IGetUserXsRequest;
import UserXManagementService = user_x_management.UserXManagementService;

export function SelectUserXsAutocomplete(
  props: DeepReadOnly<{
    label: string;
    baseRequest: DeepReadOnly<IGetUserXsRequest>;
    values: DeepReadOnly<IFullUserXDetails[]>;
    onChange: (userXs: DeepReadOnly<IFullUserXDetails[]>) => void;
    renderTags?: (
      value: DeepReadOnly<IFullUserXDetails[]>,
      getTagProps: AutocompleteRenderGetTagProps
    ) => ReactNode;
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
    <DynamicLoadAutocomplete<IFullUserXDetails>
      label={props.label}
      placeholder="Search by Name or Email Address"
      getId={userX => userX.userX?.id ?? 0}
      values={props.values}
      onChange={props.onChange}
      loadMoreOptions={loadMoreUserXs}
      getOptionLabel={option => {
        const first = option.userX?.firstName ?? '';
        const last = option.userX?.lastName ?? '';
        const email = option.userX?.emailAddress ?? '';
        return `${last}, ${first} ${email}`;
      }}
      renderOption={(params, option, {selected}) => (
        <li {...params} key={option.userX?.id ?? 0}>
          <Checkbox style={{marginRight: 8}} checked={selected} />
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
      renderTags={(options, getTagProps) => (
        <>
          {props.renderTags?.(options, getTagProps) ??
            options.map(option => (
              <Chip
                {...getTagProps}
                key={option.userX?.id ?? 0}
                label={`${option.userX?.lastName ?? ''}, ${
                  option.userX?.firstName ?? ''
                }`}
                size="small"
                onDelete={() => {
                  props.onChange(
                    props.values
                      .slice()
                      .filter(userX => userX?.userX?.id !== option?.userX?.id)
                  );
                }}
              />
            ))}
        </>
      )}
    />
  );
}
