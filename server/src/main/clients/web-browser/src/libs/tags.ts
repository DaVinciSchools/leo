export function addClassName(
  tagProps: {className?: string},
  additionalClassNames: string
) {
  const newTagProps = {...tagProps};
  newTagProps.className = (
    additionalClassNames +
    ' ' +
    (newTagProps.className ?? '')
  ).trim();
  return newTagProps;
}
