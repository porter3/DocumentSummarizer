import React, { useState, useRef } from 'react'
import { CSSTransition } from 'react-transition-group'
import Paper from '@material-ui/core/Paper'
import Typography from '@material-ui/core/Typography'
import Alert from '@material-ui/lab/Alert'
import Button from '@material-ui/core/Button'
import FileCopyOutlinedIcon from '@material-ui/icons/FileCopyOutlined'
import Loader from './Loader'
import SummaryLengthSlider from './SummaryLengthSlider'
import '../styling/css/flexLayout.css'
import '../styling/css/copyMessageAnimation.css'


// CSSTransition in here is not working at the moment but doesn't break anything
export default function SummarySection({ sentences, errorMessage, isLoading, sentenceThreshold, loaderMessage, handleSliderChange, maxSummaries }) {
    
    const sentenceCount = sentences.length
    const [ copyMessage, setCopyMessage ] = useState('')
    const nodeRef = useRef(null)

    const copyText = () => {
        // select summary text
        const summaryText = document.querySelector('.summaryText').textContent
        // append temp textarea to DOM and set its value to the summary text
        const tempTextarea = document.createElement('textarea')
        // substring() is for removing last unneccessary space in the text
        tempTextarea.value = summaryText.substring(0, summaryText.length - 1)
        document.body.appendChild(tempTextarea)
        // select text inside temp textarea, copy it, remove it from DOM
        tempTextarea.select()
        const isSuccessfulCopy = document.execCommand('copy')
        document.body.removeChild(tempTextarea)
        return isSuccessfulCopy
    }
    
    const executeCopyAndFeedback = () => {
        const isSuccessfulCopy = copyText()
        const message = isSuccessfulCopy ? 'Copy successful!' : 'Copy unsuccessful. Your browser may not support it.'
        setCopyMessage(message)
        setTimeout(() => {
            setCopyMessage('')
        }, 2000)
    }

    return (
        <div className='resultsSection'>
            {sentenceCount !== 0 &&
                <div className='summarySectionCol'>
                    <div className='copyButtonRow'>
                        <div className='copyMsgCol'>
                            <CSSTransition
                                nodeRef={nodeRef}
                                // linter complains about trying to use a string as a boolean
                                in={copyMessage !== ''}
                                timeout={100}
                                classNames='fade'
                            >
                                <Typography
                                    variant='body2'
                                    key={copyMessage}
                                >
                                    {copyMessage}
                                </Typography>
                            </CSSTransition>
                        </div>
                        <Button
                            variant='outlined'
                            component='span'
                            color='secondary'
                            startIcon={<FileCopyOutlinedIcon />}
                            onClick={() => executeCopyAndFeedback()}
                        >
                            Copy
                        </Button>
                    </div>
                    <Paper elevation={3} className='summaryPaper'>
                        <div className='paperText'>
                            <Typography className='summaryText'>
                                {
                                    sentences.map(sentence => sentence.score >= sentenceThreshold
                                        ? <span key={sentence.orderPlacement}>{sentence.sentence + ' '}</span>
                                        : false
                                    )
                                }
                            </Typography>
                        </div>
                    </Paper>
                    {sentenceCount > 1 &&
                        <div className='slider'>
                            <SummaryLengthSlider
                                handleChange={handleSliderChange}
                                max={maxSummaries}
                            />
                        </div>
                    }
                </div>
            }
            {errorMessage &&
                <div className='errorSection'>
                    <Alert severity='error'>
                        {errorMessage}
                    </Alert>
                </div>
            }
            {isLoading &&
                <div className='loaderSection'>   
                    <Loader loaderMessage={loaderMessage} />
                </div>
            }
        </div>
    )
}