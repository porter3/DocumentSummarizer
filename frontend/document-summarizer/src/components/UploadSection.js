import React from 'react'
import 'bootstrap/dist/css/bootstrap.min.css';
import { Row, Col } from 'react-bootstrap'

export default function UploadSection() {
    return (
        <div className='uploadSection'>
            <Row>
                <Col>
                    <h3>Upload a file or enter text to summarize</h3>
                </Col>
            </Row>
        </div>
    )
}