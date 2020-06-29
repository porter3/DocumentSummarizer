import React, { useState, setState } from 'react'
import UploadSection from './components/UploadSection'
import TextUploadForm from './components/TextUploadForm'
import 'bootstrap/dist/css/bootstrap.min.css'
import './css/app.css'
import { Container } from 'react-bootstrap'

function App() {

  const [ isLoading, setIsLoading ] = useState(false)
  const [ text, setText ] = useState('')
  const [ fileIsLoaded, setFileIsLoaded ] = useState(false)

  const handleTextChange = e => {
    setText(e.target.value)
  }

  return (
    <Container className='app'>
      <UploadSection />
      <TextUploadForm handleTextChange={e => handleTextChange(e)} handleFileChange={() => setFileIsLoaded(!fileIsLoaded)} />
    </Container>
  )
}

export default App
