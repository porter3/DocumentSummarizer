package com.jakeporter.DocumentSummarizer.utilities.textExtractors;


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
    },

    TXT {
        public FileTextExtractor getExtractor() { return new TXTTextExtractor(); }
    };

    abstract FileTextExtractor getExtractor();
}
