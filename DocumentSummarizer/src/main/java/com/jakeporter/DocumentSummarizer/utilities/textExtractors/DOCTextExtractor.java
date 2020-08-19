package com.jakeporter.DocumentSummarizer.utilities.textExtractors;

import com.jakeporter.DocumentSummarizer.exceptions.TextExtractorException;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

import java.io.IOException;
import java.io.InputStream;

public class DOCTextExtractor implements FileTextExtractor {

    private static final String DOC_EXCEPTION_MESSAGE = "Something went wrong processing the word document.";

    @Override
    public String extractTextFromStream(InputStream stream) {
        String extractedText;
        try (stream) {
            HWPFDocument hwpfDocument = new HWPFDocument(stream);
            WordExtractor extractor = new WordExtractor(hwpfDocument);
            extractedText = extractor.getText();
        } catch (IOException e) {
            throw new TextExtractorException(DOC_EXCEPTION_MESSAGE);
        }
        return extractedText;
    }
}