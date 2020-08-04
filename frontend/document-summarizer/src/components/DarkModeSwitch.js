import React from 'react'
import FormControlLabel from '@material-ui/core/FormControlLabel'
import Switch from '@material-ui/core/Switch'
import Typography from '@material-ui/core/Typography'

export default function DarkModeSwitch({ isDarkModeEnabled, handleChange }) {

    return(
        <span id='darkModeSwitch'>
            <FormControlLabel
                control={
                    <Switch
                        color='primary'
                        checked={isDarkModeEnabled}
                        onChange={handleChange}
                    />}
                label={<Typography variant='body2'>Dark Mode</Typography>} />
        </span>
    )
}