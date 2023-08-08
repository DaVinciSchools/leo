import {stringSet} from './Venn';

type ChatCompletion = {
  id: string;
  object: string;
  created: number;
  choices: {
    index: number;
    message: {
      role: string;
      content: string;
    };
    finish_reason: string;
  }[];
  usage: {
    prompt_tokens: number;
    completion_tokens: number;
    total_tokens: number;
  };
};

async function generateCompletion(input: stringSet): Promise<stringSet> {
  const prompt = `Give me 4 project ideas for high school that develop and assess the skill (${input[1]}) that uses ${input[2]} and connects to ${input[3]} and a career of ${input[0]}. Make sure to separate the ideas with a new line and steer clear of any fluff.`;

  const response = await fetch('/api/openai/generateCompletion', {
    method: 'POST',
    body: prompt,
  });

  const data: ChatCompletion = await response.json();
  let completion = data.choices[0].message.content.split('\n\n', 4);
  if (completion.length < 4) {
    completion = data.choices[0].message.content.split('\n', 4);
  }

  return [completion[0], completion[1], completion[2], completion[3]];
}

export default generateCompletion;
