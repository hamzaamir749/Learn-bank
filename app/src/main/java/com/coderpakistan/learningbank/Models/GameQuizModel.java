package com.coderpakistan.learningbank.Models;

public class GameQuizModel {
    private String question,optionOne,optionTwo,optionThree,optionFour,answer;

    public GameQuizModel(String question, String optionOne, String optionTwo, String optionThree, String optionFour, String answer) {
        this.question = question;
        this.optionOne = optionOne;
        this.optionTwo = optionTwo;
        this.optionThree = optionThree;
        this.optionFour = optionFour;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getOptionOne() {
        return optionOne;
    }

    public String getOptionTwo() {
        return optionTwo;
    }

    public String getOptionThree() {
        return optionThree;
    }

    public String getOptionFour() {
        return optionFour;
    }

    public String getAnswer() {
        return answer;
    }
}
