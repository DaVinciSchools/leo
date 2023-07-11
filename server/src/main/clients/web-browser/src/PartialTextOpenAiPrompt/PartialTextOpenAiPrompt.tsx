import './PartialTextOpenAiPrompt.scss';
import {createService, partial_text_openai_prompt} from '../libs/protos';
import GetSuggestionsRequest = partial_text_openai_prompt.GetSuggestionsRequest;
import GetSuggestionsResponse = partial_text_openai_prompt.GetSuggestionsResponse;
import {useState} from 'react';
import {CgSearch, CgSearchLoading} from 'react-icons/cg';
import {HandleError, HandleErrorType} from '../libs/HandleError/HandleError';

export function PartialTextOpenAiPrompt(props: {
  id: string;
  initialSuggestions: string[];
  prompt: GetSuggestionsRequest.Prompt;
}) {
  const [handleError, setHandleError] = useState<HandleErrorType>();
  const [enabled, setEnabled] = useState(true);
  const [suggestions, setSuggestions] = useState(props.initialSuggestions);

  // Calls the server to get a fresh list of suggestions for the text in the text box.
  function updateSuggestions(): void {
    if (!enabled) {
      return;
    }
    const input = document.getElementById(props.id) as HTMLInputElement;
    if (!input) {
      return;
    }

    const partialTextOpenAiPromptService = createService(
      partial_text_openai_prompt.PartialTextOpenAiPromptService,
      'PartialTextOpenAiPromptService'
    );

    setEnabled(false);
    setSuggestions([]);
    partialTextOpenAiPromptService
      .getSuggestions(
        GetSuggestionsRequest.create({
          partialText: input.value,
          prompt: props.prompt,
        })
      )
      .then((response: GetSuggestionsResponse) => {
        input.value = '';
        setSuggestions(response.suggestions);
      })
      .catch(reason => {
        setSuggestions([]);
        setHandleError({error: reason, reload: false});
      })
      .finally(() => setEnabled(true));
  }

  return (
    <>
      <HandleError error={handleError} setError={setHandleError} />
      <div className="partial-text-form-elements">
        <input
          id={props.id}
          className="partial-text-input"
          type="text"
          list={props.id + '.list'}
          style={{flexGrow: 1}}
        />
        <datalist id={props.id + '.list'}>
          {suggestions.map((item: string) => (
            <option key={item} value={item}></option>
          ))}
        </datalist>
        {enabled ? (
          <CgSearch
            className="partial-text-button"
            onClick={updateSuggestions}
          />
        ) : (
          <CgSearchLoading className="partial-text-button" />
        )}
      </div>
    </>
  );
}
