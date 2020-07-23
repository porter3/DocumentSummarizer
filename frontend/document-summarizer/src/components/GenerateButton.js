import React from 'react'
import { Button } from 'react-bootstrap'

export default function GenerateButton({ isLoading, handleClick }) {
    return (
        <Button
            id='generateButton'
            variant='primary'
            disabled={isLoading}
            size='lg'
            onClick={handleClick}>
            {isLoading ? 'Generating summary...' : 'Generate Summary'}
        </Button>
    )
}