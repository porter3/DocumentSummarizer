import React, { useState, useEffect } from 'react'
// my components
import AboutAppPopover from './components/AboutAppPopover'
import DarkModeSwitch from './components/DarkModeSwitch'
import UploadHeader from './components/UploadHeader'
import LanguageSelector from './components/LanguageSelector'
import TextUploadForm from './components/TextUploadForm'
import GenerateButton from './components/GenerateButton'
import SummarySection from './components/SummarySection'
import SummaryLengthSlider from './components/SummaryLengthSlider'
// other components
import Paper from '@material-ui/core/Paper'
import { ThemeProvider } from '@material-ui/core/styles'
// css/styling
import './styling/css/app.css'
import './styling/css/flexLayout.css'
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
  const [ language, setLanguage ] = useState('en')
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

  const handleLanguageChange = e => {
    setLanguage(e.target.value)
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

  const clearLoaderMessageQueue = timeouts => {
    timeouts.forEach(timeout => {
      window.clearTimeout(timeout)
    })
  }

  const handleNetworkErrors = response => {
    let errorMessage
    if (response.message === 'Failed to fetch') {
      errorMessage = 'A network error occurred.'
    } else {
      errorMessage = 'A TypeError occurred. Please tell Jake so he can fix it.' 
    }
    return errorMessage
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
            error.message = handleNetworkErrors(errorResponse)
          } else {
            error.message = 'An error occurred. Please tell Jake so he can fix it.'
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
    const timeouts = changeLoaderMessage(setLoaderMessage, fileInfo.file.sizeInBytes)
    let url, body
    if (uploadChoice === 'fileUpload') {
      url = `${serverUrl}/file?language=${language}`
      body = new FormData()
      body.append('file', document.getElementById('file').files[0])
    } else {
      url = `${serverUrl}/text?language=${language}`
      body = text
    }
    const summaryData = await fetchSummaryData(url, body)
    clearLoaderMessageQueue(timeouts)
    setLoaderMessage('')
    setIsLoading(false)
    if (!summaryData.sentences && !summaryData.message) {
      setSentences([])
      setErrorMessage('Something went wrong.')
    } else if (summaryData.sentences) {
      setErrorMessage('')
      setSummaryCount(summaryData.summaryCount)
      setSentences(summaryData.sentences)
    } else if (summaryData.message) {
      setSentences([])
      setErrorMessage(summaryData.message)
    }
  }

  return (
    <ThemeProvider theme={theme}>
      <Paper style={{ height: '100vh', boxShadow: 'none' }}>
        <div className='flexContainer'>
          <div className='leftCol'>
            <div className='headerRow'>
              <div className='aboutPopover'>
                <AboutAppPopover />
              </div>
              <div className='darkSwitch'>
                <DarkModeSwitch
                  isDarkModeEnabled={isDarkModeEnabled}
                  handleChange={e => toggleDarkMode(e)} />
                </div>
            </div>
            <div className='headingRow'>
              <UploadHeader />
            </div>
            <div className='langGenerateButtonRow'>
              <div>
                <LanguageSelector 
                  language={language}
                  handleChange={e => handleLanguageChange(e)}
                />
              </div>
              <div className='generateButtonCol'>
                <GenerateButton
                  isLoading={isLoading}
                  fileExtension={fileInfo.file.extension}
                  isBadExtension={isBadExtension}
                  isTooLargeFile={isTooLargeFile}
                  uploadChoice={uploadChoice}
                  handleClick={() => handleGenerateButtonClick()}
                />
              </div>
            </div>
            <div className='formRow'>
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
            </div>
          </div>
          <div className='rightCol'>
            <SummarySection
              sentences={sentences}
              errorMessage={errorMessage}
              isLoading={isLoading}
              sentenceThreshold={sentenceThreshold}
              loaderMessage={loaderMessage}
              handleSliderChange={handleSliderChange}
              maxSummaries={summaryCount - 1}
            />
          </div>
        </div>
      </Paper>
    </ThemeProvider>
  )
}

export default App
