import './EditSchools.scss';
import {ChangeEvent, useEffect, useState} from 'react';
import {
  createService,
  district_management,
  school_management,
  pl_types,
} from '../../../libs/protos';
import {Display, SelectFromList} from '../../../SelectFromList/SelectFromList';
import {SelectDistrictFromList} from '../EditDistricts/EditDistricts';
import DistrictManagementService = district_management.DistrictManagementService;
import IDistrict = pl_types.IDistrict;
import SchoolManagementService = school_management.SchoolManagementService;
import ISchool = pl_types.ISchool;
import ISchoolInformationResponse = school_management.ISchoolInformationResponse;
import {
  MultipleDisplay,
  SelectMultipleFromList,
} from '../../../SelectMultipleFromList/SelectMultipleFromList';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {getCurrentUser, sendToLogin} from '../../../libs/authentication';
import {
  HandleError,
  HandleErrorType,
} from '../../../libs/HandleError/HandleError';

export function SelectSchoolFromList(props: {
  id: string;
  display: Display;
  schools: Map<number, ISchool>;
  schoolId: number;
  onSelect: (key: number) => void;
  defaultText: string;
}) {
  return SelectFromList<number, ISchool>({
    id: props.id,
    display: props.display,
    values: props.schools,
    selectedKey: props.schoolId,
    getKey: school => (school != null ? school.id! : -1),
    stringToKey: Number,
    compareValues: (a, b) =>
      (a.name || '').localeCompare(b.name || '') ||
      (a.address || '').localeCompare(b.address || ''),
    onSelect: props.onSelect,
    renderValue: schoolId => {
      const school = props.schools.get(schoolId);
      if (school != null) {
        return (
          <>
            <span className="school">
              <span className="school-name">{school.name}</span>,&nbsp;
              <span className="school-address">{school.address}</span>
            </span>
          </>
        );
      } else {
        return <>{props.defaultText}</>;
      }
    },
  });
}

export function SelectMultipleSchoolsFromList(props: {
  id: string;
  display: MultipleDisplay;
  schools: Map<number, ISchool>;
  schoolIds: Set<number>;
  onSelect: (keys: Set<number>) => void;
}) {
  return SelectMultipleFromList<number, ISchool>({
    id: props.id,
    display: props.display,
    values: props.schools,
    selectedKeys: props.schoolIds,
    getKey: school => (school != null ? school.id! : -1),
    stringToKey: Number,
    compareValues: (a, b) =>
      (a.name || '').localeCompare(b.name || '') ||
      (a.address || '').localeCompare(b.address || ''),
    onSelect: props.onSelect,
    renderValue: schoolId => {
      const school = props.schools.get(schoolId);
      if (school != null) {
        return (
          <>
            <span className="school">
              <span className="school-name">{school.name}</span>,&nbsp;
              <span className="school-address">{school.address}</span>
            </span>
          </>
        );
      } else {
        return <></>;
      }
    },
  });
}

export function EditSchools() {
  const [handleError, setHandleError] = useState<HandleErrorType>();
  const user = getCurrentUser();
  if (user == null) {
    return sendToLogin();
  }

  const [districts, setDistricts] = useState(new Map<number, IDistrict>());
  const [districtId, setDistrictId] = useState(-1);

  const [schools, setSchools] = useState(new Map<number, ISchool>());
  const [schoolId, setSchoolId] = useState(-1);
  const [name, setName] = useState('');
  const [address, setAddress] = useState('');

  const districtManagementService = createService(
    DistrictManagementService,
    'DistrictManagementService'
  );

  useEffect(() => {
    districtManagementService
      .getDistricts({})
      .then(response => {
        setDistricts(new Map(response.districts.map(v => [v.id!, v!])));
        setDistrictId(response.modifiedDistrictId!);
      })
      .catch(setHandleError);
  }, []);

  const schoolManagementService = createService(
    SchoolManagementService,
    'SchoolManagementService'
  );

  useEffect(() => {
    if (districtId !== -1) {
      schoolManagementService
        .getSchools({districtId: districtId})
        .then(processSchoolInformationResponse)
        .catch(setHandleError);
    }
  }, [districtId]);

  function upsertSchool() {
    schoolManagementService
      .upsertSchool({
        school: {
          districtId: districtId,
          id: schoolId !== -1 ? schoolId : undefined,
          name: name,
          address: address,
        },
      })
      .then(processSchoolInformationResponse)
      .catch(setHandleError);
  }

  function removeSchool() {
    schoolManagementService
      .removeSchool({
        districtId: districtId,
        schoolId: schoolId,
      })
      .then(processSchoolInformationResponse)
      .catch(setHandleError);
  }

  function processSchoolInformationResponse(
    response: ISchoolInformationResponse
  ) {
    setDistrictId(response.districtId!);
    setSchools(new Map(response.schools!.map(v => [v.id!, v!])));
    setSchoolId(response.nextSchoolId!);
  }

  return (
    <>
      <HandleError error={handleError} setError={setHandleError} />
      <DefaultPage title="Edit Schools">
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
              <th>School:</th>
              <td>
                <SelectSchoolFromList
                  id="schools"
                  display={Display.RADIO_BUTTONS}
                  schools={schools}
                  schoolId={schoolId}
                  onSelect={schoolId => {
                    setSchoolId(schoolId);
                    const school = schools.get(schoolId);
                    if (school != null) {
                      setName(school.name!);
                      setAddress(school.address!);
                    } else {
                      setName('');
                      setAddress('');
                    }
                  }}
                  defaultText="[Create New School]"
                />
              </td>
            </tr>
            <tr hidden={districtId === -1}>
              <th>Name:</th>
              <td>
                <input
                  type="text"
                  placeholder="New School Name"
                  onChange={(e: ChangeEvent<HTMLInputElement>) => {
                    setName(e.target.value);
                  }}
                  value={name}
                />
              </td>
            </tr>
            <tr hidden={districtId === -1}>
              <th>Address:</th>
              <td>
                <input
                  type="text"
                  placeholder="New School Address"
                  onChange={(e: ChangeEvent<HTMLInputElement>) => {
                    setAddress(e.target.value);
                  }}
                  value={address}
                />
              </td>
            </tr>
            <tr hidden={districtId === -1}>
              <th></th>
              <td className="form-buttons">
                <div hidden={schoolId !== -1} onClick={upsertSchool}>
                  Add
                </div>
                <div hidden={schoolId === -1} onClick={upsertSchool}>
                  Update
                </div>
                <div
                  className="delete-button"
                  hidden={schoolId === -1}
                  onClick={removeSchool}
                >
                  Delete
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </DefaultPage>{' '}
    </>
  );
}
