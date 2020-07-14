#!/usr/bin/env python
from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize, sent_tokenize
from nltk.stem import PorterStemmer
from sys import argv, stdin


DELIMITER = argv[1]


def get_text_from_stdin() -> str:
    text = ''
    for line in stdin:
        text += line
    return text


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


def get_multipliers(sentences) -> list:
    multipliers = []
    multiplier = 0.8
    step = 5
    for i in range(0, len(sentences), step):
        multipliers.append(multiplier)
        multiplier += 0.2
    return multipliers


def main():
    try:
        text = get_text_from_stdin()
        frequency_table = create_frequency_table(text)
        sentences = sent_tokenize(text)
        sentence_scores = score_sentences(sentences, frequency_table)
        threshold = find_average_score(sentence_scores)
        threshold_multipliers = get_multipliers(sentences)
        for multiplier in threshold_multipliers:
            print(generate_summary(sentences, sentence_scores, threshold * multiplier) + DELIMITER)
    except:
        print("Something went wrong with executing the Python script.")


if __name__ == '__main__':
    main()