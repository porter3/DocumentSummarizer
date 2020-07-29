const localBuild = true
const path = '/summarize'
const serverUrl = localBuild ? 'http://localhost:5000' : 'http://documentsummarizer-env.eba-5pbvjawt.us-east-1.elasticbeanstalk.com'
export default serverUrl + path