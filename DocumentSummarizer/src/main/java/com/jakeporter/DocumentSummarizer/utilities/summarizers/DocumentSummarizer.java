package com.jakeporter.DocumentSummarizer.utilities.summarizers;

import com.jakeporter.DocumentSummarizer.exceptions.*;
import com.jakeporter.DocumentSummarizer.utilities.textExtractors.FileTextExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public abstract class DocumentSummarizer {

    private static final int CHAR_MAX = Integer.MAX_VALUE; // prevent unpredictable behavior
    private FileTextExtractor extractor;

    public DocumentSummarizer() {}

    public DocumentSummarizer(FileTextExtractor extractor) {
        this.extractor = extractor;
    }

    Logger logger = LoggerFactory.getLogger(this.getClass());

    // template method for files
    public List<String> summarizeDocument(MultipartFile file) {
        String text = extractor.extractText(file);
        checkTextLength(text);
        List<String> summaries = computeSummaries(text);
        return removeDuplicateSummaries(summaries);
    }

    // template method for pure text
    public List<String> summarizeDocument(String text) {
        checkTextLength(text);
        List<String> summaries = computeSummaries(text);
        return removeDuplicateSummaries(summaries);
    }

    protected abstract List<String> computeSummaries(String text);

    private void checkTextLength(String text) {
        int textLength = text.length();
        logger.info("Text length: " + textLength);
        if (textLength > CHAR_MAX) {
            throw new TextTooLongException("The current character count limit for texts to be summarized is " + CHAR_MAX + ". We're working on making it longer.\\n(Your character count: " + textLength);
        }
    }

    private List<String> removeDuplicateSummaries(List<String> summaries) {
        return summaries.stream()
            .distinct()
            .collect(Collectors.toList());
    }

}
