package com.jakeporter.DocumentSummarizer.domainEntities;

public class SummarySentence {

    private int orderPlacement;
    // is the adjusted score in the Python script, not the original one
    private double score;
    private String sentence;

    public SummarySentence(int orderPlacement, double score, String sentence) {
        this.orderPlacement = orderPlacement;
        this.score = score;
        this.sentence = sentence;
    }
}
