import 'bootstrap/dist/css/bootstrap.min.css';
import React from 'react';
import { Row, Col, Button } from 'react-bootstrap';
import { FormControl, FormControlLabel, RadioGroup, Radio } from '@material-ui/core'


export default function TextUploadForm({ uploadChoice, handleRadioChange, handleFileChange, handleTextChange, text, handleClick, isLoading }) {


    return (
        <Row>
            <Col sm={6}>
                <FormControl component="fieldset">
                    <RadioGroup aria-label="Upload Choices" name="uploadChoices" onChange={e => handleRadioChange(e)}>
                        <FormControlLabel value="fileUpload" control={<Radio />} label="Upload File" />
                        <FormControlLabel value="text" control={<Radio />} label="Enter Text" />
                    </RadioGroup>
                    {uploadChoice === 'fileUpload' &&
                        <div>
                            <label htmlFor ="file">Upload file:</label><br />
                            <input type="file" name="file" id="file" onChange={handleFileChange} />
                        </div>
                    }
                    {uploadChoice === 'text' &&
                        <textarea rows="18" cols="50" id="textEntry" onChange={handleTextChange} value={text} />
                    }
                </FormControl>
            </Col>
            <Col>
                <Button
                    variant='primary'
                    // disabled={isLoading}
                    size='lg'
                    onClick={handleClick}>
                    {isLoading ? 'Generating summary...' : 'Generate Summary'}
                </Button>
            </Col>
        </Row>
    )
}