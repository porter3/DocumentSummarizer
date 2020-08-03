import React from 'react'
import Button from '@material-ui/core/Button'

export default function GenerateButton({ isLoading, isBadExtension, isTooLargeFile, handleClick }) {

    return (
            <Button
                id='generateButton'
                variant='contained'
                color='primary'
                disabled={isLoading || isBadExtension || isTooLargeFile}
                size='large'
                onClick={handleClick}>
                {isLoading ? 'Generating summary...' : 'Generate Summary'}
            </Button>
    )
}