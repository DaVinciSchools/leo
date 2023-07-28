import './DemoProjectBuilder.scss';

import {pl_types} from '../../../generated/protobuf-js';
import {ProjectBuilder} from '../../../libs/ProjectBuilder/ProjectBuilder';
import {useEffect, useState} from 'react';

import ValueType = pl_types.ProjectInputCategory.ValueType;

const exampleCategories: pl_types.IProjectInputValue[] = [
  {
    id: 54321,
    category: {
      id: 12345,
      name: 'Assignment',
      placeholder: 'Assignment Topics',
      shortDescr: 'Enter assignment topics for the project:',
      hint: 'Click to add assignment topics.',
      maxNumValues: 2,
      valueType: ValueType.FREE_TEXT,
      options: [
        {id: 1, name: 'Art'},
        {id: 2, name: 'Physics'},
        {id: 3, name: 'English'},
      ],
    },
    freeTexts: ['Chemistry'],
  },
  {
    id: 98765,
    category: {
      id: 56789,
      name: "EKS'",
      placeholder: 'EKS`',
      shortDescr: "Select EKS' for the project:",
      hint: "Click to add EKS'.",
      maxNumValues: 2,
      valueType: ValueType.EKS,
      options: [
        {id: 1, name: 'EKS 1', shortDescr: 'EKS 1 Short Descr'},
        {id: 2, name: 'EKS 2', shortDescr: 'EKS 2 Short Descr'},
        {id: 3, name: 'EKS 3', shortDescr: 'EKS 3 Short Descr'},
      ],
    },
    selectedIds: [3],
  },
];

export function DemoProjectBuilder() {
  const [activeCategories, setActiveCategories] =
    useState<pl_types.IProjectInputValue[]>(exampleCategories);

  useEffect(() => {
    setActiveCategories(exampleCategories);
  }, []);

  return (
    <>
      <ProjectBuilder
        demo={true}
        noCategoriesText={'Select categories on the right'}
        categories={activeCategories}
        detailsAugment={<></>}
      />
    </>
  );
}
