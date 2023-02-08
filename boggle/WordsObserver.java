package boggle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * GuidelineObserver interface
 */
public interface WordsObserver {

    /**
     * Receive notification from observable and update
     *
     * @param guessed_words Updated guidelines
     */
    HashMap<String, String> update(Set<String> guessed_words);

}

