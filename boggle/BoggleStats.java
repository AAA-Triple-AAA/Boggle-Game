package boggle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * The BoggleStats will contain statistics related to game play Boggle
 */
public class BoggleStats {

    /**
     * set of words the player finds in a given round
     */
    private Set<String> playerWords = new HashSet<String>();
    /**
     * set of words the computer finds in a given round
     */
    private Set<String> computerWords = new HashSet<String>();
    /**
     * the player's score for the current round
     */
    private int pScore;
    /**
     * the computer's score for the current round
     */
    private int cScore;
    /**
     * the player's total score across every round
     */
    private int pScoreTotal;
    /**
     * the computer's total score across every round
     */
    private int cScoreTotal;
    /**
     * the average number of words, per round, found by the player
     */
    private double pAverageWords;
    /**
     * the average number of words, per round, found by the computer
     */
    private double cAverageWords;
    /**
     * the current round being played
     */
    private int round;

    /**
     * enumarable types of players (human or computer)
     */
    public enum Player {
        Human("Human"),
        Computer("Computer");
        private final String player;

        Player(final String player) {
            this.player = player;
        }
    }

    /* BoggleStats constructor
     * ----------------------
     * Sets round, totals and averages to 0.
     * Initializes word lists (which are sets) for computer and human players.
     */
    public BoggleStats() {
        this.round = 0;
        // Human
        this.pScoreTotal = 0;
        this.pAverageWords = 0;
        this.playerWords = new HashSet<String>();
        // Computer
        this.cScoreTotal = 0;
        this.cAverageWords = 0;
        this.computerWords = new HashSet<String>();
    }

    /*
     * Add a word to a given player's word list for the current round.
     * You will also want to increment the player's score, as words are added.
     *
     * @param word     The word to be added to the list
     * @param player  The player to whom the word was awarded
     */
    public void addWord(String word, Player player) {
        int score = Math.max(0, word.length() - 3);
        if (player == Player.Human) {
            // Human
            pScore += score;
            playerWords.add(word);
        } else {
            // Computer
            cScore += score;
            computerWords.add(word);
        }
    }

    /*
     * Remove a word from a given player's word list for the current round.
     * You will also want to decrement the player's score, as words are removed.
     *
     * @param word     The word to be removed to the list
     * @param player  The player to whom the word was removed from
     */
    public void removeWord(String word, Player player) {
        int score = Math.max(0, word.length() - 3);
        if (player == Player.Human) {
            // Human
            pScore -= score;
            playerWords.remove(word);
        } else {
            // Computer
            cScore -= score;
            computerWords.remove(word);
        }
    }

    /*
     * End a given round.
     * This will clear out the human and computer word lists, so we can begin again.
     * The function will also update each player's total scores, average scores, and
     * reset the current scores for each player to zero.
     * Finally, increment the current round number by 1.
     */
    public void endRound() {
        round++;
        // update total scores
        pScoreTotal += pScore;
        cScoreTotal += cScore;
        // update average scores
        pAverageWords = (pAverageWords * (round - 1) + playerWords.size()) / round;
        cAverageWords = (cAverageWords * (round - 1) + computerWords.size()) / round;
        // reset the scores
        pScore = 0;
        cScore = 0;
        // Clear word lists
        playerWords.clear();
        computerWords.clear();
    }

    /*
     * Summarize one round of boggle.  Print out:
     * The words each player found this round.
     * Each number of words each player found this round.
     * Each player's score this round.
     */
    public void summarizeRound() {
        // Human
        printPlayerStat("Human", playerWords, pScore);
        // Computer
        printPlayerStat("Computer", computerWords, cScore);
        System.out.println("----------------------------------------");
    }

    /*
     * Summarize one round of boggle for one player. Print out the following stats for <playerName>:
     * The <words> <playerName> found this round.
     * The number of words <playerName> found this round.
     * <playerName>'s <score> this round.
     */
    private void printPlayerStat(String playerName, Set<String> words, int score) {
        System.out.println("----------------------------------------");
        System.out.println(playerName + " Player Stats:");
        System.out.print("Word(s) found this round: ");
        if (!words.isEmpty()) {
            for (String s : words) {
                System.out.print(s + ", ");
            }
        } else {
            System.out.print("No word found.");
        }
        System.out.println();
        System.out.println("Total word(s) found this round: " + words.size());
        System.out.println("Score this round: " + score);
    }

    /*
     * Summarize the entire boggle game.  Print out:
     * The total number of rounds played.
     * The total score for either player.
     * The average number of words found by each player per round.
     */
    public void summarizeGame() {
        System.out.println("Round Played: " + round);
        // Human
        System.out.println("Human Player Stats:");
        System.out.println("Score: " + pScoreTotal);
        System.out.println("Average number of words found per round: " + pAverageWords);
        // Computer
        System.out.println("Computer Player Stats:");
        System.out.println("Score: " + cScoreTotal);
        System.out.println("Average number of words found per round: " + cAverageWords);
    }

    /**
     * Return the list of the player's found word.
     *
     * @return the list of the player's found word.
     */
    public Set<String> getPlayerWords() {
        return this.playerWords;
    }

    /**
     * Return the number of rounds played.
     *
     * @return the number of rounds played.
     */
    public int getRound() {
        return this.round;
    }

    /**
     * Return the player's score.
     *
     * @return the player's current score.
     */
    public int getScore() {
        return this.pScore;
    }

    /**
     * Reset the score of the player to 0 and clear found words.
     */
    public void resetStats() {
        pScore = 0;
        playerWords.clear();
    }
}
