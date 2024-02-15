import {user_x_management} from 'pl-pb';
import {useFormFields} from '../form_utils/forms';
import {
  Paper,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TextField,
} from '@mui/material';
import Table from '@mui/material/Table';
import {useContext, useEffect, useState} from 'react';
import {DeepReadOnly} from '../misc';
import {Person} from '@mui/icons-material';
import {createService} from '../protos';
import {GlobalStateContext} from '../GlobalStateProvider/GlobalStateProvider';
import IFullUserXDetails = user_x_management.IFullUserXDetails;
import UserXManagementService = user_x_management.UserXManagementService;
import IGetUserXsRequest = user_x_management.IGetUserXsRequest;

export function SearchForUserX(props: {
  onSelect: (userX?: DeepReadOnly<IFullUserXDetails>) => void;
  baseRequest: DeepReadOnly<IGetUserXsRequest>;
}) {
  const global = useContext(GlobalStateContext);

  const [userXs, setUserXs] = useState<
    readonly DeepReadOnly<IFullUserXDetails>[]
  >([]);

  const form = useFormFields();
  const searchText = form.useStringFormField('searchText', {
    startIcon: <Person />,
    maxLength: 255,
  });

  useEffect(() => {
    createService(UserXManagementService, 'UserXManagementService')
      .getUserXs(
        Object.assign({}, props.baseRequest, {
          firstLastEmailSearchText: searchText.getValue(),
        } as IGetUserXsRequest)
      )
      .then(response => {
        setUserXs(response.userXs);
      })
      .catch(global.setError);
  }, [searchText.getValue()]);

  return (
    <>
      <TextField
        {...searchText.getTextFieldParams({
          label: 'Search by Name or Email Address',
          autoFocus: true,
          style: {marginTop: '0.5rem', marginBottom: '1rem'},
        })}
      />
      <TableContainer component={Paper}>
        <Table size="small" aria-label="Select Individual">
          <TableHead>
            <TableRow>
              <TableCell style={{fontWeight: 'bold'}}>First Name</TableCell>
              <TableCell style={{fontWeight: 'bold'}}>Last Name</TableCell>
              <TableCell style={{fontWeight: 'bold'}}>Email Address</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {userXs.map(userX => (
              <TableRow
                key={userX.userX?.id ?? 0}
                style={{
                  cursor: 'pointer',
                }}
                onClick={() => {
                  props.onSelect(userX);
                }}
              >
                <TableCell>{userX.userX?.firstName ?? ''}</TableCell>
                <TableCell>{userX.userX?.lastName ?? ''}</TableCell>
                <TableCell>{userX.userX?.emailAddress ?? ''}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </>
  );
}
