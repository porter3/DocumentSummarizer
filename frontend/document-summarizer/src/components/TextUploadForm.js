import React from 'react'
import 'bootstrap/dist/css/bootstrap.min.css';
import { Row, Form } from 'react-bootstrap'

// TODO: download/import react-radio-group

export default function TextUploadForm() {
    return (
        <Row>
            <Form>
                <Form.Group>
                    <Form.Check type='radio' name='inputType' label='Upload File (.docx)' checked />
                </Form.Group>
                <Form.Group>
                    <Form.File id='fileInput' name='file'></Form.File>
                </Form.Group>
                <Form.Group>
                    <Form.Check type='radio' name='inputType' label='Copy and paste text' />
                </Form.Group>
                <Form.Group hidden>
                    <Form.Label>Enter Document Text</Form.Label>
                    <Form.Control as="textarea" rows='18' />
                </Form.Group>
            </Form>
        </Row>
    )
}