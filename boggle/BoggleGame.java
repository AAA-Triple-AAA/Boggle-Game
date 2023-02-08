package boggle;

import java.util.*;

public class BoggleGame {

    /**
     * stores game statistics
     */
    private BoggleStats gameStats;


    /**
     * stores all words guessed by the player
     */
    public Stack<String> wordsGuessed;


    /**
     * dice used to randomize letter assignments for a small grid
     */
    private final String[] dice_small_grid = //dice specifications, for small and large grids
            {"AAEEGN", "ABBJOO", "ACHOPS", "AFFKPS", "AOOTTW", "CIMOTU", "DEILRX", "DELRVY",
                    "DISTTY", "EEGHNW", "EEINSU", "EHRTVW", "EIOSST", "ELRTTY", "HIMNQU", "HLNNRZ"};
    /**
     * dice used to randomize letter assignments for a big grid
     */
    private final String[] dice_big_grid =
            {"AAAFRS", "AAEEEE", "AAFIRS", "ADENNN", "AEEEEM", "AEEGMU", "AEGMNN", "AFIRSY",
                    "BJKQXZ", "CCNSTW", "CEIILT", "CEILPT", "CEIPST", "DDLNOR", "DDHNOT", "DHHLOR",
                    "DHLNOR", "EIIITT", "EMOTTT", "ENSSSU", "FIPRSY", "GORRVW", "HIPRRY", "NOOTUW", "OOOTTU"};

    /*
     * BoggleGame constructor
     */
    public BoggleGame() {
        this.wordsGuessed = new Stack<String>();
        this.gameStats = new BoggleStats();
    }

    /*
     * Provide instructions to the user, so they know how to play the game.
     */
    public void giveInstructions() {
        System.out.println("The Boggle board contains a grid of letters that are randomly placed.");
        System.out.println("We're both going to try to find words in this grid by joining the letters.");
        System.out.println("You can form a word by connecting adjoining letters on the grid.");
        System.out.println("Two letters adjoin if they are next to each other horizontally, ");
        System.out.println("vertically, or diagonally. The words you find must be at least 4 letters long, ");
        System.out.println("and you can't use a letter twice in any single word. Your points ");
        System.out.println("will be based on word length: a 4-letter word is worth 1 point, 5-letter");
        System.out.println("words earn 2 points, and so on. After you find as many words as you can,");
        System.out.println("I will find all the remaining words.");
        System.out.println("\nHit return when you're ready...");
    }


    /*
     * Gets information from the user to initialize a new Boggle game.
     * It will loop until the user indicates they are done playing.
     */
    public void playGame(String board) {
//        int boardSize;
//        while (true) {
//        System.out.println("Enter 1 to play on a big (5x5) grid; 2 to play on a small (4x4) one:");
//        String choiceGrid = board;
//
//        //get grid size preference
//            if (choiceGrid == "") break; //end game if user inputs nothing
//            while (!choiceGrid.equals("1") && !choiceGrid.equals("2")) {
//                System.out.println("Please try again.");
//                System.out.println("Enter 1 to play on a big (5x5) grid; 2 to play on a small (4x4) one:");
//            }
//
//            if (choiceGrid.equals("1")) boardSize = 5;
//            else boardSize = 4;
//
//        //get letter choice preference
//        System.out.println("Enter 1 to randomly assign letters to the grid; 2 to provide your own.");
//        String choiceLetters = board;
//
//            if (choiceLetters.equals("1")) {
//                playRound(boardSize, randomizeLetters(boardSize));
//            } else {
//                System.out.println("Input a list of " + boardSize * boardSize + " letters:");
//                choiceLetters = scanner.nextLine();
//                while (!(choiceLetters.length() == boardSize * boardSize)) {
//                    System.out.println("Sorry, bad input. Please try again.");
//                    System.out.println("Input a list of " + boardSize * boardSize + " letters:");
//                    choiceLetters = scanner.nextLine();
//                }
//                playRound(boardSize, choiceLetters.toUpperCase());
//            }
//        playRound(4, choiceLetters.toUpperCase());
//
//        //round is over! So, store the statistics, and end the round.
//        this.gameStats.summarizeRound();
//        this.gameStats.endRound();
//
//        }
//
//        //we are done with the game! So, summarize all the play that has transpired and exit.
//        this.gameStats.summarizeGame();
//        System.out.println("Thanks for playing!");
    }

