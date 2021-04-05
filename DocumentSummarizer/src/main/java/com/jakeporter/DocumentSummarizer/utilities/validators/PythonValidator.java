package com.jakeporter.DocumentSummarizer.utilities.validators;

import com.jakeporter.DocumentSummarizer.exceptions.SummaryException;
import com.jakeporter.DocumentSummarizer.utilities.scriptRunners.JARScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class PythonValidator implements TextValidator {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private boolean IS_PRODUCTION_BUILD = true;
    private final String PYTHON_CMD = IS_PRODUCTION_BUILD ? "python3" : "python";
    private static final String SCRIPT = "textValidator.py";
    private static final String MIN_SENTENCE_COUNT = "4"; // needs to be a String
    private static final String sep = File.separator;
    private static final String ldProfileProdPath = sep + "usr" + sep + "local" + sep + "lib" + sep + "python3.7" + sep + "site-packages" + sep + "langdetect" + sep +  "profiles";
    private static final String ldProfileLocalPath = "C:" + sep + "Python39" + sep + "Lib" + sep + "site-packages" + sep + "langdetect" + sep + "profiles";
    private final String LANGDETECT_PROFILE = IS_PRODUCTION_BUILD ? ldProfileProdPath : ldProfileLocalPath;
    private static final String OUTPUT_DELIMITER = "-:::-";

    // TODO here and in PythonSummarizer
//    public PythonValidator() {
//        this.IS_PRODUCTION_BUILD = Boolean.parseBoolean(new Properties().getProperty("is.production.build"));
//    }

    public void validateText(String text, String language) {
        logger.info("PythonValidator LINE 31: Validation starting.");
        JARScriptRunner scriptRunner = new JARScriptRunner(PYTHON_CMD, SCRIPT);
        logger.info("PythonValidator LINE 33: Running script now.");
        String result = scriptRunner.runPythonScript(text, language, MIN_SENTENCE_COUNT, LANGDETECT_PROFILE, OUTPUT_DELIMITER);
        logger.info("PythonValidator LINE 33: Text validation complete.");
        logger.info("Validation message: " + result);
        checkErrorMessages(result);
    }

    private void checkErrorMessages(String result) {
        final String MISMATCHED_LANGUAGE_ERROR = "MISMATCHED_LANGUAGE_ERROR";
        final String UNKNOWN_LANGUAGE_ERROR = "UNKNOWN_LANGUAGE_ERROR";
        final String UNSUPPORTED_LANGUAGE_ERROR = "UNSUPPORTED_LANGUAGE_ERROR";
        final String SENTENCE_COUNT_ERROR = "SENTENCE_COUNT_ERROR";

        String[] resultComponents = result.split(OUTPUT_DELIMITER);

        String error = resultComponents[0];
        if (error.equals(UNKNOWN_LANGUAGE_ERROR)) {
            final String UNKNOWN_LANGUAGE_MSG = "The text's language is either unknown or too lingually ambiguous. Please upload a different text and try again.";
            throw new SummaryException(UNKNOWN_LANGUAGE_MSG);
        }
        if (error.equals(MISMATCHED_LANGUAGE_ERROR)) {
            String detectedLang = resultComponents[1];
            String selectedLang = resultComponents[2];
            final String MISMATCHED_LANGUAGE_MSG =
                    "The language you selected is " + selectedLang + " but it looks like the language used in the text is " + detectedLang + ". " +
                    "Please ensure the selected language appropriately matches your text.";
            throw new SummaryException(MISMATCHED_LANGUAGE_MSG);
        }
        if (error.equals(UNSUPPORTED_LANGUAGE_ERROR)) {
            String detectedLang = resultComponents[1];
            String selectedLang = resultComponents[2];
            final String UNSUPPORTED_LANGUAGE_MSG =
                    "The language you selected is " + selectedLang + " but it looks like the language used in the text is " + detectedLang +
                            ", which is not supported.";
            throw new SummaryException(UNSUPPORTED_LANGUAGE_MSG);
        }
        if (error.equals(SENTENCE_COUNT_ERROR)) {
            String sentenceCount = resultComponents[1];
            final String SENTENCE_COUNT_MSG =
                    "A text must have at least " + MIN_SENTENCE_COUNT + " sentences. " +
                    "The text submitted contains " + sentenceCount + " sentences.";
            throw new SummaryException(SENTENCE_COUNT_MSG);
        }
    }
}
