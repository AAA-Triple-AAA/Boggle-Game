import java.util.*;

public class Receiver {
    Stack<String> historyWords;
    Stack<String> wordsGuessed;
    public Receiver(Stack<String> wordsGuessed) {
        this.wordsGuessed = wordsGuessed;
        this.historyWords = new Stack<String>();
    }

    public String undo() {
        if (!this.wordsGuessed.isEmpty()) {
            String word = this.wordsGuessed.pop();
            this.historyWords.push(word);
            return word;
        } else {
            System.out.println("No words left to undo.");
            return "";
        }
    }

    public String redo() {
        if (!this.historyWords.isEmpty()) {
            String word = this.historyWords.pop();
            this.wordsGuessed.push(word);
            return word;
        } else {
            System.out.println("No words left to redo.");
            return "";
        }
    }
}
