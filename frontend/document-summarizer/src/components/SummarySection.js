import React from 'react'
import Paper from '@material-ui/core/Paper'
import Alert from '@material-ui/lab/Alert'
import Loader from './Loader'
import SummaryLengthSlider from './SummaryLengthSlider'
import '../styling/css/flexLayout.css'

export default function SummarySection({ sentences, errorMessage, isLoading, sentenceThreshold, loaderMessage, handleSliderChange, maxSummaries }) {

    return (
        <>
            {sentences.length !== 0 &&
                <div className='summarySection'>
                    <Paper elevation={3} className='summaryPaper'>
                            <div id='paperText'>
                                {
                                    sentences.map(sentence => sentence.score >= sentenceThreshold
                                        ? <span key={sentence.orderPlacement}>{sentence.sentence + ' '}</span>
                                        : false
                                    )}
                            </div>
                    </Paper>
                    {sentences.length > 1 &&
                        <SummaryLengthSlider
                            handleChange={handleSliderChange}
                            max={maxSummaries}
                        />
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
        </>
    )
}