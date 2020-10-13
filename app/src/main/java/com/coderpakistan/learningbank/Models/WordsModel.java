package com.coderpakistan.learningbank.Models;

public class WordsModel {
    private String name, image;

    public WordsModel(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }
}
