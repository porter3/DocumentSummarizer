import React from 'react'
import Typography from '@material-ui/core/Typography'
import '../styling/css/loader.css'

export default function Loader({ loaderMessage }) {
    return (
        <>
            <div className='spinnerRow'>
                <div className="reverse-spinner"></div>
            </div>
            {loaderMessage &&
                <div className='messageRow'>
                    <Typography variant='h5'>{loaderMessage}</Typography>
                </div>
            }
        </>
    )
}