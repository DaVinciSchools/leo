import {ThemeOptions} from '@mui/material';

const THEME: ThemeOptions = {
  components: {
    MuiButtonBase: {
      defaultProps: {
        disableRipple: true,
      },
    },
  },
  palette: {
    primary: {
      main: '#FF9800',
      contrastText: '#FFFFFF',
    },
    secondary: {
      main: '#1677FF',
    },
  },
  typography: {
    button: {
      textTransform: 'none',
      fontWeight: 600,
    },
  },
};

export default THEME;
