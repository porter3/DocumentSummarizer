import React, { useState, useEffect } from 'react'
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
import './styling/css/app.css'
import createTheme from './styling/muiTheme'
// functions
import changeLoaderMessage from './misc/changeLoaderMessage'
import validateSentenceCount from './validation/validateSentenceCount'
// objects
import serverUrl from './misc/serverUrl'
import supportedFileFormats from './validation/supportedFileFormats'


function App() {

  const [ isDarkModeEnabled, setIsDarkModeEnabled ] = useState(localStorage.getItem('isDarkModeEnabled') === 'true' || false)
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
  const [ sentences, setSentences ] = useState([])
  const [ errorMessage, setErrorMessage ] = useState('')
  const [ sentenceThreshold, setSentenceThreshold ] = useState(0)
  const [ summaryCount, setSummaryCount ] = useState(0)

  const isBadExtension = !supportedFileFormats.includes(fileInfo.file.extension) && fileInfo.file.extension !== ''
  const isTooLargeFile = fileInfo.file.sizeInBytes > 5242880
  const theme = createTheme(isDarkModeEnabled)

  useEffect(() => {
    localStorage.setItem('isDarkModeEnabled', isDarkModeEnabled)
  }, [isDarkModeEnabled])
  
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
    setSentenceThreshold(value)
  }

  const handleGenerateButtonClick = () => {
    if (uploadChoice === 'text') {
      if (!validateSentenceCount(text, setSentences, setErrorMessage)) {
        return
      }
    }
    getSummaryData()
  }

  const handleClearButtonClick = () => {
    setText('')
  }

  const fetchSummaryData = (url, body) => {
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

  const getSummaryData = async () => {
    setSentences([])
    setSentenceThreshold(0)
    setErrorMessage('')
    setIsLoading(true)
    changeLoaderMessage(setLoaderMessage, fileInfo.file.sizeInBytes)
    let url, body
    if (uploadChoice === 'fileUpload') {
      url = serverUrl + '/file'
      body = new FormData()
      body.append('file', document.getElementById('file').files[0])
    } else {
      url = serverUrl + '/text'
      body = text
    }
    const summaryData = await fetchSummaryData(url, body)
    console.log(summaryData)
    setLoaderMessage('')
    setIsLoading(false)
    if (summaryData.sentences) {
      setErrorMessage('')
      setSummaryCount(summaryData.summaryCount)
      setSentences(summaryData.sentences)
    } else if (summaryData.message) {
      setSentences([])
      setErrorMessage(summaryData.message)
    } else {
      setSentences([])
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
                uploadChoice={uploadChoice}
                handleClick={() => handleGenerateButtonClick()}
              />
            </Col>
            <Col md={6} xs={12}>
              <SummarySection
                sentences={sentences}
                errorMessage={errorMessage}
                isLoading={isLoading}
                sentenceThreshold={sentenceThreshold}
                loaderMessage={loaderMessage}
              />
              {sentences.length > 1 &&
                <SummaryLengthSlider
                  handleChange={handleSliderChange}
                  max={summaryCount - 1}
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
