import React from 'react'
import { Button } from 'react-bootstrap'

export default function GenerateButton({ isLoading, isBadExtension, handleClick }) {
    return (
        <Button
            id='generateButton'
            variant='primary'
            disabled={isLoading || isBadExtension}
            size='lg'
            onClick={handleClick}>
            {isLoading ? 'Generating summary...' : 'Generate Summary'}
        </Button>
    )
}