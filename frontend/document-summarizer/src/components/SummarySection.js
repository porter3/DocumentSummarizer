import React from 'react'
import { motion } from 'framer-motion'

export default function SummarySection({ summary, error }) {

    const summaryAnimation = { opacity: summary ? 1 : 0 }
    const errorAnimation = { opacity: error ? 1 : 0 }

    return (
        <>
            <motion.div
                className="summary"
                animate={summaryAnimation}>
                {summary &&
                    `${summary}`
                }
            </motion.div>
            <motion.div
                className="errorMsg"
                animate={errorAnimation}>
                {error &&
                    `${error}`
                }
            </motion.div>
        </>
    )
}