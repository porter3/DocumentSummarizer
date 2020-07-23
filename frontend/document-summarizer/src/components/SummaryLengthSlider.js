import React from 'react'
import { Slider } from '@material-ui/core'

export default function SummaryLengthSlider({ handleChange, max }) {

    const marks = [
        { 
            value: 0,
            label: 'Verbose'
        },
        {
            value: max,
            label: 'Succinct'
        }
    ]

    return (
        <Slider
            marks={marks}
            onChange={handleChange}
            defaultValue={0}
            step={1}
            min={0}
            max={max}
            valueLabelDisplay='off'
        />
    )
}