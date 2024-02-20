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
    fontFamily: [
      'hind',
      '-apple-system',
      'BlinkMacSystemFont',
      'Segoe UI',
      'Roboto',
      'Oxygen',
      'Ubuntu',
      'Cantarell',
      'Fira Sans',
      'Droid Sans',
      'Helvetica Neue',
      'sans-serif',
    ].join(','),
    h1: {
      fontFamily: 'montserrat',
    },
    h2: {
      fontFamily: 'montserrat',
    },
    h3: {
      fontFamily: 'montserrat',
    },
    h4: {
      fontFamily: 'montserrat',
    },
    h5: {
      fontFamily: 'montserrat',
    },
    h6: {
      fontFamily: 'montserrat',
    },
    button: {
      textTransform: 'none',
      fontWeight: 600,
    },
  },
};

export default THEME;