    /*
     * Play a round of Boggle.
     * This initializes the main objects: the board, the dictionary, the map of all
     * words on the board, and the set of words found by the user. These objects are
     * passed by reference from here to many other functions.
     */
    public void playRound(int size, String letters) {
        //step 1. initialize the grid
        BoggleGrid grid = new BoggleGrid(size);
        grid.initalizeBoard(letters);
        //step 2. initialize the dictionary of legal words
        Dictionary boggleDict = new Dictionary("src/wordlist.txt"); //you may have to change the path to the wordlist, depending on where you place it.
        //step 3. find all legal words on the board, given the dictionary and grid arrangement.
        Map<String, ArrayList<Position>> allWords = new HashMap<String, ArrayList<Position>>();
        findAllWords(allWords, boggleDict, grid);
        //step 4. allow the user to try to find some words on the grid
        humanMove(grid, allWords);
        //step 5. allow the computer to identify remaining words
        computerMove(allWords);
    }

    /*
     * This method should return a String of letters (length 16 or 25 depending on the size of the grid).
     * There will be one letter per grid position, and they will be organized left to right,
     * top to bottom. A strategy to make this string of letters is as follows:
     * -- Assign a one of the dice to each grid position (i.e. dice_big_grid or dice_small_grid)
     * -- "Shuffle" the positions of the dice to randomize the grid positions they are assigned to
     * -- Randomly select one of the letters on the given die at each grid position to determine
     *    the letter at the given position
     *
     * @return String a String of random letters (length 16 or 25 depending on the size of the grid)
     */
    private String randomizeLetters(int size) {
        size = size * size;
        // Assign the dice
        String[] diceGrid = new String[size];
        if (size <= 16) {
            for (int i = 0; i < size; i++) {
                diceGrid[i] = dice_small_grid[i];
            }
        } else { // Size > 16
            for (int i = 0; i < size; i++) {
                diceGrid[i] = dice_big_grid[i];
            }
        }
        // Shuffle
        int shuffleTime = (int) (Math.random() * size) + size; // size to size * 2 - 1
        for (int i = 0; i < shuffleTime; i++) {
            int randomIndex1 = (int) (Math.random() * size); // 0 to size - 1
            int randomIndex2 = (int) (Math.random() * size); // 0 to size - 1
            String temp = diceGrid[randomIndex1];
            diceGrid[randomIndex1] = dice_big_grid[randomIndex2];
            dice_big_grid[randomIndex2] = temp;
        }
        // Form string
        String acu = "";
        for (int i = 0; i < size; i++) {
            acu += diceGrid[i].charAt((int) (Math.random() * diceGrid[i].length())); // 0 to diceGrid[i].length() - 1
        }
        return acu;
    }


    /*
     * This should be a recursive function that finds all valid words on the boggle board.
     * Every word should be valid (i.e. in the boggleDict) and of length 4 or more.
     * Words that are found should be entered into the allWords HashMap.  This HashMap
     * will be consulted as we play the game.
     *
     * Note that this function will be a recursive function.  You may want to write
     * a wrapper for your recursion. Note that every legal word on the Boggle grid will correspond to
     * a list of grid positions on the board, and that the Position class can be used to represent these
     * positions. The strategy you will likely want to use when you write your recursion is as follows:
     * -- At every Position on the grid:
     * ---- add the Position of that point to a list of stored positions
     * ---- if your list of stored positions is >= 4, add the corresponding word to the allWords Map
     * ---- recursively search for valid, adjacent grid Positions to add to your list of stored positions.
     * ---- Note that a valid Position to add to your list will be one that is either horizontal, diagonal, or
     *      vertically touching the current Position
     * ---- Note also that a valid Position to add to your list will be one that, in conjunction with those
     *      Positions that precede it, form a legal PREFIX to a word in the Dictionary (this is important!)
     * ---- Use the "isPrefix" method in the Dictionary class to help you out here!!
     * ---- Positions that already exist in your list of stored positions will also be invalid.
     * ---- You'll be finished when you have checked EVERY possible list of Positions on the board, to see
     *      if they can be used to form a valid word in the dictionary.
     * ---- Food for thought: If there are N Positions on the grid, how many possible lists of positions
     *      might we need to evaluate?
     *
     * @param allWords A mutable list of all legal words that can be found, given the boggleGrid grid letters
     * @param boggleDict A dictionary of legal words
     * @param boggleGrid A boggle grid, with a letter at each position on the grid
     */
    private void findAllWords(Map<String, ArrayList<Position>> allWords, Dictionary boggleDict, BoggleGrid boggleGrid) {
        Position startPosition = new Position(0, 0);
        ArrayList<Position> prevPosition = new ArrayList<Position>();
        for (int row = 0; row < boggleGrid.numRows(); row++) {
            for (int column = 0; column < boggleGrid.numCols(); column++) {
                startPosition = new Position(row, column);
                prevPosition = new ArrayList<Position>();
                findAllWords_Recursion(allWords, boggleDict, boggleGrid,
                        boggleGrid.getCharAt(startPosition.getRow(0), startPosition.getCol(0)) + "", startPosition, prevPosition);
            }
        }
    }

