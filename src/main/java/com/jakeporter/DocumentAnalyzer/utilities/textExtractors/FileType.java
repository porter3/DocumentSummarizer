package com.jakeporter.DocumentAnalyzer.utilities.textExtractors;


public enum FileType {

    DOC {
        public FileTextExtractor getExtractor() {
            return new DOCTextExtractor();
        }
    },

    DOCX {
        public FileTextExtractor getExtractor() {
            return new DOCXTextExtractor();
        }
    },

    PDF {
        public FileTextExtractor getExtractor() { return new PDFTextExtractor(); }
    };

    abstract FileTextExtractor getExtractor();
}
