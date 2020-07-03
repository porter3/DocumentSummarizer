import 'bootstrap/dist/css/bootstrap.min.css';
import React from 'react';
import { Row, Col, Button } from 'react-bootstrap';
import { FormControl, FormControlLabel, RadioGroup, Radio } from '@material-ui/core'


export default function TextUploadForm({ uploadChoice, handleRadioChange, handleFileChange, handleTextChange, text, handleClick, isLoading }) {


    return (
        <Row>
            <Row>
                <Col sm={8}>
                    <FormControl component="fieldset">
                        <RadioGroup aria-label="Upload Choices" name="uploadChoices" onChange={e => handleRadioChange(e)}>
                            <FormControlLabel value="fileUpload" control={<Radio />} label="Upload File (.docx, .doc)" />
                            <FormControlLabel value="text" control={<Radio />} label="Enter Text" />
                        </RadioGroup>
                    </FormControl>
                </Col>
                <Col sm={4}>
                    <Button
                        variant='primary'
                        disabled={isLoading}
                        size='lg'
                        onClick={handleClick}>
                        {isLoading ? 'Generating summary...' : 'Generate Summary'}
                    </Button>
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
        </Row>
    )
}