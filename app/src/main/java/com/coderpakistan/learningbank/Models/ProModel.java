package com.coderpakistan.learningbank.Models;

public class ProModel {
    String word,english,urdu,sentence,image;

    public ProModel(String word, String english, String urdu, String sentence, String image) {
        this.word = word;
        this.english = english;
        this.urdu = urdu;
        this.sentence = sentence;
        this.image = image;
    }

    public String getWord() {
        return word;
    }

    public String getEnglish() {
        return english;
    }

    public String getUrdu() {
        return urdu;
    }

    public String getSentence() {
        return sentence;
    }

    public String getImage() {
        return image;
    }
}
