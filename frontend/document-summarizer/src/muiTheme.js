import { createMuiTheme } from '@material-ui/core/styles'

export default createMuiTheme({
    palette: { 
        type: 'light',
        primary: {
            main: '#6DD3CE',
            dark: '#639FAB'
        },
        secondary: {
            main: '#52CBC5'
        }
    },
    typography: {
        fontFamily: [ 'Montserrat', 'Times New Roman' ]
    }
})