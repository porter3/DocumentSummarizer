from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize, sent_tokenize
from nltk.stem import PorterStemmer
from enchant import Dict
from sys import argv, stdin

# DELIMITER = ':::'
DELIMITER = argv[1]
NO_VALID_WORDS_MSG = "NO_VALID_WORDS_ERROR"
GENERIC_MSG = "GENERIC_ERROR"

def get_text_from_stdin() -> str:
    # text = input()
    text = ''
    for line in stdin:
        text += line
    return text


def check_if_valid_word_exists(words) -> bool:
    dictionary = Dict("en_US")
    for word in words:
        if dictionary.check(word) == True and len(word) > 1:
            return True
    raise NoValidWordsException()


def create_frequency_table(text) -> dict:
    words = word_tokenize(text)
    # check if a single word is valid - if it does, can be reasonably certain it's not a non-text file with a .txt extension
    check_if_valid_word_exists(words)
    # generally speaking, stop words are filler words (https://en.wikipedia.org/wiki/Stop_words)
    stop_words = set(stopwords.words("english"))

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


def get_sentences(text) -> list:
    unfiltered_sentences = sent_tokenize(text)
    sentences = []
    # Only add sentence if it's at least 4 characters ("I am" is shortest possible English sentence) - sent_tokenize() isn't smart enough on its own
    # Avoid repeat sentences
    for sentence in unfiltered_sentences:
        if len(sentence) >= 4 and sentence not in sentences:
            sentences.append(sentence)
    return sentences


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
    return summary.replace('\n', ' - ')


def get_multipliers(sentences) -> list:
    multipliers = []
    multiplier = 0.8
    step = 4
    for i in range(0, len(sentences), step):
        multipliers.append(multiplier)
        multiplier += 0.115
    return multipliers


def main():
    try:
        text = get_text_from_stdin()
        try:
            frequency_table = create_frequency_table(text)
        except NoValidWordsException as e:
            print(NO_VALID_WORDS_MSG)
            return
        sentences = get_sentences(text)
        sentence_scores = score_sentences(sentences, frequency_table)
        threshold = find_average_score(sentence_scores)
        threshold_multipliers = get_multipliers(sentences)
        for multiplier in threshold_multipliers:
            summary = generate_summary(sentences, sentence_scores, threshold * multiplier)
            # due to the first threshold multiplier being 0.8, it's possible for a summary to the same as the text if the text is particularly short
            if summary != text:
                print(summary + DELIMITER)
    except:
        print(GENERIC_MSG)


if __name__ == '__main__':
    main()