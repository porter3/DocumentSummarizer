package com.jakeporter.DocumentSummarizer.utilities.textExtractors;

import java.io.InputStream;

public interface FileTextExtractor {

    public String extractTextFromStream(InputStream inputStream);
}
