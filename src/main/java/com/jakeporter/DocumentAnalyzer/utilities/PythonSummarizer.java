package com.jakeporter.DocumentAnalyzer.utilities;


public class PythonSummarizer extends DocumentSummarizer {

    @Override
    protected String computeSummary(String text) {
        return runPythonScript(text);
    }

    private String runPythonScript(String text) {
        return "TODO";
    }
}
