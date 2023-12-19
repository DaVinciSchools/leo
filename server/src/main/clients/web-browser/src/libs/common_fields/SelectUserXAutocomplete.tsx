import '../global.scss';
import {user_x_management} from 'pl-pb';
import {Autocomplete, Checkbox, TextField} from '@mui/material';
import {DeepReadOnly} from '../misc';
import {useContext, useEffect, useRef, useState} from 'react';
import {createService} from '../protos';
import {GlobalStateContext} from '../GlobalState';
import IFullUserXDetails = user_x_management.IFullUserXDetails;
import IGetUserXsRequest = user_x_management.IGetUserXsRequest;
import UserXManagementService = user_x_management.UserXManagementService;

const PAGE_SIZE = 10;

export function SelectUserXAutocomplete(
  props: DeepReadOnly<{
    label: string;
    baseRequest: DeepReadOnly<IGetUserXsRequest>;
    onSelect: (userXs: DeepReadOnly<IFullUserXDetails[]>) => void;
  }>
) {
  const global = useContext(GlobalStateContext);

  const allPagedUserXs = useRef<DeepReadOnly<IFullUserXDetails>[]>([]);
  const [searchText, setSearchText] = useState('');
  const [allUserXOptions, setAllUserXOptions] = useState<
    DeepReadOnly<IFullUserXDetails[]>
  >([]);
  const [selectedUserXs, setSelectedUserXs] = useState<
    DeepReadOnly<IFullUserXDetails>[]
  >([]);
  const [noMoreResults, setNoMoreResults] = useState(false);

  useEffect(() => {
    allPagedUserXs.current = [];
    loadMoreUserXs();
  }, [searchText]);

  function loadMoreUserXs() {
    if (noMoreResults) {
      return;
    }
    const page = allPagedUserXs.current.length / PAGE_SIZE;
    createService(UserXManagementService, 'UserXManagementService')
      .getUserXs(
        Object.assign({}, props.baseRequest, {
          firstLastEmailSearchText: searchText,
          page,
          pageSize: PAGE_SIZE,
        } as IGetUserXsRequest)
      )
      .then(response => {
        allPagedUserXs.current.splice(
          page * PAGE_SIZE,
          PAGE_SIZE,
          ...response.userXs
        );
        setAllUserXOptions(allPagedUserXs.current.slice());
        if (response.userXs?.length < PAGE_SIZE) {
          setNoMoreResults(true);
        }
      })
      .catch(global.setError);
  }

  return (
    <Autocomplete
      multiple
      disableClearable={false}
      freeSolo={false}
      autoHighlight
      disableCloseOnSelect
      openOnFocus={true}
      options={allUserXOptions}
      isOptionEqualToValue={(option, value) =>
        option?.userX?.id === value?.userX?.id
      }
      size="small"
      value={selectedUserXs}
      onChange={(_, values) => {
        setSelectedUserXs(values);
        props.onSelect(values);
      }}
      onInputChange={(_, value) => setSearchText(value)}
      inputValue={searchText}
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
      getOptionLabel={option => {
        if (option != null) {
          const first = option.userX?.firstName ?? '';
          const last = option.userX?.lastName ?? '';
          const email = option.userX?.emailAddress ?? '';
          return `${last}, ${first} ${email}`;
        }
        return '';
      }}
      renderInput={params => (
        <TextField
          {...params}
          label={props.label}
          placeholder="Search by Name or Email Address"
        />
      )}
      ListboxProps={{
        onScroll: e => {
          const listboxNode = e.currentTarget;
          if (
            listboxNode.scrollTop + listboxNode.clientHeight >=
            listboxNode.scrollHeight
          ) {
            loadMoreUserXs();
          }
        },
      }}
      renderTags={() => ''}
    />
  );
}
