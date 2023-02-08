import boggle.BoggleGame;
import boggle.BoggleGrid;
import boggle.Dictionary;
import boggle.Position;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

/**
 * The Adapter class of BoggleUI, it handles data from Controller and BoggleGame to update each other.
 */
public class BoggleAdapter {

    // The main game class linked to the adapter
    BoggleGame game;

    // The dictionary of valid words
    Dictionary dictionary;

    // The map to keep track of the word
    Map<String, ArrayList<Position>> allWords = new HashMap<String, ArrayList<Position>>();

    // The controller linked to the adapter
    BoggleController controller;

    // The receiver object for undo and redo words
    Receiver receiver;

    // redo command that knows how to execute redo calls.
    RedoCommand redo;

    // undo command that knows how to execute undo calls.
    UndoCommand undo;

    /**
     * The constructor of BoggleAdapter, require a BoggleController to link with.
     */
    public BoggleAdapter(BoggleController controller) {
        this.controller = controller;
        this.game = new BoggleGame();
        this.dictionary = new Dictionary("wordlist.txt");
        this.receiver = new Receiver(this.game.wordsGuessed);
        this.redo = new RedoCommand(this.receiver);
        this.undo = new UndoCommand(this.receiver);
    }

    /**
     * Return a random 4x4 board made by the game system.
     */
    public String getBoardString() {
        return game.getRandomBoard();
    }

    /**
     * Reset the score of the game system.
     */
    public void resetScore() {
        game.resetStats();
    }

    /**
     * Populate allWords with the game system.
     */
    public void createAllWords(String board) {
        BoggleGrid grid = new BoggleGrid(4);
        grid.initalizeBoard(board);
        game.populateAllWords(allWords, dictionary, grid);
    }


    /**
     * Check if the word is valid (on the board and not been found).
     *
     * @param word the word to check
     * @return true if the word is valid.
     */
    public boolean checkWord(String word) {
        return allWords.containsKey(word) && !game.hasFoundWord(word);
    }

    /**
     * Let the game system know a word is found.
     *
     * @param word the word found
     */
    public void wordFound(String word) {
        game.increaseScore(word);
        addWordToTracker(word);
    }

    /**
     * Get the score of the player from the game system.
     *
     * @return the score of the player.
     */
    public int getScore() {
        return game.getScore();
    }

    /*
     * Word tracker methods
     * They are used to link UI with the word tracker
     */

    /**
     * Add word to the word tracker, and update the UI to display it.
     *
     * @param word the word to add to the word tracker
     */
    public void addWordToTracker(String word) {
        String s = "difficulty";
        int len = word.length();
        if (len == 4) {
            s = "Easy";
        } else if (len == 5) {
            s = "Medium";
        } else {
            s = "Hard";
        }
        this.game.wordsGuessed.push(word + " (" + s + ")");
        updateWordTrackerUI(this.game.wordsGuessed);
    }

    /**
     * Make the word tracker empty, and update the UI to display it. Used when a new game start.
     */
    public void clearTracker() {
        this.game.wordsGuessed.clear();
        this.receiver.historyWords.clear();
        this.controller.updateWordTrackerText("");
    }

    /**
     * Update the UI of word tracker to display the wordList.
     *
     * @param wordList the list of words to display
     */
    private void updateWordTrackerUI(Stack<String> wordList) {
        Iterator<String> iterator = wordList.iterator();
        String acu = "Words Found:\n";
        while (iterator.hasNext()) {
            acu += iterator.next() + "\n";
        }
        controller.updateWordTrackerText(acu);
    }

    /*
     * Score saver methods
     * They are used to  link UI with the score saver
     */

    /**
     * Add a new entry to the score saver, if the score saver already has an entry with the same name, and the score is
     * higher, replace the old score with new high score.
     *
     * @param name  the name given to high score
     * @param score the score to be saved
     */
    public void addEntry(String name, int score) throws IOException {
        Leaderboard lb = new Leaderboard();
        lb.addScore(name, score);
    }

    /**
     * Return the leaderboard as formatted string.
     */
    public String getLeaderboard() throws IOException {
        Leaderboard lb = new Leaderboard();
        return lb.getScores();
    }

    /*
     * Undo/redo methods
     * They are used to link UI with the undo/redo system
     */

    /**
     * Revert the last action of the player.
     */
    public void undo() {
        String word = this.undo.execute();
        String word1 = word.split(" ")[0];
        this.game.decreaseScore(word1);
        this.controller.updateScoreText();
        this.updateWordTrackerUI(this.game.wordsGuessed);
    }

    /**
     * Revert the last undo action of the player.
     */
    public void redo() {
        String word = this.redo.execute();
        String word1 = word.split(" ")[0];
        this.game.increaseScore(word1);
        this.controller.updateScoreText();
        this.updateWordTrackerUI(this.game.wordsGuessed);
    }
}
