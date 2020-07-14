import React from 'react'
import 'bootstrap/dist/css/bootstrap.min.css';
import 'react-typist/dist/Typist.css'
import Typist from 'react-typist'
import Loader from './Loader'


export default function SummarySection({ summaries, errorMessage, isLoading }) {
    // only display first element of summaries for now
    return (
        <div id='summarySection'>
            {summaries &&
                <div>
                    {summaries[0]}
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