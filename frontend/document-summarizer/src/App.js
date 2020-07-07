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
  const [ errorMessage, setErrorMessage ] = useState('')
  
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
      .catch(errorResponse => {
        let error = {}
        try {
          error = errorResponse.json()
        } catch (e) {
          if (e instanceof TypeError) {
            error.message = 'There was a TypeError. Please tell Jake so he can fix it.'
          } else {
            error.message = 'There was an error. Please tell Jake so he can fix it.'
          }
        }
        return error
      })
  }

  const getSummary = async () => {
    setIsLoading(true)
    let url, body
    if (uploadChoice === 'fileUpload') {
      url = serverUrl + '/file'
      body = new FormData()
      body.append('file', document.getElementById('file').files[0])
    } else {
      url = serverUrl + '/text'
      body = text
    }
    const fetchedSummary = await fetchSummary(url, body)
    setIsLoading(false)
    if (fetchedSummary.summary) {
      setErrorMessage('')
      setSummary(fetchedSummary.summary)
    } else if (fetchedSummary.message) {
      setSummary('')
      setErrorMessage(fetchedSummary.message)
    } else {
      console.log(fetchedSummary)
      setSummary('')
      setErrorMessage('Something went wrong.')
    }
  }

  return (
    <Container className='app'>
      <Row>
        <Col md={6} xs={12}>
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
        <Col md={6} xs={12}>
          <SummarySection
            summary={summary}
            errorMessage={errorMessage}
            isLoading={isLoading}
          />
        </Col>
      </Row>
    </Container>
  )
}

export default App
