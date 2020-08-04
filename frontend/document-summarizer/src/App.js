import React, { useState } from 'react'
// my components
import AboutAppPopover from './components/AboutAppPopover'
import DarkModeSwitch from './components/DarkModeSwitch'
import UploadHeader from './components/UploadHeader'
import TextUploadForm from './components/TextUploadForm'
import GenerateButton from './components/GenerateButton'
import SummarySection from './components/SummarySection'
import SummaryLengthSlider from './components/SummaryLengthSlider'
// other components
import Container from 'react-bootstrap/Container'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import Paper from '@material-ui/core/Paper'
import { ThemeProvider } from '@material-ui/core/styles'
// css/styling
import 'bootstrap/dist/css/bootstrap.min.css'
import './css/app.css'
import createTheme from './muiTheme'
// functions
import getLoaderMessage from './getLoaderMessage'
import validateSentenceCount from './validateSentenceCount'
// objects
import serverUrl from './serverUrl'
import supportedFileFormats from './supportedFileFormats'


function App() {

  const [ isDarkModeEnabled, setIsDarkModeEnabled ] = useState(false)
  const [ isLoading, setIsLoading ] = useState(false)
  const [ loaderMessage, setLoaderMessage ] = useState('')
  const [ uploadChoice, setUploadChoice ] = useState('')
  const [ fileInfo, setFileInfo ] = useState({
    file: {
      name: '',
      extension: '',
      sizeInBytes: 0
    }
  })
  const [ text, setText ] = useState('')
  const [ summaries, setSummaries ] = useState([])
  const [ errorMessage, setErrorMessage ] = useState('')
  const [ summaryChoice, setSummaryChoice ] = useState(0)

  const isBadExtension = !supportedFileFormats.includes(fileInfo.file.extension) && fileInfo.file.extension !== ''
  const isTooLargeFile = fileInfo.file.sizeInBytes > 5242880
  const theme = createTheme(isDarkModeEnabled)
  
  const toggleDarkMode = e => {
    setIsDarkModeEnabled(e.target.checked)
  }

  const handleUploadChoice = e => {
    setUploadChoice(e.target.value)
  }

  const handleFileChange = e => {
    if (e.target.files[0]) {
      const extension = getFileExtension(e.target.value)
      const fileName = e.target.files[0].name
      const fileSize = e.target.files[0].size
      setFileInfo(() => ({
        file: {
          name: fileName,
          extension: extension,
          sizeInBytes: fileSize
        }
      }))
    } else {
      setFileInfo(() => ({
        file: {
          name: '',
          extension: '',
          sizeInBytes: 0
        }
      }))
    }
  }

  const getFileExtension = filePath => filePath.slice((Math.max(0, filePath.lastIndexOf('.')) || Infinity) + 1).toLowerCase()

  const handleTextChange = e => {
    setText(e.target.value)
  }

  const handleSliderChange = (e, value) => {
    setSummaryChoice(value)
  }

  const handleGenerateButtonClick = () => {
    if (uploadChoice === 'text') {
      if (!validateSentenceCount(text, setSummaries, setErrorMessage)) {
        return
      }
    }
    getSummaries()
  }

  const handleClearButtonClick = () => {
    setText('')
  }

  const changeLoaderMessage = () => {
    setTimeout(() => { setLoaderMessage(getLoaderMessage(fileInfo.file.sizeInBytes)) }, 4000)
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

  const getSummaries = async () => {
    setSummaries([])
    setSummaryChoice(0)
    setErrorMessage('')
    setIsLoading(true)
    changeLoaderMessage()
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
    setLoaderMessage('')
    setIsLoading(false)
    if (fetchedSummaries.summaries) {
      setErrorMessage('')
      setSummaries(fetchedSummaries.summaries)
    } else if (fetchedSummaries.message) {
      setSummaries([])
      setErrorMessage(fetchedSummaries.message)
    } else {
      setSummaries([])
      setErrorMessage('Something went wrong.')
    }
  }

  return (
    <ThemeProvider theme={theme}>
      <Paper style={{ height: '110vh', boxShadow: 'none' }}>
        <Container fluid id='app'>
          <Row>
            <Col xs={12}>
              <AboutAppPopover />
              <DarkModeSwitch
                isDarkModeEnabled={isDarkModeEnabled}
                handleChange={e => toggleDarkMode(e)} />
            </Col>
          </Row>
          <Row>
            <Col md={4} xs={10}>
              <UploadHeader />
              <TextUploadForm
                theme={theme}
                uploadChoice={uploadChoice}
                text={text}
                fileExtension={fileInfo.file.extension}
                isBadExtension={isBadExtension}
                fileName={fileInfo.file.name}
                isTooLargeFile={isTooLargeFile}
                isDarkModeEnabled={isDarkModeEnabled}
                handleRadioChange={e => handleUploadChoice(e)}
                handleTextChange={e => handleTextChange(e)}
                handleFileChange={e => handleFileChange(e)}
                handleClick={() => handleClearButtonClick()}
              />
            </Col>
            <Col xs={2}>
              <GenerateButton
                isLoading={isLoading}
                fileExtension={fileInfo.file.extension}
                isBadExtension={isBadExtension}
                isTooLargeFile={isTooLargeFile}
                handleClick={() => handleGenerateButtonClick()}
              />
            </Col>
            <Col md={6} xs={12}>
              <SummarySection
                summaries={summaries}
                errorMessage={errorMessage}
                isLoading={isLoading}
                summaryChoice={summaryChoice}
                loaderMessage={loaderMessage}
              />
              {summaries.length > 1 &&
                <SummaryLengthSlider
                  handleChange={handleSliderChange}
                  max={summaries.length - 1}
                />
              }
            </Col>
          </Row>
        </Container>
      </Paper>
    </ThemeProvider>
  )
}

export default App
