import './PartialTextOpenAiPrompt.scss';
import {createService, partial_text_openai_prompt} from '../libs/protos';
import GetSuggestionsRequest = partial_text_openai_prompt.GetSuggestionsRequest;
import GetSuggestionsResponse = partial_text_openai_prompt.GetSuggestionsResponse;
import {useContext, useState} from 'react';
import {CgSearch, CgSearchLoading} from 'react-icons/cg';
import {GlobalStateContext} from '../libs/GlobalState';

export function PartialTextOpenAiPrompt(props: {
  id: string;
  initialSuggestions: string[];
  prompt: GetSuggestionsRequest.Prompt;
}) {
  const global = useContext(GlobalStateContext);

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
        global.setError({error: reason, reload: false});
      })
      .finally(() => setEnabled(true));
  }

  return (
    <>
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
