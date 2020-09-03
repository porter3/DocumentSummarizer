package com.jakeporter.DocumentSummarizer.utilities.summarizers;

import com.jakeporter.DocumentSummarizer.domainEntities.SummaryComponents;
import com.jakeporter.DocumentSummarizer.exceptions.SummaryException;
import com.jakeporter.DocumentSummarizer.utilities.textExtractors.FileTextExtractor;
import com.jakeporter.DocumentSummarizer.utilities.validators.TextValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;


public abstract class DocumentSummarizer {

    private static final int CHAR_MAX = Integer.MAX_VALUE; // prevent unpredictable behavior
    private FileTextExtractor extractor;
    private TextValidator validator;

    public DocumentSummarizer(TextValidator validator) {
        this.validator = validator;
    }

    public DocumentSummarizer(FileTextExtractor extractor, TextValidator validator) {
        this.extractor = extractor;
        this.validator = validator;
    }

    Logger logger = LoggerFactory.getLogger(this.getClass());

    // template method for files
    public SummaryComponents summarize(InputStream stream, String language) {
        String text = extractor.extractTextFromStream(stream);
        checkTextLength(text);
        validator.validateText(text, language);
        return computeSummaries(text, language);
    }

    // template method for pure text
    public SummaryComponents summarize(String text, String language) {
        checkTextLength(text);
        validator.validateText(text, language);
        return computeSummaries(text, language);
    }

    protected abstract SummaryComponents computeSummaries(String text, String language);

    private void checkTextLength(String text) {
        int textLength = text.length();
        logger.info("Text length: " + textLength);
        if (textLength > CHAR_MAX) {
            throw new SummaryException("The current character count limit for texts to be summarized is " + CHAR_MAX + ".\\nYour character count: " + textLength);
        }
    }

}
