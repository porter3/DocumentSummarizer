import 'bootstrap/dist/css/bootstrap.min.css';
import React from 'react';
import { Row } from 'react-bootstrap';
import { FormControl, FormControlLabel, RadioGroup, Radio } from '@material-ui/core'


export default function TextUploadForm({ handleFileChange, handleTextChange }) {
    return (
        <Row>
            <FormControl component="fieldset">
                <RadioGroup aria-label="Upload Choices" name="uploadChoices" >
                    <FormControlLabel value="uploadFile" control={<Radio />} label="Upload File" />
                    <FormControlLabel value="enterText" control={<Radio />} label="Enter Text" />
                </RadioGroup>
                <div id="fileInput">
                    <label htmlFor="file">Upload file:</label><br />
                    <input type="file" name="file" id="file" onChange={handleFileChange} />
                </div>
                <textarea rows="18" cols="50" id="textEntry" onChange={handleTextChange} />
            </FormControl>
        </Row>
    )
}