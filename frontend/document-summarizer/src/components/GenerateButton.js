import React from 'react'
import supportedFileFormats from '../supportedFileFormats'
import { Button } from 'react-bootstrap'

export default function GenerateButton({ isLoading, fileExtension, isBadExtension, handleClick }) {
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