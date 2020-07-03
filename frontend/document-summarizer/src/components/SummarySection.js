import React from 'react'
import 'bootstrap/dist/css/bootstrap.min.css';
import 'react-typist/dist/Typist.css'
import Typist from 'react-typist'
import Loader from './Loader'

/*
    previousSummary/previousError are necessary to render the Typist component if one summary or error has already been generated.
    summary/error renders fine inside other elements w/o the extra conditional, so it's likely a bug in the Typist component.
*/
export default function SummarySection({ summary, previousSummary, error, previousError, isLoading }) {

    return (
        <div>
            {summary && summary !== previousSummary &&
                <Typist
                    avgTypingDelay={0}
                    stdTypingDelay={0}>
                        {summary}
                </Typist>
            }
            {error && error !== previousError &&
                <Typist
                    avgTypingDelay={0}
                    stdTypingDelay={0}>
                        {error}
                </Typist>
            }
            {isLoading &&
                <Loader />
            }
        </div>
    )
}