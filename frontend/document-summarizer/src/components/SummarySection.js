import React from 'react'
import 'bootstrap/dist/css/bootstrap.min.css';
import Paper from '@material-ui/core/Paper'
import Loader from './Loader'


export default function SummarySection({ summaries, errorMessage, isLoading, summaryChoice, loaderMessage }) {
    // only display first element of summaries for now
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
                <div>
                    {errorMessage}
                </div>
            }
            {isLoading &&
                <Loader loaderMessage={loaderMessage} />
            }
        </div>
    )
}