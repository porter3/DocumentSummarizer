#!/usr/bin/env python
from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize, sent_tokenize
from nltk.stem import PorterStemmer
from sys import argv
import sysconfig

# random generated paragraph from randomwordgenerator.com
# text = """One of the greatest aspects of NLP is that is stretches across multiple areas of computational studies from artificial intelligence to computational linguistics all studying the interactions between computers and the language of humans. It is primarily concerned with programming computers to accurately and quickly process large amounts of natural language corpora. What are natural language corpora? It is the study of language as expressed by real-world languages. It is a comprehensive approach to understanding a set of abstract rules from a text and the relationship that language has with another.
# While NLP has become even more present with the computer industrial revolution in the modern era it was actually the brain child of the amazing Alan Turing who along with helping to crack the German Enigma Coding machine also wrote an article titled “Computing Machinery and Intelligence” which proposed the first serious use of relating humans language to computers. With the ever present nature of technology in our daily lives we have seen the emergence of just how influential NLP can be to our daily lives through revolutionary tools like Google Translate, IBM Watson, Speech Recognition and generation and sentiment analysis.
# However, like all things there are a few areas of concern and disadvantages of NLP. It struggles to generate language that would naturally flow as a person talks similar to when you read a bad movie script and it sounds like a computer talking. While there are methods to attempt to understand changes in tone NLP continues to struggle with understanding things like sarcasm and detecting things like humor. However, this is an area of much study that I look forward to the day that the "sarcasm breakthrough" occurs. If for nothing else, then to better understand the occasional text or instant message from a friend."""

# threshold is the score a sentence has to pass to make it into the summary
threshold_multiplier = 1

def get_text_from_args() -> str:
    textChunks = argv[1:]
    return "".join(textChunks)


def create_frequency_table(text) -> dict:
    # generally speaking, stop words are filler words (https://en.wikipedia.org/wiki/Stop_words)
    stop_words = set(stopwords.words("english"))
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


# find average score of the sentences to use as a threshold
def find_average_score(sentence_scores) -> int:
    sum = 0
    for entry in sentence_scores:
        sum += sentence_scores[entry]

    average = int(sum / len(sentence_scores))
    return average


def generate_summary(sentences, sentence_scores, threshold) -> str:
    summary = ''

    for sentence in sentences:
        abbrv_sentence = sentence[:10]
        if abbrv_sentence in sentence_scores and sentence_scores[abbrv_sentence] > threshold:
            summary += ' ' + sentence

    return summary


def main():
    try:
        text = get_text_from_args()
        frequency_table = create_frequency_table(text)
        sentences = sent_tokenize(text)
        sentence_scores = score_sentences(sentences, frequency_table)
        threshold = find_average_score(sentence_scores)
        summary = generate_summary(sentences, sentence_scores, threshold * threshold_multiplier)
        print(summary)
    except:
        print("Something went wrong with executing the Python script.")


if __name__ == '__main__':
    main()