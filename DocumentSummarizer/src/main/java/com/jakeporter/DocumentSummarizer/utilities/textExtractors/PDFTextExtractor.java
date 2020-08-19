package com.jakeporter.DocumentSummarizer.utilities.textExtractors;

import com.jakeporter.DocumentSummarizer.exceptions.TextExtractorException;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.io.InputStream;

public class PDFTextExtractor implements FileTextExtractor {

    private static final String PDF_EXCEPTION_MESSAGE = "Something went wrong processing the PDF file.";

    @Override
    public String extractTextFromStream(InputStream stream) {
        String extractedText;
        // closing this closes the underlying InputStream
        try (RandomAccessBufferedFileInputStream randomAccessBufferedFileInputStream = new RandomAccessBufferedFileInputStream(stream)) {
            PDFParser parser = new PDFParser(randomAccessBufferedFileInputStream);
            // parse the stream and populate a nested COSDocument ref, close stream when done
            parser.parse();
            COSDocument cosDocument = parser.getDocument();
            // closing this closes the nested COSDocument
            try (PDDocument pdDocument = new PDDocument(cosDocument)) {
                extractedText = new PDFTextStripper().getText(pdDocument);
            }
        } catch (IOException e) {
            throw new TextExtractorException(PDF_EXCEPTION_MESSAGE);
        }
        return extractedText;
    }
}