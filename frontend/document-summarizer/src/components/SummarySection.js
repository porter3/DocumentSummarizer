import React from 'react'
import 'bootstrap/dist/css/bootstrap.min.css';
import 'react-typist/dist/Typist.css'
import Typist from 'react-typist'
import Loader from './Loader'

/*
    previousSummary/previousError are necessary to render the Typist component if one summary or error has already been generated.
    summary/error renders fine inside other elements w/o the extra conditional, so it's likely a bug in the Typist component.
*/
export default function SummarySection({ summary, errorMessage, isLoading }) {

    return (
        <div id='summarySection'>
            {summary &&
                <div>
                    {summary}
                </div>
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