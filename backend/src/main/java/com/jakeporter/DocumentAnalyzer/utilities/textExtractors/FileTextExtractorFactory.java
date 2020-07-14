package com.jakeporter.DocumentAnalyzer.utilities.textExtractors;

public class FileTextExtractorFactory {

    public static FileTextExtractor getExtractor(FileType type) {
        return type.getExtractor();
    }
}
