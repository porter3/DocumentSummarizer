package com.jakeporter.DocumentSummarizer.utilities.summarizers;

import com.jakeporter.DocumentSummarizer.domainEntities.SummaryComponents;
import com.jakeporter.DocumentSummarizer.exceptions.SummaryException;
import com.jakeporter.DocumentSummarizer.utilities.textExtractors.FileTextExtractor;
import com.jakeporter.DocumentSummarizer.utilities.validators.TextValidator;

import java.io.InputStream;
import java.util.concurrent.atomic.AtomicReference;


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
    public SummaryComponents summarize(InputStream stream, String language) throws InterruptedException {
        String text = extractor.extractTextFromStream(stream);
        Thread validatorThread = new Thread(() -> {
            validator.validateText(text, language);
        });
        final AtomicReference<Throwable> throwableRef = new AtomicReference<Throwable>();
        validatorThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                throwableRef.set(e);
            }
        });
        validatorThread.start();
        SummaryComponents components = computeSummaries(text, language);
        validatorThread.join();
        handleAsyncExceptions(throwableRef);
        return components;
    }

    // template method for non-file text
    public SummaryComponents summarize(String text, String language) throws InterruptedException {
        Thread validatorThread = new Thread(() -> {
            validator.validateText(text, language);
        });
        final AtomicReference<Throwable> throwableRef = new AtomicReference<Throwable>();
        validatorThread.setUncaughtExceptionHandler((t, e) -> { throwableRef.set(e); });
        validatorThread.start();
        SummaryComponents components = computeSummaries(text, language);
        validatorThread.join();
        handleAsyncExceptions(throwableRef);
        return components;
    }

    private void handleAsyncExceptions(AtomicReference<Throwable> ref) {
        Throwable summaryThrowable = ref.get();
        if (summaryThrowable != null) {
            throw new SummaryException(summaryThrowable.getMessage());
        }
    }

    protected abstract SummaryComponents computeSummaries(String text, String language);
}
