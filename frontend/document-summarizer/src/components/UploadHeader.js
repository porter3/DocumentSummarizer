import React from 'react'
import 'bootstrap/dist/css/bootstrap.min.css';
import { Row, Col } from 'react-bootstrap'

export default function UploadHeader() {
    return (
        <div id='uploadHeader'>
            <Row>
                <Col>
                    <h3>Upload a file or enter text to summarize</h3>
                </Col>
            </Row>
        </div>
    )
}