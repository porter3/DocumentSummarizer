import React from 'react'
import Button from '@material-ui/core/Button'

export default function GenerateButton({ isLoading, isBadExtension, handleClick }) {

    return (
            <Button
                id='generateButton'
                variant='contained'
                color='primary'
                disabled={isLoading || isBadExtension}
                size='large'
                onClick={handleClick}>
                {isLoading ? 'Generating summary...' : 'Generate Summary'}
            </Button>
    )
}