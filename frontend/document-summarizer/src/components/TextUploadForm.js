import React from 'react';
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

export default function TextUploadForm({ uploadChoice, text, fileExtension, isBadExtension, fileName, isTooLargeFile, isDarkModeEnabled, handleRadioChange, handleTextChange, handleFileChange, handleClick }) {

    const textareaStyle = isDarkModeEnabled ? { backgroundColor: '#6B6B6B', color: '#ffffff' } : null

    return (
        <>
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
            <div hidden={uploadChoice !== 'fileUpload'}>
                <div className='uploadButtonRow'>
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
                </div>
                <div className='filenameRow'>
                    <Typography>
                        {fileName &&
                            fileName
                        }
                    </Typography>
                </div>
                {isBadExtension &&
                    <div className='badExtensionRow'>
                        <div className='col'>
                            <Alert className='fileError' severity='warning'>
                                <AlertTitle>Unsupported File Format</AlertTitle>
                                This can't summarize .{fileExtension} files.
                            </Alert>
                        </div>
                    </div>
                }
                {isTooLargeFile &&
                    <div className='tooLargeFileRow'>
                        <div className='col'>
                            <Alert className='fileError' severity='warning'>
                                <AlertTitle>File Too Large</AlertTitle>
                                Files cannot be larger than 5MB.
                            </Alert>
                        </div>
                    </div>
                }
            </div>
            <div hidden={uploadChoice !== 'text'}>
                <div className='clearBtnRow'>
                    <Button
                        hidden={uploadChoice !== 'text'}
                        variant='outlined'
                        component='span'
                        color='secondary'
                        startIcon={<ClearOutlinedIcon />}
                        onClick={handleClick}>
                        Clear
                    </Button>
                </div>
                <div className='textInput'>
                    <textarea
                        style={ textareaStyle }
                        id="textEntry"
                        onChange={handleTextChange}
                        value={text}
                        aria-label='text entry'
                    />
                </div>
            </div>
        </>
    )
}