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

    static final String PDF_EXCEPTION_MESSAGE = "Something went wrong processing the PDF file.";

    @Override
    public String extractTextFromStream(InputStream stream) {
        // this class name isn't long enough
        RandomAccessBufferedFileInputStream randomAccessBufferedFileInputStream = null;
        PDFParser parser;
        COSDocument cosDocument;
        PDDocument pdDocument = null;
        String extractedText;
        try {
            randomAccessBufferedFileInputStream = new RandomAccessBufferedFileInputStream(stream);
            parser = new PDFParser(randomAccessBufferedFileInputStream);
            // parse the stream and populate a nested COSDocument ref, close stream when done
            parser.parse();
            cosDocument = parser.getDocument();
            pdDocument = new PDDocument(cosDocument);
            extractedText = new PDFTextStripper().getText(pdDocument);
            // close the nested COSDocument
            pdDocument.close();
            // close the nested InputStream
            randomAccessBufferedFileInputStream.close();
        } catch (IOException e) {
            try {
                if (pdDocument != null) {
                    pdDocument.close();
                }
                if (randomAccessBufferedFileInputStream != null) {
                    randomAccessBufferedFileInputStream.close();
                }
            } catch (IOException f) {
                throw new TextExtractorException(PDF_EXCEPTION_MESSAGE);
            }
            throw new TextExtractorException(PDF_EXCEPTION_MESSAGE);
        }
        return extractedText;
    }
}
