package com.jakeporter.DocumentAnalyzer.utilities;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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

        // InputStreamReader reads bytes and decodes them into characters
        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String result = in.readLine();
        logger.info("Output result: " + result);
        return result;
    }
}
