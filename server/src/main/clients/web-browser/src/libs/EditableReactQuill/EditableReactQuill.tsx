import {isHtmlEmpty} from '../misc';
import ReactQuill from 'react-quill';
import {CSSProperties, ReactNode, useEffect, useRef} from 'react';

export function EditableReactQuill(props: {
  value: string | undefined | null;
  onChange?: (value: string) => void;

  editing?: boolean;
  editingStyle?: CSSProperties;
  onBlur?: () => void;

  readOnlyStyle?: CSSProperties;

  placeholder?: ReactNode;
  className?: string;
}) {
  const editingRef = useRef<ReactQuill>(null);

  useEffect(() => {
    if (props.editing) {
      editingRef.current?.focus();
    }
  }, [props.editing]);

  return (
    <>
      <div style={{display: props.editing ? undefined : 'none'}}>
        <ReactQuill
          ref={editingRef}
          theme="snow"
          className={'global-react-quill ' + props.className}
          value={props.value ?? ''}
          onChange={value => {
            if (props.editing) {
              props.onChange?.(value);
            }
          }}
          onBlur={props.onBlur}
          preserveWhitespace={true}
          style={{
            display: props.editing ? undefined : 'none',
            ...props.editingStyle,
          }}
        />
      </div>
      <ReactQuill
        theme="snow"
        className={
          'global-react-quill global-react-quill-read-only ' + props.className
        }
        value={props.value ?? ''}
        preserveWhitespace={true}
        modules={{toolbar: false}}
        readOnly={true}
        style={{
          display:
            !props.editing && !isHtmlEmpty(props.value) ? undefined : 'none',
          ...props.readOnlyStyle,
        }}
      />
      {!props.editing && isHtmlEmpty(props.value) && props.placeholder}
    </>
  );
}
