package com.jakeporter.DocumentAnalyzer.utilities.summarizers;

import com.jakeporter.DocumentAnalyzer.exceptions.ProblematicTextException;
import com.jakeporter.DocumentAnalyzer.exceptions.PythonScriptException;
import com.jakeporter.DocumentAnalyzer.exceptions.TextTooLongException;
import com.jakeporter.DocumentAnalyzer.exceptions.TextTooShortException;
import com.jakeporter.DocumentAnalyzer.utilities.textExtractors.FileTextExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class DocumentSummarizer {

    /*
        System variable that dictates the max character count for a command line arg on Unix/Linux systems.
        Don't need to pay attention to this until deployment time.
     */
    private final int MAX_ARG_STRLEN = 131072;
    /*
        System variable for max character count of entire command line.
        Is 32000 on my machine, working on a workaround that still allows me to extract the text in Java.
    */
    private final int ARG_MAX = 32000;
    private FileTextExtractor extractor;

    public DocumentSummarizer() {}

    public DocumentSummarizer(FileTextExtractor extractor) {
        this.extractor = extractor;
    }

    Logger logger = LoggerFactory.getLogger(this.getClass());

    // template method for files
    public String summarizeDocument(MultipartFile file) throws IOException {
        String text = extractor.extractText(file);
        handleTooLongText(text);
        String[] textChunks = breakText(text);
        return computeSummary(textChunks);
    }

    // template method for pure text
    public String summarizeDocument(String text) throws IOException {
        String[] textChunks = breakText(text);
        return computeSummary(textChunks);
    }

    protected abstract String computeSummary(String[] textChunks) throws IOException;

    // breaks text into chunks according to the maximum MAX_ARG_STRLEN OS value
    private String[] breakText(String text) {
        // ensure array size is always rounded up
        String[] textChunks = new String[(text.length() + MAX_ARG_STRLEN - 1) / MAX_ARG_STRLEN];
        int textIndex = 0;
        int charRemainder = text.length() % MAX_ARG_STRLEN;
        for (int i = 0; i < textChunks.length; i++) {
            // if on the last chunk of text:
            if (textIndex + charRemainder == text.length()) {
                textChunks[i] = text.substring(textIndex, textIndex + charRemainder);
            } else {
                textChunks[i] = text.substring(textIndex, textIndex + MAX_ARG_STRLEN);
            }
            textIndex += MAX_ARG_STRLEN;
        }
        return textChunks;
    }

    protected String readResult(Process process) throws IOException {
        // InputStreamReader reads bytes and decodes them into characters
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String result = reader.readLine();
        reader.close();
        // throw exceptions for any weird output
        handleResultIssues(result);
        return result;
    }

    private void handleTooLongText(String text) {
        int textLength = text.length();
        logger.info("textLength: " + textLength);
        if (textLength > ARG_MAX) {
            throw new TextTooLongException("The current character count limit for texts to be summarized is " + ARG_MAX + ". We're working on making it longer.\\n(Your character count: " + textLength);
        }
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
