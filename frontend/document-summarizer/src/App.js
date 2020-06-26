import React from 'react'
import UploadSection from './components/UploadSection'
import TextUploadForm from './components/TextUploadForm'
import 'bootstrap/dist/css/bootstrap.min.css';
import './css/app.css'
import { Container } from 'react-bootstrap'

function App() {
  return (
    <Container className='app'>
      <UploadSection />
      <TextUploadForm />
    </Container>
  );
}

export default App;