    /*
     * The recursive part of findAllWords. This finds all possible words given a "seed" location (Finding all words
     * that start with the letter at the "seed" location)
     *
     * @param allWords A mutable list of all legal words that can be found, given the boggleGrid grid letters
     * @param boggleDict A dictionary of legal words
     * @param boggleGrid A boggle grid, with a letter at each position on the grid
     * @param formingWord The "word" that is being formed
     * @param currPosition The position of the last letter
     * @param prevPositions An array list with the positions of the all previous letter
     */
    private void findAllWords_Recursion(Map<String, ArrayList<Position>> allWords, Dictionary boggleDict, BoggleGrid boggleGrid,
                                        String formingWord, Position currPosition, ArrayList<Position> prevPositions) {
        if (boggleDict.isPrefix(formingWord)) {
            // If word exists, the add to the list
            if (formingWord.length() >= 4 && boggleDict.containsWord(formingWord)) {
                // Add to the list
                ArrayList<Position> positionTaken = new ArrayList<Position>();
                for (int i = 0; i < prevPositions.size(); i++) {
                    Position p = new Position(prevPositions.get(i).getRow(0), prevPositions.get(i).getCol(0));
                    positionTaken.add(p);
                }
                Position p = new Position(currPosition.getRow(0), currPosition.getCol(0));
                positionTaken.add(p);
                allWords.put(formingWord.toUpperCase(), positionTaken);
            }

            // Recursion always happens, even when a word is found
            int currRow = currPosition.getRow(0), currCol = currPosition.getCol(0);
            // Right
            goToPostion(allWords, boggleDict, boggleGrid,
                    formingWord, currPosition, prevPositions, currRow, currCol + 1);
            // Left
            goToPostion(allWords, boggleDict, boggleGrid,
                    formingWord, currPosition, prevPositions, currRow, currCol - 1);
            // Bottom
            goToPostion(allWords, boggleDict, boggleGrid,
                    formingWord, currPosition, prevPositions, currRow + 1, currCol);
            // Top
            goToPostion(allWords, boggleDict, boggleGrid,
                    formingWord, currPosition, prevPositions, currRow - 1, currCol);
            // Bottom-Right
            goToPostion(allWords, boggleDict, boggleGrid,
                    formingWord, currPosition, prevPositions, currRow + 1, currCol + 1);
            // Bottom-Left
            goToPostion(allWords, boggleDict, boggleGrid,
                    formingWord, currPosition, prevPositions, currRow + 1, currCol - 1);
            // Top-Right
            goToPostion(allWords, boggleDict, boggleGrid,
                    formingWord, currPosition, prevPositions, currRow - 1, currCol + 1);
            // Top-Left
            goToPostion(allWords, boggleDict, boggleGrid,
                    formingWord, currPosition, prevPositions, currRow - 1, currCol - 1);
        }
        // Else not a prefix, stop recursion
    }

    /*
     * The extra recursive part of findAllWords_Recursion. Taking a next position and move the forming word onto that
     * position.
     * If the next position does not exist on the grid or it is in the <prevPositions>, the recursion stops.
     *
     * @param allWords A mutable list of all legal words that can be found, given the boggleGrid grid letters
     * @param boggleDict A dictionary of legal words
     * @param boggleGrid A boggle grid, with a letter at each position on the grid
     * @param formingWord The "word" that is being formed
     * @param currPosition The position of the last letter
     * @param prevPositions An array list with the positions of the all previous letter
     */
    private void goToPostion(Map<String, ArrayList<Position>> allWords, Dictionary boggleDict, BoggleGrid boggleGrid,
                             String formingWord, Position currPosition, ArrayList<Position> prevPositions,
                             int nextRow, int nextCol) {
        // Check if the nextRow and the nextCol exist on the grid
        if (nextRow < boggleGrid.numRows() && nextCol < boggleGrid.numCols() &&
                nextRow >= 0 && nextCol >= 0) {
            Position nextPosition = new Position(nextRow, nextCol);
            // Check if the new position has already been checked
            if (!hasPositionInList(prevPositions, nextPosition)) {
                findAllWords_Recursion(allWords, boggleDict, boggleGrid,
                        formingWord + boggleGrid.getCharAt(nextRow, nextCol),
                        new Position(nextRow, nextCol), createNewPrevPositions(currPosition, prevPositions));
            }
        }
    }

    /*
     * Taking the previous positions and add a new position at the end. Returning a new array list.
     *
     * @param currPosition The position of the last letter
     * @param prevPositions An arrayList with the positions of the all previous letter
     */
    private ArrayList<Position> createNewPrevPositions(Position currPosition, ArrayList<Position> prevPositions) {
        ArrayList<Position> newPrevPositions = new ArrayList<Position>();
        for (int i = 0; i < prevPositions.size(); i++) {
            newPrevPositions.add(prevPositions.get(i));
        }
        newPrevPositions.add(currPosition);
        return newPrevPositions;
    }

