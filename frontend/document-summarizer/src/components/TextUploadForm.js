import 'bootstrap/dist/css/bootstrap.min.css';
import React from 'react';
import { Row, Col } from 'react-bootstrap';
import { FormControl, FormControlLabel, RadioGroup, Radio } from '@material-ui/core'


export default function TextUploadForm({ uploadChoice, handleRadioChange, handleFileChange, handleTextChange, text, handleClick, isLoading }) {


    return (
        <div>
            <Row>
                <Col sm={12}>
                    <FormControl component="fieldset">
                        <RadioGroup aria-label="Upload Choices" name="uploadChoices" onChange={e => handleRadioChange(e)}>
                            <FormControlLabel value="fileUpload" control={<Radio />} label="Upload File (.docx, .doc, .pdf, .txt)" />
                            <FormControlLabel value="text" control={<Radio />} label="Enter Text" />
                        </RadioGroup>
                    </FormControl>
                </Col>
            </Row>
            <Row>
                <Col sm={12}>
                    <div hidden={uploadChoice !== 'fileUpload'}>
                        <label htmlFor ="file">Upload file:</label><br />
                        <input type="file" name="file" id="file" onChange={handleFileChange} />
                    </div>
                    <div hidden={uploadChoice !== 'text'}>
                        <textarea rows="18" cols="50" id="textEntry" onChange={handleTextChange} value={text} />
                    </div>
                </Col>
            </Row>
        </div>
    )
}