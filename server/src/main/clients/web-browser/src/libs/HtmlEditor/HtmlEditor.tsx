import {DeepReadOnly} from '../misc';
import React, {useEffect, useRef} from 'react';
import 'froala-editor/css/froala_editor.pkgd.min.css';
import 'froala-editor/css/froala_style.min.css';
import 'froala-editor/js/plugins.pkgd.min.js';
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

const TOOLBAR_BUTTONS = [
  'bold',
  'italic',
  'underline',
  'clearFormatting',
  '|',
  'paragraphStyle',
  'fontFamily',
  'fontSize',
  'color',
  '|',
  'formatOL',
  'formatUL',
  'indent',
  'outdent',
  '|',
  'insertLink',
  'insertImage',
  'insertTable',
  'insertVideo',
  'insertFile',
  '|',
  'undo',
  'redo',
];

const ALLOWED_FILE_TYPES = [
  'application/javascript',
  'application/json',
  'application/pdf',
  'image/gif',
  'image/jpeg',
  'image/png',
  'image/svg+xml',
  'image/vnd.microsoft.icon',
  'text/css',
  'text/csv',
  'text/html',
  'text/plain',
  'video/mp4',
  'video/mpeg',
  'video/ogg',
  'video/quicktime',
  'video/webm',
  'video/x-m4v',
  'video/x-msvideo',
];

// Image types source: https://developer.mozilla.org/en-US/docs/Web/Media/Formats/Image_types
const ALLOWED_IMAGE_TYPES = [
  // Mime type: 'image/apng',
  'apng',

  // Mime type: 'image/avif',
  'avif',

  // Mime type: 'image/gif',
  'gif',

  // Mime type: 'image/jpeg',
  'jfif',
  'jpeg',
  'jpg',
  'pjp',
  'pjpeg',

  // Mime type: 'image/png',
  'png',

  // TODO: Windows doesn't list '*.svg' in the image file selection dialog box.
  // TODO: Manually selecting a svg file causes Froala to return "Image file type is invalid.".

  // Mime type: 'image/svg+xml',
  'svg',

  // Mime type: 'image/webp',
  'webp',
];

const ALLOWED_VIDEO_TYPES = [
  // Mime type: 'video/x-msvideo'
  'avi',

  // Mime type: 'video/x-m4v'
  'm4v',

  // Mime type: 'video/mp4'
  'mp4',

  // Mime type: 'video/mpeg'
  'm2v',
  'mpeg',
  'mpg',

  // Mime type: 'video/web'
  'webm',
];

const MAX_FILE_SIZE = 1024 * 1024 * 16 - 1;

export function HtmlEditor(props: DeepReadOnly<HtmlEditorProps>) {
  const [requestEditing, setRequestEditing] = React.useState(false);
  const [currentlyEditing, setCurrentlyEditing] = React.useState<
    boolean | undefined
  >();
  const [haveRequestedFocus, setHaveRequestedFocus] = React.useState(false);
  const editor = useRef<FroalaEditor | null>(null);

  function possiblyShowEditor(e: React.MouseEvent<HTMLElement>) {
    if (e.target instanceof HTMLElement) {
      if (e.target?.nodeName?.toLowerCase() === 'a') {
        return;
      }
    }
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
            apiKey:
              'yDC5hH4I4B10D9A5E4A3g1JWSDBCQG1ZGDf1C1d2JXDAAOZWJhE5B4E4G3F2I3A8A4C4F5==',
            initOnClick: false,
            attribution: false,
            tabSpaces: 4,
            events: {
              blur: possiblyHideEditor,
              keydown: (e: JQuery.KeyDownEvent) => {
                if (e.key === 'Escape') {
                  possiblyHideEditor();
                }
              },
            },

            charCounterCount: false,

            placeholderText: props.editingPlaceholder ?? 'Type here...',

            toolbarButtons: TOOLBAR_BUTTONS,
            toolbarButtonsMD: TOOLBAR_BUTTONS,
            toolbarButtonsSM: TOOLBAR_BUTTONS,
            toolbarButtonsXS: TOOLBAR_BUTTONS,

            linkAlwaysBlank: true,

            listAdvancedTypes: true,

            fileAllowedTypes: ALLOWED_FILE_TYPES,
            fileMaxSize: MAX_FILE_SIZE,
            fileUploadURL: '/api/FileService/PostFile',

            filesManagerAllowedTypes: ALLOWED_FILE_TYPES,
            filesManagerMaxSize: MAX_FILE_SIZE,
            filesManagerUploadURL: '/api/FileService/PostFile',

            imageAllowedTypes: ALLOWED_IMAGE_TYPES,
            imageMaxSize: MAX_FILE_SIZE,
            imageUploadURL: '/api/FileService/PostFile',

            videoAllowedTypes: ALLOWED_VIDEO_TYPES,
            videoMaxSize: MAX_FILE_SIZE,
            videoUploadURL: '/api/FileService/PostFile',
          }}
        />
      </div>
      {props.value && !currentlyEditing && !props.alwaysShowEditor && (
        <div
          className="fr-view"
          dangerouslySetInnerHTML={{
            __html: props.value,
          }}
          onClick={possiblyShowEditor}
        />
      )}
      {!props.value && !currentlyEditing && !props.alwaysShowEditor && (
        <div className="fr-view" onClick={possiblyShowEditor}>
          <p>
            {props.placeholder ??
              'No content.' + (props.readOnly ? '' : ' Click to edit.')}
          </p>
        </div>
      )}
    </>
  );
}
