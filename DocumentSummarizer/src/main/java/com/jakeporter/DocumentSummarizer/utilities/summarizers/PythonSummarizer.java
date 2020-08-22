package com.jakeporter.DocumentSummarizer.utilities.summarizers;

import com.jakeporter.DocumentSummarizer.domainEntities.SummaryComponents;
import com.jakeporter.DocumentSummarizer.domainEntities.SummarySentence;
import com.jakeporter.DocumentSummarizer.exceptions.*;
import com.jakeporter.DocumentSummarizer.utilities.scriptRunners.JARScriptRunner;
import com.jakeporter.DocumentSummarizer.utilities.textExtractors.FileTextExtractor;

import java.util.*;


public class PythonSummarizer extends DocumentSummarizer {

    private static final boolean IS_PRODUCTION_BUILD = false;
    private static final String PYTHON_CMD = IS_PRODUCTION_BUILD ? "python3" : "python";
    private static final String SCRIPT = "textSummarizer.py";
    private static final String ATTR_DELIMITER = "-::-";
    private static final String SENTENCE_DELIMITER = "-:::-";
    private static final String SUMMARY_COUNT_DELIMITER = "-::::-";

    public PythonSummarizer(){ super(); }

    public PythonSummarizer(FileTextExtractor extractor) {
        super(extractor);
    }

    @Override
    protected SummaryComponents computeSummaries(String text, String language) {
        String summaryString = runPythonScript(text, language);
        handleResultIssues(summaryString);
        // first element of script output is the number of summaries there will be
        List<String> splitSummaryString = Arrays.asList(summaryString.split(SUMMARY_COUNT_DELIMITER));
        int summaryCount = Integer.parseInt(splitSummaryString.get(0));
        Set<SummarySentence> sentences = populateSummarySentenceObjects(splitSummaryString.get(1));
        return new SummaryComponents(sentences, summaryCount);
    }

    private String runPythonScript(String text, String language)  {
        JARScriptRunner scriptRunner = new JARScriptRunner(PYTHON_CMD, SCRIPT);
        String result = scriptRunner.runPythonScript(text, language, ATTR_DELIMITER, SENTENCE_DELIMITER, SUMMARY_COUNT_DELIMITER);
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

    private Set<SummarySentence> populateSummarySentenceObjects(String sentencesString) {
        Set<SummarySentence> sentenceObjects = new LinkedHashSet<>();
        // get array of sentence object values
        String[] sentenceObjectStrings = sentencesString.split(SENTENCE_DELIMITER);
        // populate sentence objects with values and add to list
        for (int i = 0; i < sentenceObjectStrings.length; i++) {
            String[] attributes = sentenceObjectStrings[i].split(ATTR_DELIMITER);
            sentenceObjects.add(new SummarySentence(Integer.parseInt(attributes[0]), Double.parseDouble(attributes[1]), attributes[2]));
        }
        return sentenceObjects;
    }

}
