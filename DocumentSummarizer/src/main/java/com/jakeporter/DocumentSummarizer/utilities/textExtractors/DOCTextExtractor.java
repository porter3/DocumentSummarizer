package com.jakeporter.DocumentSummarizer.utilities.textExtractors;

import com.jakeporter.DocumentSummarizer.exceptions.DOCIssueException;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

import java.io.IOException;
import java.io.InputStream;

public class DOCTextExtractor implements FileTextExtractor {

    static final String DOC_EXCEPTION_MESSAGE = "Something went wrong processing the word document.";

    @Override
    public String extractTextFromStream(InputStream stream) {
        HWPFDocument hwpfDocument;
        WordExtractor extractor;
        String extractedText;
        try {
            hwpfDocument = new HWPFDocument(stream);
            extractor = new WordExtractor(hwpfDocument);
            extractedText = extractor.getText();
            stream.close();
        } catch (IOException e) {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException f) {
                throw new DOCIssueException(DOC_EXCEPTION_MESSAGE);
            }
            throw new DOCIssueException(DOC_EXCEPTION_MESSAGE);
        }
        return extractedText;
    }
}
