package com.jakeporter.DocumentSummarizer.domainEntities;

import java.util.Set;

public class SummaryComponents {

    private Set<SummarySentence> sentences;
    private int summaryCount;

    public SummaryComponents(Set<SummarySentence> sentences, int summaryCount) {
        this.sentences = sentences;
        this.summaryCount = summaryCount;
    }

    @Override
    public String toString() {
        return "{ " + "Sentence count: " + sentences.size() + "Summary count: " + summaryCount + " }";
    }
}