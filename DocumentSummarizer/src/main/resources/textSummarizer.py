from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize, sent_tokenize
from nltk.stem import PorterStemmer
from enchant import Dict
from sys import argv, stdin
from math import ceil

ATTR_DELIMITER = '-::-'
SENTENCE_DELIMITER = '-:::-'
SUMMARY_COUNT_DELIMITER = '-::::-'
# want no more than 20 summaries right now
SUMMARY_MAX = 20
NO_VALID_WORDS_MSG = "NO_VALID_WORDS_ERROR"
GENERIC_MSG = "GENERIC_ERROR"

file_path = r"C:\Users\jake\Downloads\TheCrucibleFullText.txt"

class SummarySentence():
    def set_adjusted_score(self, adjusted_score):
        self.adjusted_score = adjusted_score
    
    def __init__(self, score, sentence, order_placement):
        self.score = score
        self.sentence = sentence
        self.order_placement = order_placement
        self.adjusted_score = None
    
    def __str__(self):
        return str(self.order_placement) + ATTR_DELIMITER + str(self.adjusted_score) + ATTR_DELIMITER + self.sentence


def get_text_from_txt_file():
    text = ''
    with open(file_path,"r", encoding='utf-8') as f:
        text = f.read()
    return text

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
    sentences = list()
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
    print(average)
    return average


def get_sentence_objects_above_threshold(sentences, sentence_scores, threshold) -> list:
    sentence_objects = list()
    scores_above_threshold = list()
    order_index = 0
    for sentence in sentences:
        abbrv_sentence = sentence[:10]
        if sentence_scores[abbrv_sentence] > threshold:
            # construct with score, sentence, order_placement
            sentence_objects.append(SummarySentence(sentence_scores[abbrv_sentence], sentence, order_index))
            scores_above_threshold.append(sentence_scores[abbrv_sentence])
            order_index += 1

    # get adjusted scores
    adjusted_scores, max_adjusted_score = get_adjusted_scores(scores_above_threshold)
    # sort sentence objects by score
    sentence_objects_by_score_asc = sorted(sentence_objects, key=lambda s: s.score)
    # iterate over sentence objects and add adjusted score
    adj_score_index = 0
    for obj in sentence_objects_by_score_asc:
        obj.set_adjusted_score(adjusted_scores[adj_score_index])
        adj_score_index += 1
    # return re-sorted sentence objects by order_placement
    return sorted(sentence_objects_by_score_asc, key=lambda s: s.order_placement), max_adjusted_score


def get_adjusted_scores(scores_above_threshold):
    scores_above_threshold_count = len(scores_above_threshold)
    adjusted_scores = list()
    # get step (value with which to increase the assigned score)
    step = 1 if scores_above_threshold_count < SUMMARY_MAX else SUMMARY_MAX / scores_above_threshold_count
    adjusted_score = 0
    for scores in scores_above_threshold:
        adjusted_scores.append(adjusted_score)
        adjusted_score += step
    # get the max score of the sentences rounded up - due to the scoring algorithm, the scoring may cause the most verbose summary to not be SUMMARY_MAX sentences 100% of the time, even if more than SUMMARY_MAX sentences are included
    max_adjusted_score = max(adjusted_scores)
    return adjusted_scores, max_adjusted_score


def get_summary_count(sentence_objects, max_adjusted_score) -> int:
    sentence_count = len(sentence_objects)
    if sentence_count > SUMMARY_MAX:
        return ceil(max_adjusted_score)
    else:
        return sentence_count



def main():
        text = get_text_from_stdin()
        try:
            frequency_table = create_frequency_table(text)
        except NoValidWordsException as e:
            print(NO_VALID_WORDS_MSG)
            return
        sentences = get_sentences(text)
        sentence_scores = score_sentences(sentences, frequency_table)
        avg_score = find_average_score(sentence_scores)
        # construct sentence objects with all objects above threshold
        sentence_objects, max_adjusted_score = get_sentence_objects_above_threshold(sentences, sentence_scores, avg_score)
        # get max adjusted score to know for sure how many summaries there will be
        summary_count = get_summary_count(sentence_objects, max_adjusted_score)
        print(str(summary_count) + SUMMARY_COUNT_DELIMITER)
        for obj in sentence_objects:
            print(str(obj) + SENTENCE_DELIMITER)


if __name__ == '__main__':
    main()