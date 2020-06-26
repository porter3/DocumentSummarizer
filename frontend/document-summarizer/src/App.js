import React, { useState, setState } from 'react'
import UploadSection from './components/UploadSection'
import TextUploadForm from './components/TextUploadForm'
import 'bootstrap/dist/css/bootstrap.min.css'
import './css/app.css'
import { Container } from 'react-bootstrap'

function App() {

  const [ isLoading, setIsLoading ] = useState(false)
  const [ text, setText ] = useState('')
  const [ fileIsSelected, setFileIsSelected ] = useState(false)

  return (
    <Container className='app'>
      <UploadSection />
      <TextUploadForm />
    </Container>
  )
}

export default App
