export default function validateSentenceCount(text, setSentences, setErrorMessage) {
    const sentenceRegex = text.match(/[\w|)][.?!](\s|$)/g)
    const minSentenceCount = 4
    if (sentenceRegex == null) {
        setSentences([])
        setErrorMessage("Please enter at least " + minSentenceCount + " sentences to summarize.")
        return false
    }
    const sentenceCount = sentenceRegex.length
    if (sentenceCount < minSentenceCount) {
        const sentenceWord = sentenceCount === 1 ? 'sentence' : 'sentences'
        setSentences([])
        setErrorMessage("You don't need a tool to summarize only " +
        sentenceCount + " " + sentenceWord + ". Please enter at least " + minSentenceCount + " sentences.")
        return false
    }
    return true
}