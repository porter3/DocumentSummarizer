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
        h3: {
            fontSize: '1.75rem'
        },
        button: {
            fontFamily: [ 'montserratmedium', 'Times New Roman' ].join(',')
        },
        fontFamily: [ 'montserratlight', 'Times New Roman' ].join(',')
    }
})