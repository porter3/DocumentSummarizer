import { createMuiTheme } from '@material-ui/core/styles'
import './css/fontFaces.css'

export default function createTheme(isDarkModeEnabled) {
    return createMuiTheme({
        palette: { 
            type: isDarkModeEnabled ? 'dark' : 'light',
            primary: {
                main: '#6DD3CE',
                dark: '#639FAB'
            },
            secondary: {
                main: '#52CBC5'
            }
        },
        typography: {
            fontFamily: [ 'montserratlight', 'Times New Roman' ].join(','),
            body2: {
                fontFamily: [ 'montserratmedium', 'Times New Roman' ].join(',')
            },
            h3: {
                fontSize: '1.75rem'
            },
            button: {
                fontFamily: [ 'montserratmedium', 'Times New Roman' ].join(',')
            }
        }
    })
}