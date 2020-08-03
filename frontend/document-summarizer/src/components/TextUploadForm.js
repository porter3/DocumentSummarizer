import 'bootstrap/dist/css/bootstrap.min.css';
import React from 'react';
import { Row, Col } from 'react-bootstrap';
import { FormControl, FormControlLabel, RadioGroup, Radio, Typography, Button } from '@material-ui/core'
import Alert from '@material-ui/lab/Alert'
import AlertTitle from '@material-ui/lab/AlertTitle'


export default function TextUploadForm({ uploadChoice, text, fileExtension, isBadExtension, fileName, isTooLargeFile, handleRadioChange, handleTextChange, handleFileChange, handleClick }) {

    return (
        <div>
            <Row>
                <Col xs={12}>
                    <FormControl component="fieldset">
                        <RadioGroup aria-label="Upload Choices" name="uploadChoices" onChange={e => handleRadioChange(e)}>
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
                        </RadioGroup>
                    </FormControl>
                </Col>
            </Row>
            <Row>
                <Col xs={12}>
                    <div hidden={uploadChoice !== 'fileUpload'}>
                        <input type="file" name="file" id="file" style={{ display: 'none' }} onChange={handleFileChange} />
                        <label htmlFor='file'>
                            <Button variant='outlined' component='span' color='secondary'>Upload File</Button>
                        </label>
                        <Row>
                            <Typography id='fileName'>
                                {fileName &&
                                    fileName
                                }
                            </Typography>
                        </Row>
                        {isBadExtension &&
                            <Row>
                                <Col sm={12}>
                                    <Alert className='fileError' severity='warning'>
                                        <AlertTitle>Unsupported File Format</AlertTitle>
                                        This can't summarize .{fileExtension} files.
                                    </Alert>
                                </Col>
                            </Row>
                        }
                        {isTooLargeFile &&
                            <Row>
                                <Col sm={12}>
                                    <Alert className='fileError' severity='warning'>
                                        <AlertTitle>File Too Large</AlertTitle>
                                        Files cannot be larger than 5MB.
                                    </Alert>
                                </Col>
                            </Row>
                        }
                    </div>
                    <div id='textDiv' hidden={uploadChoice !== 'text'}>
                        <Row>
                            <Col xs={12}>
                                <Button
                                    hidden={uploadChoice !== 'text'}
                                    id='clearButton'
                                    variant='outlined'
                                    component='span'
                                    color='secondary'
                                    onClick={handleClick}>
                                        Clear
                                </Button>
                                <textarea rows="20" id="textEntry" onChange={handleTextChange} value={text} />
                            </Col>
                        </Row>
                    </div>
                </Col>
            </Row>
        </div>
    )
}