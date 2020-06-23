from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize
from nltk.stem import PorterStemmer

# random generated paragraph from randomwordgenerator.com
text = "He took a sip of the drink. He wasn't sure whether he liked it or not, but at this moment it didn't matter. She had made it especially for him so he would have forced it down even if he had absolutely hated it. That's simply the way things worked. She made him a new-fangled drink each day and he took a sip of it and smiled, saying it was excellent."

# function must return a dictionary
def create_frequency_table(text) -> dict:
    # generally speaking, stop words are filler words (https://en.wikipedia.org/wiki/Stop_words)
    stopWords = set(stopwords.words("english"))
    # create a list of word tokens
    words = word_tokenize(text)
    # create object to get word stems (e.g. laughing -> laugh)
    stemmer = PorterStemmer()

    # create table containing the frequencies of word stems for non-stop words
    frequencyTable = dict()
    for word in words:
        word = stemmer.stem(word)
        if word in stopWords:
            continue
        if word in frequencyTable:
            frequencyTable[word] += 1
        else:
            frequencyTable[word] = 1

    return frequencyTable