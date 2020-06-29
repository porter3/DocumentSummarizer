import 'bootstrap/dist/css/bootstrap.min.css';
import React from 'react';
import { Row } from 'react-bootstrap';
import { FormControl, FormControlLabel, RadioGroup, Radio } from '@material-ui/core'

// TODO: download/import react-radio-group

export default function TextUploadForm() {
    return (
        <Row>
            <FormControl component="fieldset">
                <RadioGroup aria-label="Upload Choices" name="uploadChoices" >
                    <FormControlLabel value="uploadFile" control={<Radio />} label="Upload File" />
                    <FormControlLabel value="enterText" control={<Radio />} label="Enter Text" />
                </RadioGroup>
                <label for="file">Upload file:</label>
                <input type="file" name="file" id="file" />
                <textarea rows="18" cols="50" id="textEntry" />
            </FormControl>
        </Row>
    )
}