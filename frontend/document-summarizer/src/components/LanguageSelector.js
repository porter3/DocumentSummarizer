import React from 'react'
import InputLabel from '@material-ui/core/InputLabel'
import MenuItem from '@material-ui/core/MenuItem'
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select'
import { makeStyles } from '@material-ui/core/styles'

const useStyles = makeStyles((theme) => ({
    formControl: {
        minWidth: 150
    },
    select: {
        marginBottom: theme.spacing(3)
    },
}));

export default function LanguageSelector({ language, handleChange }) {
    const classes = useStyles();

    return (
        <FormControl className={classes.formControl} variant="standard">
            <InputLabel id="language-selector-label">Language</InputLabel>
            <Select
                labelId="language-selector-label"
                id="language-selector"
                value={language}
                onChange={handleChange}
                className={classes.select}
            >
                <MenuItem value={"en"} selected>English (US)</MenuItem>
                <MenuItem value={'es'}>Spanish</MenuItem>
            </Select>
        </FormControl>
    )
}