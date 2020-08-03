import { createMuiTheme } from '@material-ui/core/styles'
import './css/fonts/fontFaces.css'

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
        button: {
            fontFamily: [ 'montserratmedium', 'Times New Roman' ]
        },
        fontFamily: [ 'montserratlight', 'Times New Roman' ]
    }
})