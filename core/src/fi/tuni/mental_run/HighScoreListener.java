package fi.tuni.mental_run;

import java.util.List;

/**
 * The HighScore part of the game needs to implement this interface.
 */
public interface HighScoreListener {
    /**
     * receiveHighScore is called in the parent class once the high score data
     * has been downloaded from the server. It is passed using this method.
     *
     * @param highScores the loaded highScoreEntry data in a List
     */
    void receiveHighScore(List<HighScoreEntry> highScores);

    /**
     * If retrieving the high scores fails for some reason this is called.
     *
     * @param s error message as String
     */
    void failedToRetrieveHighScores(String s);

    /**
     * ReceiveConfirmationOnSend is called when sending high score entry is
     * successful.
     *
     * @param httpResponse the response code of the succeeded connection
     */
    void receiveSendReply(int httpResponse);

    /**
     * If sending high score entry fails this is called.
     *
     * @param s error message as String
     */
    void failedToSendHighScore(String s);
}