const isLocalBuild = false
const path = '/summarize'
const serverUrl = isLocalBuild ? 'http://localhost:5000' : 'http://documentsummarizer-env.eba-5pbvjawt.us-east-1.elasticbeanstalk.com'
export default serverUrl + path