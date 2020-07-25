import 'bootstrap/dist/css/bootstrap.min.css';
import React from 'react';
import { Row, Col } from 'react-bootstrap';
import { FormControl, FormControlLabel, RadioGroup, Radio, Typography, ThemeProvider } from '@material-ui/core'


export default function TextUploadForm({ uploadChoice, handleRadioChange, handleFileChange, handleTextChange, text, theme }) {


    return (
        <div>
            <Row>
                <Col sm={12}>
                    <FormControl component="fieldset">
                        <RadioGroup aria-label="Upload Choices" name="uploadChoices" onChange={e => handleRadioChange(e)}>
                            <ThemeProvider theme={theme}>
                                <FormControlLabel value="fileUpload" control={<Radio />}
                                label={
                                    <>
                                        <Typography>Upload File</Typography>
                                        <Typography><small>(.docx, .doc, .pdf, .txt)</small></Typography>
                                    </>
                                }/>
                                <FormControlLabel value="text" control={<Radio />}
                                label={
                                    <Typography>Enter Text</Typography>
                                }/>
                            </ThemeProvider>
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