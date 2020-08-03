import React from 'react'
import 'bootstrap/dist/css/bootstrap.min.css';
import { Row, Col } from 'react-bootstrap'
import Typography from '@material-ui/core/Typography'

export default function UploadHeader() {
    return (
        <div id='uploadHeader'>
            <Row>
                <Col>
                    <Typography variant='h3'>Upload a file or enter text to summarize</Typography>
                </Col>
            </Row>
        </div>
    )
}