    /*
     * Check if <position> exist in <list>. Return true if it does.
     * If the row and column of a <position> match with one position that exist in the <list>, its in the <list>.
     *
     * @param currPosition The position of the last letter
     * @param prevPositions An arrayList with the positions of the all previous letter
     */
    private boolean hasPositionInList(ArrayList<Position> list, Position position) {
        for (Position p : list) {
            if (p.getRow(0) == position.getRow(0) && p.getCol(0) == position.getCol(0)) {
                return true;
            }
        }
        return false;
    }

    /*
     * Gets words from the user.  As words are input, check to see that they are valid.
     * If yes, add the word to the player's word list (in boggleStats) and increment
     * the player's score (in boggleStats).
     * End the turn once the user hits return (with no word).
     *
     * @param board The boggle board
     * @param allWords A mutable list of all legal words that can be found, given the boggleGrid grid letters
     */
    private void humanMove(BoggleGrid board, Map<String, ArrayList<Position>> allWords) {
        System.out.println("It's your turn to find some words!");
        ArrayList<String> foundWords = new ArrayList<String>();
        while (true) {
            //step 1. Print the board for the user, so they can scan it for words
            System.out.print(board);
            //step 2. Get a input (a word) from the user via the console
            String playerInput = "";
            //step 3. Check to see if it is valid (note validity checks should be case-insensitive)
            final String TRY_AGAIN = ", please try again.";
            // Prints different message for each invalidity
            if (playerInput.equals("")) {
                //step 6. End when the player hits return (with no word choice).
                break;
            } else if (playerInput.length() < 4) {
                System.out.println("Word must have at least 4 characters" + TRY_AGAIN);
            } else if (foundWords.contains(playerInput)) {
                System.out.println("Word already found" + TRY_AGAIN);
            } else if (!allWords.containsKey(playerInput)) {
                System.out.println("Word does not exist on board" + TRY_AGAIN);
            } else {
                //step 4. If it's valid, update the player's word list and score (stored in boggleStats)
                this.gameStats.addWord(playerInput, BoggleStats.Player.Human);
                foundWords.add(playerInput);
            }
            //step 5. Repeat step 1 - 4
        }
    }

    /*
     * Gets words from the computer.  The computer should find words that are
     * both valid and not in the player's word list.  For each word that the computer
     * finds, update the computer's word list and increment the
     * computer's score (stored in boggleStats).
     *
     * @param allWords A mutable list of all legal words that can be found, given the boggleGrid grid letters
     */
    private void computerMove(Map<String, ArrayList<Position>> all_words) {
        ArrayList<String> foundWords = new ArrayList<String>();
        for (String s : all_words.keySet()) {
            if (!foundWords.contains(s) && !gameStats.getPlayerWords().contains(s)) {
                this.gameStats.addWord(s, BoggleStats.Player.Computer);
                foundWords.add(s);
            }
        }
    }

    /*
     * Method for the adapter
     */

    /**
     * Return a string with 16 characters to create a boggle board.
     *
     * @return a string with 16 characters.
     */
    public String getRandomBoard() {
        return randomizeLetters(4);
    }

    /**
     * Reset the stats of the game.
     */
    public void resetStats() {
        this.gameStats.resetStats();
    }

    /**
     * Increase the score of the player based on the word found or redo.
     *
     * @param word the word being added
     */
    public void increaseScore(String word) {
        this.gameStats.addWord(word, BoggleStats.Player.Human);
    }

    /**
     * Decrease the score of the player based on the word undone.
     *
     * @param word the word being added
     */
    public void decreaseScore(String word) {
        this.gameStats.removeWord(word, BoggleStats.Player.Human);
    }

    /**
     * Return the score of the player.
     *
     * @return the current score of the player.
     */
    public int getScore() {
        return this.gameStats.getScore();
    }

    /**
     * Populate allWords using findAllWords.
     *
     * @param allWords   the map to populate
     * @param boggleDict the dictionary of valid words
     * @param boggleGrid the grid to find word in
     */
    public void populateAllWords(Map<String, ArrayList<Position>> allWords, Dictionary boggleDict, BoggleGrid boggleGrid) {
        findAllWords(allWords, boggleDict, boggleGrid);
    }

    /**
     * Determine whether a word is already found by the player. Return true if the word has been found.
     *
     * @param word the word to check
     * @return true if the word is already found by the player.
     */
    public boolean hasFoundWord(String word) {
        return this.gameStats.getPlayerWords().contains(word);
    }
}
