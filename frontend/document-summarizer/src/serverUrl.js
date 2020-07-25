const localBuild = true
const path = '/summarize'
export const serverUrl = localBuild ? 'http://localhost:5000' + path : 'http://documentsummarizer-env.eba-5pbvjawt.us-east-1.elasticbeanstalk.com' + path