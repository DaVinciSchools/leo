import {useContext, useState} from 'react';

import {UserXAvatar} from '../UserXAvatar/UserXAvatar';
import {GlobalStateContext} from '../GlobalState';
import {Divider, IconButton, MenuItem, Popover} from '@mui/material';
import {Link} from 'react-router-dom';
import {styled} from 'styled-components';

const AvatarWrapper = styled.div`
  padding: ${props => props.theme.spacing(3)} ${props => props.theme.spacing(4)};
  display: flex;
  flex-direction: column;
  place-items: center;
  gap: ${props => props.theme.spacing(1)};
`;

const Email = styled.div`
  font-size: 12px;
`;

const Name = styled.div`
  padding-top: ${props => props.theme.spacing(2)};
  font-weight: 600;
`;

const PopoverContent = styled.div`
  display: flex;
  flex-direction: column;
`;

export function AvatarNavMenu() {
  const global = useContext(GlobalStateContext);
  const userX = global.optionalUserX();

  const [anchorElUser, setAnchorElUser] = useState<Element | null>(null);

  const handleOpenUserMenu = (event: React.MouseEvent<HTMLButtonElement>) => {
    setAnchorElUser(event.currentTarget);
  };

  const handleCloseUserMenu = () => {
    setAnchorElUser(null);
  };

  return userX != null ? (
    <>
      <IconButton onClick={handleOpenUserMenu}>
        <UserXAvatar userX={userX} />
      </IconButton>
      <Popover
        anchorEl={anchorElUser}
        open={Boolean(anchorElUser)}
        onClose={handleCloseUserMenu}
        anchorOrigin={{
          vertical: 'bottom',
          horizontal: 'right',
        }}
        transformOrigin={{
          vertical: 'top',
          horizontal: 'right',
        }}
      >
        <PopoverContent>
          <AvatarWrapper>
            <UserXAvatar size="7rem" userX={userX} />
            <Name>
              {userX.firstName} {userX.lastName}
            </Name>
            {userX.emailAddress && <Email>{userX.emailAddress}</Email>}
          </AvatarWrapper>
          <Divider />
          <MenuItem
            component={Link}
            to="/users/my-account.html"
            onClick={handleCloseUserMenu}
          >
            Manage account
          </MenuItem>
          <MenuItem component={Link} to="/users/logout.html">
            Log out
          </MenuItem>
        </PopoverContent>
      </Popover>
    </>
  ) : (
    <></>
  );
}
