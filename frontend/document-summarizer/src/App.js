import React, { useState } from 'react'
import UploadHeader from './components/UploadHeader'
import TextUploadForm from './components/TextUploadForm'
import SummarySection from './components/SummarySection'
import { serverUrl } from './serverUrl'
import 'bootstrap/dist/css/bootstrap.min.css'
import './css/app.css'
import { Container, Row, Col } from 'react-bootstrap'


function App() {

  const [ isLoading, setIsLoading ] = useState(false)
  const [ uploadChoice, setUploadChoice ] = useState('')
  const [ text, setText ] = useState('')
  const [ fileIsLoaded, setFileIsLoaded ] = useState(false)
  const [ summary, setSummary ] = useState('')
  const [ error, setError ] = useState('')
  
  const handleUploadChoice = e => {
    setUploadChoice(e.target.value)
  }

  const handleTextChange = e => {
    setText(e.target.value)
  }

  const fetchSummary = (url, body) => {
    return fetch(url, {
      method: 'POST',
      body: body
    })
      .then(response => response.json())
      .catch(errorResponse => errorResponse.json())
  }

  const getSummary = async () => {
    setIsLoading(true)
    const url = serverUrl + '/uploadFile'
    const form = new FormData()
    form.append('file', document.getElementById('file').files[0])
    const fetchedSummary = await fetchSummary(url, form)
    setIsLoading(false)
    if (fetchedSummary.summary) {
      setError('')
      setSummary(fetchedSummary.summary)
    } else if (fetchedSummary.message) {
      setSummary('')
      setError(fetchedSummary.message)
    } else {
      setSummary('')
      setError('Something went wrong.')
    }
  }

  return (
    <Container className='app'>
      <Row>
        <Col sm={6}>
          <UploadHeader />
          <TextUploadForm
            uploadChoice={uploadChoice}
            text={text}
            handleRadioChange={e => handleUploadChoice(e)}
            handleTextChange={e => handleTextChange(e)}
            handleFileChange={() => setFileIsLoaded(!fileIsLoaded)}
            handleClick={() => getSummary()}
            isLoading={isLoading}
          />
        </Col>
        <Col sm={6}>
          <SummarySection summary={summary} error={error} />
        </Col>
      </Row>
    </Container>
  )
}

export default App
