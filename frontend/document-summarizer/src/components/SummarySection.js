import React from 'react'
import 'bootstrap/dist/css/bootstrap.min.css';
import { Paper } from '@material-ui/core'
import Loader from './Loader'


export default function SummarySection({ summaries, errorMessage, isLoading, summaryChoice }) {
    // only display first element of summaries for now
    return (
        <div id='summarySection'>
            {summaries.length !== 0 &&
                <Paper style={{minHeight: 400, maxHeight: 400, overflow: 'auto'}}>
                    <div>
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
                <Loader />
            }
        </div>
    )
}