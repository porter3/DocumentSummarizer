package com.jakeporter.DocumentAnalyzer.utilities.summarizers;

import com.jakeporter.DocumentAnalyzer.exceptions.ProblematicTextException;
import com.jakeporter.DocumentAnalyzer.exceptions.TextTooShortException;
import com.jakeporter.DocumentAnalyzer.utilities.textExtractors.FileTextExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class DocumentSummarizer {

    // system variable that dictates the max character count for a command line argument
    private final int ARG_MAX = 32000;
    private FileTextExtractor extractor;

    public DocumentSummarizer() {}

    public DocumentSummarizer(FileTextExtractor extractor) {
        this.extractor = extractor;
    }

    Logger logger = LoggerFactory.getLogger(this.getClass());

    // template method for files
    public String summarizeDocument(MultipartFile file) throws IOException {
        String[] textChunks = breakText(extractor.extractText(file));
        return computeSummary(textChunks);
    }

    // template method for pure text
    public String summarizeDocument(String text) throws IOException {
        String[] textChunks = breakText(text);
        return computeSummary(textChunks);
    }

    protected abstract String computeSummary(String[] textChunks) throws IOException;

    // breaks text into chunks according to the maximum ARG_MAX OS value
    private String[] breakText(String text) {
        // ensure array size is always rounded up
        String[] textChunks = new String[(text.length() + ARG_MAX - 1) / ARG_MAX];
        int textIndex = 0;
        int charRemainder = text.length() % ARG_MAX;
        for (int i = 0; i < textChunks.length; i++) {
            // if on the last chunk of text:
            if (textIndex + charRemainder == text.length()) {
                textChunks[i] = text.substring(textIndex, textIndex + charRemainder);
            } else {
                textChunks[i] = text.substring(textIndex, textIndex + ARG_MAX);
            }
            textIndex += ARG_MAX;
        }
        return textChunks;
    }

    protected String readResult(Process process) throws IOException {
        // InputStreamReader reads bytes and decodes them into characters
        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String result = in.readLine();
        logger.info("Output result: " + result);
        // throw exceptions for any weird output
        handleResultIssues(result);
        return result;
    }

    private void handleResultIssues(String result) {
        final String pythonErrorLine = "Traceback (most recent call last):";
        if (result.isBlank()) {
            throw new TextTooShortException("The text you tried to summarize is either too short or too repetitive to do so.");
        }
        if (result.equals(pythonErrorLine)) {
            throw new ProblematicTextException("The text you tried to summarize doesn't summarize well.");
        }
    }
}
