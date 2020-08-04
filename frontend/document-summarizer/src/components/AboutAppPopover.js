import React, { useState } from 'react'
import Button from '@material-ui/core/Button'
import Popover from '@material-ui/core/Popover'
import Typography from '@material-ui/core/Typography'
import aboutAppMsg from '../aboutAppMsg'

export default function AboutAppPopover() {

    const [ anchorEl, setAnchorEl ] = useState(null)

    const handleClick = e => {
        setAnchorEl(e.currentTarget)
    }

    const handleClose = () => {
        setAnchorEl(null)
    }

    const isOpen = Boolean(anchorEl)
    const popoverId = isOpen ? 'popover' : undefined

    return (
        <>
            <Button
                id='aboutButton'
                variant='outlined'
                component='span'
                color='secondary'
                onClick={handleClick}>
                About This App
            </Button>
            <Popover
                id={popoverId}
                open={isOpen}
                anchorEl={anchorEl}
                onClose={handleClose}
                anchorOrigin={{
                    vertical: 'bottom',
                    horizontal: 'left',
                }}
                transformOrigin={{
                    vertical: 'top',
                    horizontal: 'left',
                }}>
                <Typography variant='body2'>{aboutAppMsg}</Typography>
            </Popover>
        </>
    )
}