import {DeepReadOnly} from '../misc';
import React, {useEffect, useRef} from 'react';
import 'froala-editor/css/froala_editor.pkgd.min.css';
import 'froala-editor/css/froala_style.min.css';
import FroalaEditorComponent from 'react-froala-wysiwyg';
import FroalaEditor from 'react-froala-wysiwyg';

export interface HtmlEditorProps {
  id: string;
  value: string | null | undefined;
  // This will be sent periodically, when the editor is blurred, and before it is destroyed.
  onChange?: (value: string) => void;
  // This will be sent when the editor is created and only if alwaysShowEditor is false.
  startEditing?: () => void;
  // This will be sent when the editor is destroyed and only if alwaysShowEditor is false.
  finishEditing?: () => void;
  placeholder?: string;
  editingPlaceholder?: string;
  // This will not show the editor unless alwaysShowEditor is true.
  readOnly?: boolean;
  alwaysShowEditor?: boolean;
}

export function HtmlEditor(props: DeepReadOnly<HtmlEditorProps>) {
  const [requestEditing, setRequestEditing] = React.useState(false);
  const [currentlyEditing, setCurrentlyEditing] = React.useState<
    boolean | undefined
  >();
  const [haveRequestedFocus, setHaveRequestedFocus] = React.useState(false);
  const editor = useRef<FroalaEditor | null>(null);

  function possiblyShowEditor() {
    setRequestEditing(true);
  }

  function possiblyHideEditor() {
    setRequestEditing(false);
  }

  useEffect(() => {
    const newEditing = !props.readOnly && requestEditing;
    if (newEditing === currentlyEditing) {
      return;
    }

    if (!props.alwaysShowEditor && newEditing) {
      props.startEditing?.();
    }

    if (!newEditing && editor.current?.getEditor?.()?.html?.get != null) {
      const newValue = editor.current?.getEditor?.()?.html?.get?.();
      if (props.value !== newValue) {
        props.onChange?.(newValue ?? props.value ?? '');
      }
    }

    if (!props.alwaysShowEditor && !newEditing) {
      props.finishEditing?.();
    }

    setHaveRequestedFocus(!newEditing);
    setCurrentlyEditing(newEditing);
  }, [requestEditing, props.readOnly, props.alwaysShowEditor]);

  useEffect(() => {
    if (editor?.current?.getEditor?.()?.edit?.on) {
      if (props.readOnly) {
        editor?.current?.getEditor?.()?.edit?.off();
      } else {
        editor?.current?.getEditor?.()?.edit?.on();
      }
    }
  }, [currentlyEditing, props.readOnly]);

  useEffect(() => {
    if (currentlyEditing && !haveRequestedFocus) {
      setHaveRequestedFocus(true);
      editor.current?.getEditor?.()?.events?.focus?.();
    }
  }, [currentlyEditing, haveRequestedFocus]);

  return (
    <>
      <div
        style={{
          display:
            currentlyEditing || props.alwaysShowEditor ? undefined : 'none',
        }}
      >
        <FroalaEditorComponent
          ref={editor}
          model={props.value ?? ''}
          onModelChange={(value: string) => props.onChange?.(value)}
          config={{
            initOnClick: false,
            tabSpaces: 4,
            events: {
              blur: possiblyHideEditor,
              keydown: (e: JQuery.KeyDownEvent) => {
                if (e.key === 'Escape') {
                  possiblyHideEditor();
                }
              },
            },
            placeholderText: props.editingPlaceholder ?? 'Type here...',
          }}
        />
      </div>
      <div
        style={{
          display:
            !currentlyEditing && !props.alwaysShowEditor ? undefined : 'none',
        }}
        className="fr-view"
        dangerouslySetInnerHTML={{
          __html: props.value
            ? props.value
            : props.placeholder ??
              'No content.' + (props.readOnly ? '' : ' Click to edit.'),
        }}
        onClick={possiblyShowEditor}
      />
    </>
  );
}
