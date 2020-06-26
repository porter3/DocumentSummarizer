import React from 'react'
import 'bootstrap/dist/css/bootstrap.min.css';
import { Row, Col, Button } from 'react-bootstrap'

export default function UploadSection() {
    return (
        <>
            <Row>
                <Col xs={4}>
                    <h3>Upload a file or enter text to summarize</h3>
                </Col>
                <Col>
                    <Button>This is a button</Button>
                </Col>
            </Row>
        </>
    )
}