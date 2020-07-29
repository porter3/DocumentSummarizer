package com.jakeporter.DocumentSummarizer.utilities.summarizers;

import com.jakeporter.DocumentSummarizer.exceptions.*;
import com.jakeporter.DocumentSummarizer.utilities.scriptRunners.JARScriptRunner;
import com.jakeporter.DocumentSummarizer.utilities.textExtractors.FileTextExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class PythonSummarizer extends DocumentSummarizer {

    private static final boolean IS_PRODUCTION_BUILD = false;
    private static final String PYTHON_CMD = IS_PRODUCTION_BUILD ? "python3.7" : "python";
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
        final String PY_COMPILER_ERROR_LINE = "Traceback (most recent call last):";
        final String GENERIC_ERROR_LINE = "Something went wrong with executing the Python script.";
        if (scriptOutput.isBlank()) {
            throw new SummaryException("The text you tried to summarize is either too short or too repetitive to do so.");
        }
        if (scriptOutput.contains(ENCHANT_ERROR) || scriptOutput.contains(UTF8_VALIDATION_ERROR)) {
            throw new SummaryException("There was a problem with your text. Ensure all characters in your text are UTF-8. " +
                    "If you uploaded a file, ensure it has the correct extension.");
        }
        if (scriptOutput.equals(PY_COMPILER_ERROR_LINE)) {
            throw new SummaryException("The text you tried to summarize doesn't summarize well.");
        }
        if (scriptOutput.equals(GENERIC_ERROR_LINE)) {
            throw new SummaryException("Something went wrong on our end.");
        }
    }

}
