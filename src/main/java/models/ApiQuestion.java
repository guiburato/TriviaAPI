package models;

public class ApiQuestion {
    private String category;
    private String difficulty;
    private String question;
    private String correct_answer;
    private String[] incorrect_answers;

    public String getCategory() {
        return category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correct_answer;
    }

    public String[] getIncorrectAnswers() {
        return incorrect_answers;
    }
}

