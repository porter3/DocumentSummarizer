import React from 'react'
import Paper from '@material-ui/core/Paper'
import Alert from '@material-ui/lab/Alert'
import Loader from './Loader'


export default function SummarySection({ summaries, errorMessage, isLoading, summaryChoice, loaderMessage }) {
    return (
        <div id='summarySection'>
            {summaries.length !== 0 &&
                <Paper
                    id='paper'
                    elevation={3}>
                    <div id='paperText'>
                        {summaries[summaryChoice]}
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