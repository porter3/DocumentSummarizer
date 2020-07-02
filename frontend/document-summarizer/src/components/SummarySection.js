import React from 'react'
import 'bootstrap/dist/css/bootstrap.min.css';
import Loader from './Loader'
import { motion } from 'framer-motion'
import { Alert } from 'react-bootstrap'

export default function SummarySection({ summary, error, isLoading }) {

    const summaryAnimation = { opacity: summary ? 1 : 0 }
    const errorAnimation = { opacity: error ? 1 : 0 }

    return (
        <div id='summarySection'>
            <motion.div
                id='summary'
                animate={summaryAnimation}>
                {summary &&
                    `${summary}`
                }
            </motion.div>
            <motion.div
                id='error'
                animate={errorAnimation}>
                <Alert variant='danger'>
                    {error &&
                        `${error}`
                    }
                </Alert>
            </motion.div>
            {isLoading &&
                <Loader />
            }
        </div>
    )
}