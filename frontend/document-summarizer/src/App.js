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
  const [ summaries, setSummaries ] = useState({})
  const [ errorMessage, setErrorMessage ] = useState('')
  
  const handleUploadChoice = e => {
    setUploadChoice(e.target.value)
  }

  const handleTextChange = e => {
    setText(e.target.value)
  }

  const fetchSummaries = (url, body) => {
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
    setSummaries({})
    setErrorMessage('')
    let url, body
    if (uploadChoice === 'fileUpload') {
      url = serverUrl + '/file'
      body = new FormData()
      body.append('file', document.getElementById('file').files[0])
    } else {
      url = serverUrl + '/text'
      body = text
    }
    const fetchedSummaries = await fetchSummaries(url, body)
    console.log(fetchedSummaries)
    setIsLoading(false)
    if (fetchedSummaries.summaries) {
      setErrorMessage('')
      setSummaries(fetchedSummaries.summaries)
    } else if (fetchedSummaries.message) {
      setSummaries({})
      setErrorMessage(fetchedSummaries.summaries)
    } else {
      setSummaries({})
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
            summaries={summaries}
            errorMessage={errorMessage}
            isLoading={isLoading}
          />
        </Col>
      </Row>
    </Container>
  )
}

export default App
