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
  /*
    previousSummary/previousError are workarounds for rendering the Typist component (in SummarySection) properly if one summary has already been generated.
    Don't know why it's necessary, likely a bug with the Typist component.
  */
  const [ previousSummary, setPreviousSummary] = useState('')
  const [ error, setError ] = useState('')
  const [ previousError, setPreviousError ] = useState('')
  
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
    if (summary) {
      setPreviousSummary(summary)
    }
    if (error) {
      setPreviousError(error)
    }
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
      setError('')
      setPreviousError('') // see comment below as to why this line is here
      setSummary(fetchedSummary.summary)
    } else if (fetchedSummary.message) {
      setSummary('')
      /* 
        setPreviousSummary needs to be done in case a user tries to summarize a text,
        gets an error on the next summarization attempt, and then tries to summarize the first text
      */
      setPreviousSummary('')
      setError(fetchedSummary.message)
    } else {
      setSummary('')
      setError('Something went wrong.')
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
            previousSummary={previousSummary}
            error={error}
            previousError={previousError}
            isLoading={isLoading}
          />
        </Col>
      </Row>
    </Container>
  )
}

export default App
