import {useContext, useEffect, useState} from 'react';
import {GlobalStateContext} from '../../../libs/GlobalState';
import {
  class_management_service,
  pl_types,
} from '../../../generated/protobuf-js';
import IClassX = pl_types.IClassX;
import {createService} from '../../../libs/protos';
import ClassManagementService = class_management_service.ClassManagementService;
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';

export function TeacherEditClasses() {
  const global = useContext(GlobalStateContext);

  const [classes, setClasses] = useState<IClassX[]>([]);

  useEffect(() => {
    if (global.user == null) {
      setClasses([]);
      return;
    }
    createService(ClassManagementService, 'ClassManagementService')
      .getClasses({teacherId: global.user.teacherId})
      .then(response => setClasses(response.classes))
      .catch(global.setError);
  }, [global.user]);

  if (!global.requireUser(user => user?.isTeacher || user?.isAdmin)) {
    return <></>;
  }

  return (
    <>
      <DefaultPage title="Edit Classes">{JSON.stringify(classes)}</DefaultPage>
    </>
  );
}
