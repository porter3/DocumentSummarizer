import React from 'react'
import Paper from '@material-ui/core/Paper'
import Alert from '@material-ui/lab/Alert'
import Loader from './Loader'


export default function SummarySection({ sentences, errorMessage, isLoading, sentenceThreshold, loaderMessage }) {

    return (
        <div id='summarySection'>
            {sentences.length !== 0 &&
                <Paper
                    id='paper'
                    elevation={3}>
                    <div id='paperText'>
                        {sentences.map(sentence => sentence.score >= sentenceThreshold ? <span key={sentence.orderPlacement}>{sentence.sentence + ' '}</span> : false)}
                    </div>
                </Paper>
            }
            {errorMessage &&
                <div id='errorMessage'>
                    <Alert severity='error'>
                        {errorMessage}
                    </Alert>
                </div>
            }
            {isLoading &&
                <Loader loaderMessage={loaderMessage} />
            }
        </div>
    )
}