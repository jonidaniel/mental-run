package fi.tuni.mental_run;

/**
 * HighScoreEntry needs to have variables for all the attributes from the
 * HighScore server.In the demo case, we need variables String name and
 * int score.
 * <p>
 * The variable names need to match the json keys.
 * Also each of the attribute needs to have a getter and setter for json parsing.
 */
public class HighScoreEntry {
    private int level;
    private String name;
    private int score;

    public HighScoreEntry(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }
}