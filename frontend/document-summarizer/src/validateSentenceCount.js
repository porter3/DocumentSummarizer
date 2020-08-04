export default function validateSentenceCount(text, setSummaries, setErrorMessage) {
    if (text === '') {
        setSummaries([])
        setErrorMessage("Please enter some text to summarize.")
        return false
    }
    const sentenceCount = text.match(/[\w|)][.?!](\s|$)/g).length
    const minSentenceCount = 4
    if (sentenceCount < minSentenceCount) {
        const sentenceWord = sentenceCount === 1 ? 'sentence' : 'sentences'
        setSummaries([])
        setErrorMessage("It's a little worrisome that you need a tool to summarize only " +
        sentenceCount + " " + sentenceWord + ". Please enter at least " + minSentenceCount + " sentences.")
    return false
    }
    return true
}