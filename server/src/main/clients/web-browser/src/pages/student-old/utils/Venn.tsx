import React, {useState} from 'react';
import {createSearchParams, Link} from 'react-router-dom';
import './Venn.scss';

type stringSet = readonly [string, string, string, string];

function Dropdown({
  options,
  value,
  setValue,
  setFocused,
}: {
  options: string[];
  value: string;
  setValue: (value: string) => void;
  setFocused: (focused: boolean) => void;
}) {
  const handleInput = (event: React.ChangeEvent<HTMLInputElement>) => {
    setValue(event.target.value);
  };

  return (
    <div className="dropdown">
      <input
        value={value}
        onChange={handleInput}
        onFocus={() => setFocused(true)}
        onBlur={() => setFocused(false)}
      />
      <div className="dropdown-option-list">
        {options
          .filter(e => e.includes(value))
          .map((e, i) => (
            <div
              key={i}
              className="dropdown-option"
              onMouseDown={() => setValue(e)}
            >
              {e}
            </div>
          ))}
      </div>
    </div>
  );
}

function Circle({
  input,
  setInput,
  output,
  options,
  pageURI,
}: {
  input: string;
  setInput: (input: string) => void;
  output: string;
  options: string[];
  pageURI: string;
}) {
  const [focused, setFocused] = useState(false);

  return (
    <div className={focused ? 'circle focused' : 'circle'}>
      <Dropdown
        options={options}
        value={input}
        setValue={setInput}
        setFocused={setFocused}
      />
      <div className="output">
        <Link
          to={{
            pathname: pageURI,
            search: createSearchParams({description: output}).toString(),
          }}
        >
          {output}
        </Link>
      </div>
    </div>
  );
}

function Venn({
  options,
  processInput,
  pageURI,
}: {
  options: [string[], string[], string[], string[]];
  processInput:
    | ((input: stringSet) => stringSet)
    | ((input: stringSet) => Promise<stringSet>);
  pageURI: string;
}) {
  const [activated, setActivated] = useState(false);
  const [input1, setInput1] = useState('');
  const [input2, setInput2] = useState('');
  const [input3, setInput3] = useState('');
  const [input4, setInput4] = useState('');
  const [output, setOutput] = useState<stringSet>(['', '', '', '']);

  const handleClick = () => {
    if (activated) return;

    setActivated(true);
    const result = processInput([input1, input2, input3, input4]);
    if (result instanceof Promise) {
      result.then((output: stringSet) => {
        setOutput(output);
      });
    } else {
      setOutput(result);
    }
  };

  return (
    <div className={activated ? 'venn activated' : 'venn'}>
      <div className="circles">
        <Circle
          input={input1}
          setInput={setInput1}
          output={output[0]}
          options={options[0]}
          pageURI={pageURI}
        />
        <Circle
          input={input2}
          setInput={setInput2}
          output={output[1]}
          options={options[1]}
          pageURI={pageURI}
        />
        <Circle
          input={input3}
          setInput={setInput3}
          output={output[2]}
          options={options[2]}
          pageURI={pageURI}
        />
        <Circle
          input={input4}
          setInput={setInput4}
          output={output[3]}
          options={options[3]}
          pageURI={pageURI}
        />
      </div>
      <div className="button" onClick={handleClick}>
        Generate
      </div>
    </div>
  );
}

export default Venn;
export type {stringSet};
