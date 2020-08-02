import React from 'react'
import '../css/loader.css'

export default function Loader({ loaderMessage }) {
    return (
        <div>
            <div className="reverse-spinner"></div>
            {loaderMessage &&
                <div id='loaderMessage'>{loaderMessage}</div>
            }
        </div>
    )
}