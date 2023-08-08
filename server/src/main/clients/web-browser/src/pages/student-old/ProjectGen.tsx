import React from 'react';
import Venn, {stringSet} from './utils/Venn';
import generateCompletion from './utils/Completion';

function App() {
  const style: React.CSSProperties = {
    display: 'flex',
    flexFlow: 'column nowrap',
    justifyContent: 'center',
    alignItems: 'center',
  };

  //eslint-disable-next-line @typescript-eslint/no-unused-vars
  const processInput = async (input: stringSet): Promise<stringSet> => {
    const completion = await generateCompletion(input);
    return completion;
  };

  return (
    <main style={style}>
      <Venn
        processInput={processInput}
        options={[
          ['Video Games', 'Sports', 'Music', 'Electronics'],
          [
            'EKS 1 - Linear Functions',
            'EKS 2 - Exponential Functions',
            'EKS 3 - Logarithmic Functions',
            'EKS 4 - Rational Functions',
          ],
          ['Computer Engineer', 'Data Scientist', 'Computer Programmer'],
          [
            'Data specialists pro sports',
            'Programming',
            'Storytelling with data',
          ],
        ]}
        pageURI="/student/project-implement"
      />
    </main>
  );
}

export default App;
