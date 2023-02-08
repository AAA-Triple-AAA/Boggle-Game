package boggle;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeSet;

/**
 * The Dictionary will contain lists of words that are acceptable for Boggle
 */
public class Dictionary {

    /**
     * set of legal words for Boggle
     */
    private TreeSet<String> legalWords;

    /**
     * Class constructor
     *
     * @param filename the file containing a list of legal words.
     */
    public Dictionary(String filename) {
        String line = "";
        int wordcount = 0;
        this.legalWords = new TreeSet<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            while ((line = br.readLine()) != null) {
                if (line.strip().length() > 0) {
                    legalWords.add(line.strip());
                    wordcount++;
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Initialized " + wordcount + " words in the Dictionary.");
    }

    /*
     * Checks to see if a provided word is in the dictionary.
     *
     * @param word  The word to check
     * @return  A boolean indicating if the word has been found
     */
    public boolean containsWord(String word) {
        return legalWords.contains(word.toLowerCase());
    }

    /*
     * Checks to see if a provided string is a prefix of any word in the dictionary.
     *
     * @param str  The string to check
     * @return  A boolean indicating if the string has been found as a prefix
     */
    public boolean isPrefix(String str) {
        Object[] sList = legalWords.toArray();
        return binarySearchPrefix(sList, str.toLowerCase());
    }

    /*
     * Checks to see if <prefix> is a prefix of any object in the sorted <list>. Operate similar to binary search.
     *
     * @param list An array of object to check
     * @param prefix The prefix to check
     * @return  A boolean indicating if the string has been found as a prefix
     */
    private boolean binarySearchPrefix(Object[] list, String prefix) {
        String convertString;
        if (list.length == 0) {
            return false;
        } else if (list.length == 1) {
            convertString = list[0].toString().toLowerCase();
            if (convertString.length() < prefix.length()) {
                return false;
            } else {
                return convertString.substring(0, prefix.length()).equalsIgnoreCase(prefix);
            }
        } else {
            int m = list.length / 2;
            convertString = list[m].toString().toLowerCase();
            if (convertString.length() < prefix.length()) {
                if (prefix.compareTo(convertString) < 0) {
                    return binarySearchPrefix(sliceStringArray(list, 0, m - 1), prefix);
                } else {
                    return binarySearchPrefix(sliceStringArray(list, m, list.length - 1), prefix);
                }
            } else {
                if (prefix.compareTo(convertString.substring(0, prefix.length())) < 0) {
                    return binarySearchPrefix(sliceStringArray(list, 0, m - 1), prefix);
                } else {
                    return binarySearchPrefix(sliceStringArray(list, m, list.length - 1), prefix);
                }
            }
        }
    }

    /*
     * Create a string array from the elements of list, starting at <startIndex>, ending at <endIndex> (Inclusive).
     *
     * @param list An array of object to be sliced
     * @return String[] a string array with element of the list converted to string from <startIndex> to <endIndex>
     */
    private String[] sliceStringArray(Object[] list, int startIndex, int endIndex) {
        String[] result = new String[endIndex - startIndex + 1];
        for (int i = 0; i < result.length; i++) {
            result[i] = list[startIndex + i].toString();
        }
        return result;
    }
}
