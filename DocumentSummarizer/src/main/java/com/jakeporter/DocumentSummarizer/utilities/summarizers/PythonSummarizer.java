package com.jakeporter.DocumentSummarizer.utilities.summarizers;

import com.jakeporter.DocumentSummarizer.exceptions.*;
import com.jakeporter.DocumentSummarizer.utilities.scriptRunners.JARScriptRunner;
import com.jakeporter.DocumentSummarizer.utilities.textExtractors.FileTextExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class PythonSummarizer extends DocumentSummarizer {

    private static final boolean IS_PRODUCTION_BUILD = true;
    private static final String PYTHON_CMD = IS_PRODUCTION_BUILD ? "python3" : "python";
    private static final String SCRIPT = "textSummarizer.py";
    private static final String SUMMARY_DELIMITER = ":::";

    public PythonSummarizer(){ super(); }

    public PythonSummarizer(FileTextExtractor extractor) {
        super(extractor);
    }

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected Set<String> computeSummaries(String text) {
        String summariesString = runPythonScript(text);
        handleResultIssues(summariesString);
        return new LinkedHashSet<>(Arrays.asList(summariesString.split(SUMMARY_DELIMITER)));
    }

    private String runPythonScript(String text)  {
        JARScriptRunner scriptRunner = new JARScriptRunner(PYTHON_CMD, SCRIPT);
        String result = scriptRunner.runPythonScript(text, SUMMARY_DELIMITER);
        return result;
    }

    private void handleResultIssues(String scriptOutput) {
        final String UTF8_VALIDATION_ERROR = "Non-UTF-8 code starting with";
        final String ENCHANT_ERROR = "enchant_dict_check: assertion 'g_utf8_validate(word, len, NULL)";
        final String NO_VALID_WORDS_ERROR = "NO_VALID_WORDS_ERROR";
        final String PY_COMPILER_ERROR = "Traceback (most recent call last):";
        final String GENERIC_ERROR = "GENERIC_ERROR";
        if (scriptOutput.isBlank()) {
            throw new SummaryException("The text you tried to summarize is either too short or too repetitive to do so.");
        }
        if (scriptOutput.contains(ENCHANT_ERROR) || scriptOutput.contains(UTF8_VALIDATION_ERROR) || scriptOutput.equals(NO_VALID_WORDS_ERROR)) {
            throw new SummaryException("There was a problem with your text. Ensure all characters in your text are UTF-8. " +
                    "If you uploaded a file, ensure it has the correct extension.");
        }
        if (scriptOutput.substring(0, 34).equals(PY_COMPILER_ERROR)) {
            throw new SummaryException("The text you tried to summarize doesn't summarize well.");
        }
        if (scriptOutput.equals(GENERIC_ERROR)) {
            throw new SummaryException("Something went wrong on our end.");
        }
    }

}
