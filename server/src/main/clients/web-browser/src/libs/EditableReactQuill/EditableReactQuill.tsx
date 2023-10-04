import {isHtmlEmpty} from '../misc';
import ReactQuill from 'react-quill';
import {CSSProperties, ReactNode, useEffect, useRef} from 'react';

export function EditableReactQuill(props: {
  value: string | undefined | null;
  onChange?: (value: string) => void;
  onClick?: (event: MouseEvent) => void;

  editing?: boolean;
  editingStyle?: CSSProperties;
  onBlur?: () => void;

  readOnlyStyle?: CSSProperties;

  placeholder?: ReactNode;
  className?: string;
}) {
  const quillRef = useRef<ReactQuill>(null);
  const divRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (props.editing) {
      quillRef.current?.focus();
    }
  }, [props.editing]);

  function handleClick(event: MouseEvent) {
    props.onClick?.(event);
  }

  function handleFocusOut(event: FocusEvent) {
    let targetReceivingFocus = event.relatedTarget;
    while (
      targetReceivingFocus != null &&
      targetReceivingFocus instanceof HTMLElement
    ) {
      if (targetReceivingFocus === divRef.current) {
        return;
      }
      targetReceivingFocus = targetReceivingFocus.parentElement;
    }
    props.onBlur?.();
  }

  function handleKeyDown(event: KeyboardEvent) {
    // 'keypress' is not fired for the Escape key.
    if (event.key === 'Escape') {
      props.onBlur?.();
    }
  }

  useEffect(() => {
    if (divRef.current != null) {
      const lastDivRef = divRef.current;
      lastDivRef.addEventListener('click', handleClick);
      lastDivRef.addEventListener('focusout', handleFocusOut);
      lastDivRef.addEventListener('keydown', handleKeyDown);
      return () => {
        lastDivRef.removeEventListener('click', handleClick);
        lastDivRef.removeEventListener('focusout', handleFocusOut);
        lastDivRef.addEventListener('keydown', handleKeyDown);
      };
    }
    return;
  });

  return (
    <>
      <div ref={divRef}>
        {props.editing ? (
          <ReactQuill
            key="Editing"
            ref={quillRef}
            theme="snow"
            className={'global-react-quill ' + props.className}
            value={props.value ?? ''}
            preserveWhitespace={true}
            onChange={value => {
              props.onChange?.(value);
            }}
            style={{...(props.editingStyle ?? {})}}
          />
        ) : isHtmlEmpty(props.value) ? (
          props.placeholder ?? <></>
        ) : (
          <ReactQuill
            key="ReadOnly"
            ref={quillRef}
            theme="snow"
            className={
              'global-react-quill global-react-quill-read-only ' +
              props.className
            }
            value={props.value ?? ''}
            preserveWhitespace={true}
            modules={{toolbar: false}}
            readOnly={true}
            style={{...(props.readOnlyStyle ?? {})}}
          />
        )}
      </div>
    </>
  );
}
