import React from 'react'
import Button from '@material-ui/core/Button'

export default function GenerateButton({ isLoading, isBadExtension, isTooLargeFile, uploadChoice, handleClick }) {

    return (
            <Button
                id='generateButton'
                variant='contained'
                color='primary'
                disabled={(uploadChoice === 'fileUpload' && (isBadExtension || isTooLargeFile)) || isLoading}
                size='large'
                onClick={handleClick}>
                {isLoading ? 'Summarizing...' : 'Summarize'}
            </Button>
    )
}