from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize, sent_tokenize
from nltk.stem import PorterStemmer

# random generated paragraph from randomwordgenerator.com
text = "He took a sip of the drink. He wasn't sure whether he liked it or not, but at this moment it didn't matter. She had made it especially for him so he would have forced it down even if he had absolutely hated it. That's simply the way things worked. She made him a new-fangled drink each day and he took a sip of it and smiled, saying it was excellent."

def create_frequency_table(text) -> dict:
    # generally speaking, stop words are filler words (https://en.wikipedia.org/wiki/Stop_words)
    stop_words = set(stopwords.words("english"))
    # create a list of word tokens
    words = word_tokenize(text)
    # create object to get word stems (e.g. laughing -> laugh)
    stemmer = PorterStemmer()

    # create table containing the frequencies of word stems for non-stop words
    frequency_table = dict()
    for word in words:
        word = stemmer.stem(word)
        if word in stop_words:
            continue
        if word in frequency_table:
            frequency_table[word] += 1
        else:
            frequency_table[word] = 1

    return frequency_table


def score_sentences(sentences, frequency_table) -> dict:
    sentence_scores = dict()

    for sentence in sentences:
        sentence_word_count = len(word_tokenize(sentence))
        # don't need the whole sentence - they only need to act as keys
        abbrv_sentence = sentence[:10]
        for word in frequency_table:
            if word in sentence.lower():
                # add the score of the word in the frequency table to a sentence's score
                if abbrv_sentence in sentence_scores:
                    sentence_scores[abbrv_sentence] += frequency_table[word]
                else:
                    sentence_scores[abbrv_sentence] = frequency_table[word]
        """
        Dividing a sentence by its word count ensures it won't get a higher score for being longer.
        Multiplying it by 10 prior to ensures that the scores of strings with fewer sentences don't have a spread that's too small.
        """
        sentence_scores[abbrv_sentence] = sentence_scores[abbrv_sentence] * 10 // sentence_word_count

    return sentence_scores


def main():
    frequency_table = create_frequency_table(text)
    sentences = sent_tokenize(text)
    sentence_scores = score_sentences(sentences, frequency_table)


if __name__ == '__main__':
    main()