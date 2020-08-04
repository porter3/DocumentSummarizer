import React from 'react';
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import FormControl from '@material-ui/core/FormControl'
import FormControlLabel from '@material-ui/core/FormControlLabel'
import RadioGroup from '@material-ui/core/RadioGroup'
import Radio from '@material-ui/core/Radio'
import Typography from '@material-ui/core/Typography'
import Button from '@material-ui/core/Button'
import CloudUploadOutlinedIcon from '@material-ui/icons/CloudUploadOutlined'
import ClearOutlinedIcon from '@material-ui/icons/ClearOutlined'
import Alert from '@material-ui/lab/Alert'
import AlertTitle from '@material-ui/lab/AlertTitle'

export default function TextUploadForm({ theme, uploadChoice, text, fileExtension, isBadExtension, fileName, isTooLargeFile, isDarkModeEnabled, handleRadioChange, handleTextChange, handleFileChange, handleClick }) {

    const textareaStyle = isDarkModeEnabled ? { backgroundColor: '#6B6B6B', color: '#ffffff' } : null

    return (
        <div>
            <Row>
                <Col xs={12}>
                    <FormControl component="fieldset">
                        <RadioGroup aria-label="Upload Choices" name="uploadChoices" onChange={e => handleRadioChange(e)}>
                            <FormControlLabel
                                value="fileUpload"
                                control={<Radio />}
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
                            <Button
                                variant='outlined'
                                component='span'
                                color='secondary'
                                startIcon={<CloudUploadOutlinedIcon />}>
                                    Upload File
                            </Button>
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
                                    startIcon={<ClearOutlinedIcon />}
                                    onClick={handleClick}>
                                    Clear
                                </Button>
                                    <textarea
                                        style={ textareaStyle }
                                        rows='20'
                                        id="textEntry"
                                        onChange={handleTextChange}
                                        value={text}
                                        aria-label='text entry'
                                    />
                            </Col>
                        </Row>
                    </div>
                </Col>
            </Row>
        </div>
    )
}