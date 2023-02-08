package boggle;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * ObservableWords class, an abstract observable object type
 */
public abstract class ObservableWords {

    // Words to be observed
    private HashMap<String, String> possible_guesses = new HashMap<>();
    /**
     * add_guesses, ie add a word to the list of guesses;
     *
     * @param word is the word guessed, and player is the player type that guessed that word;
     */
    public abstract void addWord(String word, BoggleStats.Player player);

    /**
     * add_guesses, ie add a word to the list of guesses;
     *
     * @param possible_guesses is the word guessed, and player is the player type that guessed that word;
     */
    public void setObservableState(HashMap<String, String> possible_guesses) {
        // We want to observe the state, then notify all the observers
        this.possible_guesses = possible_guesses;
    }
}

