import '../global.scss';

import ReactQuill, {ReactQuillProps} from 'react-quill';
import {CSSProperties, forwardRef, Ref} from 'react';

export const EditableReactQuill = forwardRef(
  (
    props: ReactQuillProps & {
      editing?: boolean;

      editingStyle?: CSSProperties;
      readOnlyStyle?: CSSProperties;
    },
    ref: Ref<ReactQuill>
  ) => {
    return (
      <>
        <div
          style={{
            display: props.editing !== true ? undefined : 'none',
          }}
        >
          <ReactQuill
            {...props}
            ref={props.editing !== true ? ref : undefined}
            theme="snow"
            className={
              'global-react-quill global-react-quill-read-only ' +
              props.className
            }
            preserveWhitespace={true}
            modules={{
              toolbar: false,
            }}
            readOnly={true}
            style={props.readOnlyStyle}
          />
        </div>
        <div
          style={{
            display: props.editing !== true ? 'none' : undefined,
          }}
        >
          <ReactQuill
            {...props}
            ref={props.editing !== true ? undefined : ref}
            theme="snow"
            className={'global-react-quill ' + props.className}
            preserveWhitespace={true}
            modules={{
              toolbar: [
                ['bold', 'italic', 'underline', 'strike'], // toggled buttons
                ['blockquote', 'code-block'],
                [{header: [1, 2, 3, 4, 5, 6, false]}],
                [{list: 'ordered'}, {list: 'bullet'}],
                [{script: 'sub'}, {script: 'super'}], // superscript/subscript
                [{indent: '-1'}, {indent: '+1'}], // outdent/indent
                // Errors: [{direction: 'rtl'}], // text direction
                [{size: ['small', false, 'large', 'huge']}], // custom dropdown
                // Errors: [{color: []}, {background: []}], // dropdown with defaults from theme
                [{font: []}],
                [{align: []}],
                ['link'],
                ['clean'], // remove formatting button
                ['video'],
              ],
            }}
            readOnly={false}
            style={props.editingStyle}
          />
        </div>
      </>
    );
  }
);
