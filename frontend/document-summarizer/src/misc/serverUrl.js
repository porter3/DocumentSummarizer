const isLocalBuild = true
const path = '/summarize'
const serverUrl = isLocalBuild ? 'http://localhost:5000' : 'http://jar-java11-0421.eba-5pbvjawt.us-east-1.elasticbeanstalk.com'
export default serverUrl + path