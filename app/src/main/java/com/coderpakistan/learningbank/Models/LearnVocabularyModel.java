package com.coderpakistan.learningbank.Models;

public class LearnVocabularyModel {
   private String id,name,image;
   private boolean isCategory;

    public LearnVocabularyModel(String id, String name, String image, boolean isCategory) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.isCategory = isCategory;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public boolean isCategory() {
        return isCategory;
    }
}
