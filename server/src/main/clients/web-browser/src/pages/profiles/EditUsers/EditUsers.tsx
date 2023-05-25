import './EditUser.scss';
import {
  createService,
  district_management,
  school_management,
  pl_types,
  user_management,
} from '../../../libs/protos';
import {useEffect, useState} from 'react';
import {FieldWithError} from '../../../FieldWithError/FieldWithError';
import {Display, SelectFromList} from '../../../SelectFromList/SelectFromList';
import {SelectDistrictFromList} from '../EditDistricts/EditDistricts';
import {SelectMultipleSchoolsFromList} from '../EditSchools/EditSchools';
import {MultipleDisplay} from '../../../SelectMultipleFromList/SelectMultipleFromList';
import UserManagementService = user_management.UserManagementService;
import IUser = pl_types.IUser;
import DistrictManagementService = district_management.DistrictManagementService;
import IDistrict = pl_types.IDistrict;
import IUserInformationResponse = user_management.IUserInformationResponse;
import ISchool = pl_types.ISchool;
import SchoolManagementService = school_management.SchoolManagementService;
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {getCurrentUser, sendToLogin} from '../../../libs/authentication';

export function SelectUserFromList(props: {
  id: string;
  display: Display;
  users: Map<number, IUser>;
  userXId: number;
  onSelect: (userXId: number) => void;
  defaultText: string;
}) {
  return SelectFromList<number, IUser>({
    id: props.id,
    display: props.display,
    values: props.users,
    selectedKey: props.userXId,
    getKey: user => (user != null ? user.id! : -1),
    stringToKey: Number,
    compareValues: (a, b) =>
      (a.lastName || '').localeCompare(b.lastName || '') ||
      (a.firstName || '').localeCompare(b.firstName || ''),
    onSelect: props.onSelect,
    renderValue: userXId => {
      const user = props.users.get(userXId);
      if (user != null) {
        return (
          <>
            <span className="user">
              <span className="last-name">{user.lastName}</span>,&nbsp;
              <span className="first-name">{user.firstName}</span>
            </span>
          </>
        );
      } else {
        return <>{props.defaultText}</>;
      }
    },
  });
}

