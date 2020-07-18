package com.jakeporter.DocumentSummarizer.utilities.summarizers;

import com.jakeporter.DocumentSummarizer.exceptions.*;
import com.jakeporter.DocumentSummarizer.utilities.textExtractors.FileTextExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;


public class PythonSummarizer extends DocumentSummarizer {

    private static final String SCRIPT = "textSummarizer.py";
    // filePath starts with "/C:/..." - the first forward slash needs to go (.substring(1)) for it to work on Windows (unsure about other OSs)
    private static final String FILE_PATH = PythonSummarizer.class.getClassLoader().getResource(SCRIPT).getPath().substring(1);
    private static final String SUMMARY_DELIMITER = ":::";

    public PythonSummarizer(){ super(); }

    public PythonSummarizer(FileTextExtractor extractor) {
        super(extractor);
    }

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected Set<String> computeSummaries(String text) {
        String summariesString = runPythonScript(text);
        return new LinkedHashSet<>(Arrays.asList(summariesString.split(SUMMARY_DELIMITER)));
    }

    private String runPythonScript(String text)  {
        Process process;
        String result = "";
        logger.info("Script path: " + FILE_PATH);
        ProcessBuilder processBuilder = new ProcessBuilder("python", FILE_PATH, SUMMARY_DELIMITER);
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

    private String getResultFromStdOut(Process process) {
        final String READING_EXCEPTION_MESSAGE = "Something went wrong in getting the summary.";
        StringBuilder builder = new StringBuilder();
        InputStream stdOut = process.getInputStream();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(stdOut));
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            try {
                reader.close();
            } catch (IOException f) {
                throw new ResultReadingException(READING_EXCEPTION_MESSAGE);
            }
            throw new ResultReadingException(READING_EXCEPTION_MESSAGE);
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
