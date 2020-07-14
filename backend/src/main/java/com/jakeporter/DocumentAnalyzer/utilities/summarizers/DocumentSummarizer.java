package com.jakeporter.DocumentAnalyzer.utilities.summarizers;

import com.jakeporter.DocumentAnalyzer.exceptions.*;
import com.jakeporter.DocumentAnalyzer.utilities.textExtractors.FileTextExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public abstract class DocumentSummarizer {

    private static final int CHAR_MAX = Integer.MAX_VALUE; // prevent unpredictable behavior
    private FileTextExtractor extractor;

    public DocumentSummarizer() {}

    public DocumentSummarizer(FileTextExtractor extractor) {
        this.extractor = extractor;
    }

    Logger logger = LoggerFactory.getLogger(this.getClass());

    // template method for files
    public String summarizeDocument(MultipartFile file) throws IOException {
        String text = extractor.extractText(file);;
        checkTextLength(text);
        return computeSummary(text);
    }

    // template method for pure text
    public String summarizeDocument(String text) throws IOException {
        checkTextLength(text);
        return computeSummary(text);
    }

    protected abstract String computeSummary(String text) throws IOException;

    private void checkTextLength(String text) {
        int textLength = text.length();
        logger.info("Text length: " + textLength);
        if (textLength > CHAR_MAX) {
            throw new TextTooLongException("The current character count limit for texts to be summarized is " + CHAR_MAX + ". We're working on making it longer.\\n(Your character count: " + textLength);
        }
    }

}
