package com.jakeporter.DocumentSummarizer.utilities.summarizers;

import com.jakeporter.DocumentSummarizer.domainEntities.SummaryComponents;
import com.jakeporter.DocumentSummarizer.exceptions.SummaryException;
import com.jakeporter.DocumentSummarizer.utilities.textExtractors.FileTextExtractor;
import com.jakeporter.DocumentSummarizer.utilities.validators.TextValidator;

import java.io.InputStream;


public abstract class DocumentSummarizer {

    private FileTextExtractor extractor;
    private TextValidator validator;

    public DocumentSummarizer(TextValidator validator) {
        this.validator = validator;
    }

    public DocumentSummarizer(FileTextExtractor extractor, TextValidator validator) {
        this.extractor = extractor;
        this.validator = validator;
    }

    // template method for files
    public SummaryComponents summarize(InputStream stream, String language) {
        String text = extractor.extractTextFromStream(stream);
        Thread validatorThread = new Thread(() -> {
            validator.validateText(text, language);
        });
        validatorThread.start();
        SummaryComponents components = computeSummaries(text, language);
        try {
            validatorThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new SummaryException("Something went wrong.");
        }
        return components;
    }

    // template method for non-file text
    public SummaryComponents summarize(String text, String language) {
        Thread validatorThread = new Thread(() -> {
            validator.validateText(text, language);
        });
        validatorThread.start();
        SummaryComponents components = computeSummaries(text, language);
        try {
            validatorThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new SummaryException("Something went wrong.");
        }
        return components;
    }

    protected abstract SummaryComponents computeSummaries(String text, String language);
}
