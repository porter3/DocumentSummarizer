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

    private FileTextExtractor extractor;

    public DocumentSummarizer() {}

    public DocumentSummarizer(FileTextExtractor extractor) {
        this.extractor = extractor;
    }

    Logger logger = LoggerFactory.getLogger(this.getClass());

    // template method for files
    public String summarizeDocument(MultipartFile file) throws IOException {
        String text = extractText(file);
        return computeSummary(text);
    }

    // template method for pure text
    public String summarizeDocument(String text) throws IOException {
        return computeSummary(text);
    }

    protected abstract String computeSummary(String text) throws IOException;

    private String extractText(MultipartFile file) throws IOException {
        return extractor.extractText(file);
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
        String pythonErrorLine = "Traceback (most recent call last):";
        if (result.isBlank()) {
            throw new TextTooShortException("The text you tried to summarize is either too short or too repetitive to do so.");
        }
        if (result.equals(pythonErrorLine)) {
            throw new ProblematicTextException("The text you tried to summarize doesn't summarize well.");
        }
    }
}
