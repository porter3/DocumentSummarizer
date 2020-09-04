from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize, sent_tokenize
from langdetect import DetectorFactory
from sys import argv, stdin

LANGUAGE = argv[1]
MIN_SENTENCE_COUNT = int(argv[2])
WINDOWS_PROFILE_PATH = argv[3]
OUTPUT_DELIMITER = argv[4]
UNKNOWN_LANGUAGE_MSG = 'UNKNOWN_LANGUAGE_ERROR'
MISMATCHED_LANGUAGE_MSG = 'MISMATCHED_LANGUAGE_ERROR'
UNSUPPORTED_LANGUAGE_MSG = 'UNSUPPORTED_LANGUAGE_ERROR'
SENTENCE_COUNT_MSG = 'SENTENCE_COUNT_ERROR'
LANGUAGE_WORDS = {"af": "Afrikaans", "ar": "Arabic","bg": "Bulgarian", "bn": "Bengali", "ca": "Catalan", "cs": "Czech", "cy": "Welsh", "da": "Danish", "de": "German", "el": "Greek", "en": "English", "es": "Spanish", "et": "Estonian", "fa": "Persian", "fi": "Finnish", "fr": "French", "gu": "Gujarati", "he": "Hebrew"}
SUPPORTED_LANGUAGES = [
    "Arabic",
    "Danish",
    "German",
    "English",
    "Spanish",
    "Finnish"
]

class UnknownLanguageException(Exception):
    def __init__(self, message):
        self.message = message
    
    def __str__(self):
        return self.message


def get_text_from_stdin():
    stdin.reconfigure(encoding='utf-8')
    text = stdin.read()
    return text


# returns a list of Language objects - https://github.com/Mimino666/langdetect/blob/c4b28fe44370863eb6e2f73cfe0cfae5d5a895da/langdetect/language.py
def detect_languages(text, profilePath) -> list:
    DetectorFactory.seed = 0
    factory = DetectorFactory()
    factory.load_profile(profilePath)
    detector = DetectorFactory._create_detector(factory)
    detector.append(text)
    return detector.get_probabilities()


def validate_language_is_known(detected_languages):
    if detected_languages[0].lang not in LANGUAGE_WORDS:
        raise Exception(UNKNOWN_LANGUAGE_MSG)


def validate_language(detected_languages):
    # if detection algorithm isn't confident in the text's language
    if detected_languages[0].prob < 0.8:
        raise UnknownLanguageException(UNKNOWN_LANGUAGE_MSG)
    # if detected language isn't supported
    language_word = LANGUAGE_WORDS.get(detected_languages[0].lang)
    if language_word not in SUPPORTED_LANGUAGES:
            raise Exception(UNSUPPORTED_LANGUAGE_MSG)
    # if chosen language doesn't match what user selected
    if detected_languages[0].lang != LANGUAGE:
        raise Exception(MISMATCHED_LANGUAGE_MSG)


# will only return MIN_SENTENCE_COUNT or lower
def get_sentence_count(text) -> int:
    sentences = sent_tokenize(text)
    # Only count sentence if it's at least 4 characters
    sentenceCount = 0
    for sentence in sentences:
        if len(sentence) >= 4:
            sentenceCount += 1
        # Don't need to know the actual number of sentences, just that it hits the minimum
        if sentenceCount == MIN_SENTENCE_COUNT:
            break
    return sentenceCount


def validate_sentence_count(sentenceCount):
    if sentenceCount < MIN_SENTENCE_COUNT:
        raise Exception(SENTENCE_COUNT_MSG)


def main():
    text = get_text_from_stdin()
    # TODO: validate whether file is text or binary
    
    # validate text hits MIN_SENTENCE_COUNT
    sentenceCount = get_sentence_count(text)
    try:
        validate_sentence_count(sentenceCount)
    except:
        print(SENTENCE_COUNT_MSG + OUTPUT_DELIMITER + str(sentenceCount))

    detected_languages = detect_languages(text, WINDOWS_PROFILE_PATH)
    # ensure detected language is at least known (doesn't have to be supported)
    try:
        validate_language_is_known(detected_languages)
    except Exception as e:
        print(str(e) + OUTPUT_DELIMITER)
        return
    # ensure language user selected matches the text language
    try:
        validate_language(detected_languages)
    except UnknownLanguageException as e:
        print(str(e) + OUTPUT_DELIMITER)
        return
    except Exception as e:
        # print exception, the language of the text, and the full word of the language the user selected
        print(str(e) + OUTPUT_DELIMITER + LANGUAGE_WORDS.get(detected_languages[0].lang) + OUTPUT_DELIMITER + LANGUAGE_WORDS.get(LANGUAGE))
        return


if __name__ == '__main__':
    main()