export function EditUsers() {
  const user = getCurrentUser();
  if (user == null) {
    return sendToLogin();
  }

  const [districts, setDistricts] = useState(new Map<number, IDistrict>());
  const [districtId, setDistrictId] = useState(-1);
  const [schools, setSchools] = useState(new Map<number, ISchool>());

  const [users, setUsers] = useState(new Map<number, IUser>());
  const [userXId, setUserXId] = useState(-1);

  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [emailAddress, setEmailAddress] = useState('');
  const [password, setPassword] = useState('');
  const [verifyPassword, setVerifyPassword] = useState('');
  const [isAdmin, setIsAdmin] = useState(false);
  const [isTeacher, setIsTeacher] = useState(false);
  const [isStudent, setIsStudent] = useState(false);
  const [schoolIds, setSchoolIds] = useState(new Set<number>());

  const [firstNameError, setFirstNameError] = useState('');
  const [lastNameError, setLastNameError] = useState('');
  const [emailAddressError, setEmailAddressError] = useState('');
  const [passwordError, setPasswordError] = useState('');
  const [verifyPasswordError, setVerifyPasswordError] = useState('');

  const districtManagementService = createService(
    DistrictManagementService,
    'DistrictManagementService'
  );

  useEffect(() => {
    districtManagementService.getDistricts({}).then(response => {
      setDistricts(new Map(response.districts.map(v => [v.id!, v!])));
      setDistrictId(response.modifiedDistrictId!);
    });
  }, []);

  const userManagementService = createService(
    UserManagementService,
    'UserManagementService'
  );

  const schoolManagementService = createService(
    SchoolManagementService,
    'SchoolManagementService'
  );

  useEffect(() => {
    if (districtId !== -1) {
      schoolManagementService
        .getSchools({districtId: districtId})
        .then(response =>
          setSchools(new Map(response.schools.map(v => [v.id!, v])))
        );
      userManagementService
        .getUsers({districtId: districtId})
        .then(processUserInformationResponse);
    }
  }, [districtId]);

  function upsertUser() {
    userManagementService
      .upsertUser({
        user: {
          districtId: districtId,
          id: userXId !== -1 ? userXId : undefined,
          firstName: firstName,
          lastName: lastName,
          emailAddress: emailAddress,
          password: password,
          verifyPassword: verifyPassword,
          isAdmin: isAdmin,
          isTeacher: isTeacher,
          isStudent: isStudent,
        },
        schoolIds: [...schoolIds],
      })
      .then(processUserInformationResponse);
  }

  function removeUser() {
    userManagementService
      .removeUser({
        districtId: districtId,
        userXId: userXId,
      })
      .then(processUserInformationResponse);
  }

  function processUserInformationResponse(
    response: IUserInformationResponse
  ): void {
    if (response.success) {
      setDistrictId(response.districtId!);
      setUsers(new Map(response.users!.map(v => [v.id!, v])));
      loadUser(response.nextUserXId!);
    }
  }

  function loadUser(userXId: number) {
    userManagementService.getUserDetails({userXId: userXId}).then(response => {
      if (response.user != null) {
        setUserXId(userXId);
        setFirstName(response.user.firstName!);
        setLastName(response.user.lastName!);
        setEmailAddress(response.user.emailAddress!);
        setIsAdmin(response.user.isAdmin!);
        setIsTeacher(response.user.isTeacher!);
        setSchoolIds(
          response.user.isTeacher!
            ? new Set<number>(response.schoolIds)
            : new Set<number>()
        );
        setIsStudent(response.user.isStudent!);
      } else {
        setUserXId(-1);
        setFirstName('');
        setLastName('');
        setEmailAddress('');
        setIsAdmin(false);
        setIsTeacher(false);
        setIsStudent(false);
        setSchoolIds(new Set<number>());
      }
      setPassword('');
      setVerifyPassword('');

      setFirstNameError('');
      setLastNameError('');
      setEmailAddressError('');
      setPasswordError('');
      setVerifyPasswordError('');
    });
  }

  return (
    <>
      <DefaultPage title="Edit Users">
        <table className="form-table">
          <tbody>
            <tr>
              <th>District:</th>
              <td>
                <SelectDistrictFromList
                  id="districts"
                  display={Display.DROP_DOWN}
                  districts={districts}
                  districtId={districtId}
                  onSelect={setDistrictId}
                  defaultText="- Select District -"
                />
              </td>
            </tr>
            <tr hidden={districtId === -1}>
              <th>Users:</th>
              <td>
                <SelectUserFromList
                  id="users"
                  display={Display.RADIO_BUTTONS}
                  users={users}
                  userXId={userXId}
                  onSelect={loadUser}
                  defaultText="[Create New User]"
                />
              </td>
            </tr>
            <tr hidden={districtId === -1}>
              <th>
                <label htmlFor="first_name">First Name:</label>
              </th>
              <td>
                <FieldWithError
                  id="first_name"
                  value={firstName}
                  setValue={setFirstName}
                  autoComplete="given-name"
                  type="text"
                  maxLength={255}
                  error={firstNameError}
                />
              </td>
            </tr>
            <tr hidden={districtId === -1}>
              <th>
                <label htmlFor="last_name">Last Name:</label>
              </th>
              <td>
                <FieldWithError
                  id="last_name"
                  value={lastName}
                  setValue={setLastName}
                  autoComplete="family-name"
                  type="text"
                  maxLength={255}
                  error={lastNameError}
                />
              </td>
            </tr>
            <tr hidden={districtId === -1}>
              <th>
                <label htmlFor="email_address">Email Address:</label>
              </th>
              <td>
                <FieldWithError
                  id="email_address"
                  value={emailAddress}
                  setValue={setEmailAddress}
                  autoComplete="username"
                  type="email"
                  maxLength={254}
                  error={emailAddressError}
                />
              </td>
            </tr>
            <tr hidden={districtId === -1}>
              <th>
                <label htmlFor="password">Password:</label>
              </th>
              <td>
                <FieldWithError
                  id="password"
                  value={password}
                  setValue={setPassword}
                  autoComplete="new-password"
                  type="password"
                  maxLength={128}
                  error={passwordError}
                />
              </td>
            </tr>
            <tr hidden={districtId === -1}>
              <th>
                <label htmlFor="verify_password">Verify Password:</label>
              </th>
              <td>
                <FieldWithError
                  id="verify_password"
                  value={verifyPassword}
                  setValue={setVerifyPassword}
                  autoComplete="new-password"
                  type="password"
                  maxLength={128}
                  error={verifyPasswordError}
                />
              </td>
            </tr>
            <tr hidden={districtId === -1}>
              <th>
                <label htmlFor="is_admin">Admin:</label>
              </th>
              <td>
                <input
                  id="is_admin"
                  type="checkbox"
                  checked={isAdmin}
                  onChange={() => setIsAdmin(!isAdmin)}
                  className="checkbox"
                />
              </td>
            </tr>
            <tr hidden={districtId === -1}>
              <th>
                <label htmlFor="is_teacher">Teacher:</label>
              </th>
              <td>
                <input
                  id="is_teacher"
                  type="checkbox"
                  checked={isTeacher}
                  onChange={() => setIsTeacher(!isTeacher)}
                  className="checkbox"
                />
              </td>
            </tr>
            <tr hidden={districtId === -1 || !isTeacher}>
              <th></th>
              <td>
                <table className="form-table" style={{width: '100%'}}>
                  <tbody>
                    <tr>
                      <th>
                        <label htmlFor="is_teacher_schools">Schools:</label>
                      </th>
                      <td>
                        <SelectMultipleSchoolsFromList
                          id="is_teacher_schools"
                          display={MultipleDisplay.CHECK_BOXES}
                          schools={schools}
                          schoolIds={schoolIds}
                          onSelect={setSchoolIds}
                        />
                      </td>
                    </tr>
                  </tbody>
                </table>
              </td>
            </tr>
            <tr hidden={districtId === -1}>
              <th>
                <label htmlFor="is_student">Student:</label>
              </th>
              <td>
                <input
                  id="is_student"
                  type="checkbox"
                  checked={isStudent}
                  onChange={() => setIsStudent(!isStudent)}
                  className="checkbox"
                />
              </td>
            </tr>
            <tr hidden={districtId === -1}>
              <th></th>
              <td className="form-buttons">
                <div hidden={userXId !== -1} onClick={upsertUser}>
                  Add
                </div>
                <div hidden={userXId === -1} onClick={upsertUser}>
                  Update
                </div>
                <div
                  className="delete-button"
                  hidden={userXId === -1}
                  onClick={removeUser}
                >
                  Delete
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </DefaultPage>
    </>
  );
}
