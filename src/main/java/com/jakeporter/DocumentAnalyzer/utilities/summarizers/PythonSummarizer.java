package com.jakeporter.DocumentAnalyzer.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;


public class PythonSummarizer extends DocumentSummarizer {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private String script = "textSummarizer.py";

    @Override
    protected String computeSummary(String text) throws IOException {
        return runPythonScript(text);
    }

    private String runPythonScript(String text) throws IOException {
        // filePath starts with "/C:/..." - the first forward slash needs to go for it to work
        String filePath = this.getClass().getClassLoader().getResource(script).getPath().substring(1);
        logger.info(filePath);
        ProcessBuilder processBuilder = new ProcessBuilder("python", filePath, text);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        return readResult(process);
    }


}
