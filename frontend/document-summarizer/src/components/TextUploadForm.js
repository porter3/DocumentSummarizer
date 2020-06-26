import React from 'react'
import 'bootstrap/dist/css/bootstrap.min.css';
import { Row, Form } from 'react-bootstrap'

export default function TextUploadForm() {
    return (
        <Row>
            <Form>
                <Form.Group>
                    <Form.Check type='radio' label='Upload File (.docx)' checked />
                </Form.Group>
            </Form>
        </Row>
    )
}