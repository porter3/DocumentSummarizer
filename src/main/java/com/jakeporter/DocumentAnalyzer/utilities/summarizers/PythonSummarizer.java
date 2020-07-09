package com.jakeporter.DocumentAnalyzer.utilities.summarizers;

import com.jakeporter.DocumentAnalyzer.exceptions.*;
import com.jakeporter.DocumentAnalyzer.utilities.textExtractors.FileTextExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;


public class PythonSummarizer extends DocumentSummarizer {

    private static final String SCRIPT = "textSummarizer.py";

    public PythonSummarizer(){ super(); }

    public PythonSummarizer(FileTextExtractor extractor) {
        super(extractor);
    }

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected String computeSummary(String text) throws IOException {
        return runPythonScript(text);
    }

    private String runPythonScript(String text) throws IOException {
        Process process;
        String result = "";
        // filePath starts with "/C:/..." - the first forward slash needs to go for it to work
        String filePath = this.getClass().getClassLoader().getResource(SCRIPT).getPath().substring(1);
        logger.info("Script path: " + filePath);
        ProcessBuilder processBuilder = new ProcessBuilder("python", filePath);
        processBuilder.redirectErrorStream(true);
        try {
            process = processBuilder.start();
        } catch (Exception e) {
            e.printStackTrace();
            throw new PythonScriptException("Something went wrong processing the file.");
        }
        writeToStdIn(process, text);
        result = getResultFromStdOut(process);
        handleResultIssues(result);
        return result;
    }

    private void writeToStdIn(Process process, String text) {
        OutputStream pythonStdIn = process.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(pythonStdIn));
        try {
            writer.write(text);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new WriterException("Something went wrong in getting the summary.");
        }
    }

    private String getResultFromStdOut(Process process) throws IOException {
        StringBuilder builder = new StringBuilder();
        InputStream stdOut = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stdOut));
        try {
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            throw new ResultReadingException("Something went wrong reading the file.");
        }
        return builder.toString();
    }

    private void handleResultIssues(String result) {
        final String pyCompilerErrorLine = "Traceback (most recent call last):";
        final String genericErrorLine = "Something went wrong with executing the Python script.";
        if (result.isBlank()) {
            throw new TextTooShortException("The text you tried to summarize is either too short or too repetitive to do so.");
        }
        if (result.equals(pyCompilerErrorLine)) {
            throw new ProblematicTextException("The text you tried to summarize doesn't summarize well.");
        }
        if (result.equals(genericErrorLine)) {
            throw new PythonScriptException("Something went wrong on our end.");
        }
    }

}
