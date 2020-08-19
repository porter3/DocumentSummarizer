package com.jakeporter.DocumentSummarizer.utilities.textExtractors;

import com.jakeporter.DocumentSummarizer.exceptions.TextExtractorException;

import java.io.*;

public class TXTTextExtractor implements FileTextExtractor {

    private static final String TXT_EXCEPTION_MESSAGE = "Something went wrong processing the text file.";

    @Override
    public String extractTextFromStream(InputStream stream) {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream));){
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new TextExtractorException(TXT_EXCEPTION_MESSAGE);
        }
        return builder.toString();
    }
}
