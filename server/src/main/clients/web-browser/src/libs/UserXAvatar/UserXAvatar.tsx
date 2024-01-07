import {pl_types} from 'pl-pb';
import {DeepReadOnly} from '../misc';
import {Avatar} from '@mui/material';
import IUserX = pl_types.IUserX;

export function UserXAvatar(
  props: DeepReadOnly<{
    userX?: IUserX | null;
    size?: string;
  }>
) {
  return (
    <>
      <Avatar
        alt={`${props.userX?.firstName ?? ''} ${props.userX?.lastName ?? ''}`}
        src={props.userX?.avatarImageUrl ?? ''}
        sx={{
          width: props.size,
          height: props.size,
          bgcolor: `hsl(${((props.userX?.id ?? 0) * 29) % 256}, 100%, 45%)`,
          fontSize: `calc(0.5 * ${props.size})`,
        }}
      >
        {/*The dots help center the text.*/}
        <span style={{color: 'rgba(0,0,0,0)'}}>.</span>
        {props.userX?.firstName?.[0] ?? ''}
        {props.userX?.lastName?.[0] ?? ''}
        <span style={{color: 'rgba(0,0,0,0)'}}>.</span>
      </Avatar>
    </>
  );
}
