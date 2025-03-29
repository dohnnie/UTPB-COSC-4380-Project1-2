/* Columnar Transposition Cipher
 * Created by: Cameron Glenn, Johnny Ngo, and Kevin Franco
 * March 23, 2025
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ColTransCipher extends Cipher {

    ArrayList<Character> key = new ArrayList<>();
    ArrayList<Character> keyOrder = new ArrayList<>();

    // Controls
    private boolean padding = true; // Controls padding of null characters

    private boolean debug = false; // Controls debug output (verbose)

    ////////////////////////////////////////////////////////////////////
    /// Constructors                                                  //
    ////////////////////////////////////////////////////////////////////
    
    public ColTransCipher(int k, String[] names, boolean ascending, boolean strict) {
        if (names == null) {
            ArrayList<Character> alphabet = getAlphabet(new String[] {"lower"});
        } else {
            ArrayList<Character> alphabet = Cipher.getAlphabet(names);
        }
        String t = "" + k;
        if (strict) {
            setKey(sanitize(t), ascending);
        } else {
            setKey(t, ascending);
        }
    }

    public ColTransCipher(int[] k, String[] names, boolean ascending, boolean strict) {
        if (names == null) {
            this.alphabet = getAlphabet(new String[] {"lower"});
        } else {
            this.alphabet = Cipher.getAlphabet(names);
        }
        StringBuilder sb = new StringBuilder();
        for (int i : k) {
            sb.append((char) i);
        }
        if (strict) {
            setKey(sanitize(sb.toString()), ascending);
        } else {
            setKey(sb.toString(), ascending);
        }
    }

    public ColTransCipher(String k, String[] names, boolean ascending, boolean strict) {
        if (names == null) {
            this.alphabet = getAlphabet(new String[] {"lower"});
        } else {
            this.alphabet = Cipher.getAlphabet(names);
        }
        if (strict) {
            setKey(sanitize(k), ascending);
        } else {
            setKey(k, ascending);
        }
    }

    ////////////////////////////////////////////////////////////////////
    /// Functions                                                     //
    ////////////////////////////////////////////////////////////////////
    
    /**
     * Sanitize the key by removing spaces, newlines, carriage returns, and tabs.
     * @param k The key or text to sanitize.
     * @return The sanitized key.
     */
    private String sanitize(String k) {
        String t = k.toLowerCase();
        t = t.replace(" ", "");
        t = t.replace("\n", "");
        t = t.replace("\r", "");
        t = t.replace("\t", "");
        return t;
    }

    /**
     * Set the key for the cipher.
     * @param k The key to set
     * @param ascending Whether to sort the key in ascending order.
     */
    private void setKey(String k, boolean ascending) {
        char[] chars = k.toCharArray();
        ArrayList<Character> keyList = new ArrayList<>();
        ArrayList<Character> sortedList = new ArrayList<>();
        for (char c : chars) {
            if (!keyList.contains(c)) {
                keyList.add(c);
                sortedList.add(c);
            }
        }
        if (ascending) {
            sortedList.sort((a, b) -> Character.compare(a.charValue(), b.charValue()));
        } else {
            sortedList.sort((a, b) -> Character.compare(b.charValue(), a.charValue()));
        }

        this.key = keyList;
        if (debug) {
            for (char c : key) {
                System.out.print(c);
            }
            System.out.println();
        }
        this.keyOrder = sortedList;
        if (debug) {
            for (char c : this.keyOrder) {
                System.out.print(c);
            }
            System.out.println();
        }
    }

    @Override
    /**
     * Encrypt the plaintext using the columnar transposition cipher.
     * @param plaintext The plaintext to encrypt.
     * @return The encrypted ciphertext.
     */
    public String encrypt(String plaintext) {
        int cols = key.size();
        int rows = (int)(Math.ceil(plaintext.length() / (double)cols));

        char[] chars = plaintext.toCharArray();
        char[][] beta = new char[rows][cols];

        for (int i = 0; i < chars.length; i++) {
            beta[i/cols][i%cols] = chars[i];
        }

        if (debug) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    System.out.printf("%c ", beta[r][c]);
                }
                System.out.println();
            }
        }

        char[][] orderedBeta = new char[rows][cols];
        for (int c = 0; c < cols; c++) {
            int order = key.indexOf(keyOrder.get(c));
            for (int r = 0; r < rows; r++) {
                orderedBeta[r][c] = beta[r][order];
            }
        }

        if (debug) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    System.out.printf("%c ", orderedBeta[r][c]);
                }
                System.out.println();
            }
        }

        char[][] transpose = new char[cols][rows];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                transpose[c][r] = orderedBeta[r][c];
            }
        }

        if (debug) {
            for (int c = 0; c < cols; c++) {
                for (int r = 0; r < rows; r++) {
                    System.out.printf("%c ", transpose[c][r]);
                }
                System.out.println();
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows * cols; i++) {
            if ((int)transpose[i/rows][i%rows] == 0x00) {
                if (padding) {
                    sb.append(alphabet.get(Rand.randInt(alphabet.size())));
                    continue;
                } else {
                    continue;
                }
            }
            sb.append(transpose[i/rows][i%rows]);
        }

        return sb.toString();
    }

    @Override
    /**
     * Decrypt the ciphertext using the columnar transposition cipher.
     * @param ciphertext The ciphertext to decrypt.
     * @return The decrypted plaintext.
     */
    public String decrypt(String ciphertext) {
        int cols = key.size();
        int rows = (int)(Math.ceil(ciphertext.length() / (double)cols));

        char[] chars = ciphertext.toCharArray();

        char[][] transpose = new char[cols][rows];
        for (int i = 0; i < ciphertext.length(); i++) {
                transpose[i/rows][i%rows] = chars[i];
        }

        if (debug) {
            for (int c = 0; c < cols; c++) {
                for (int r = 0; r < rows; r++) {
                    System.out.printf("%c ", transpose[c][r]);
                }
                System.out.println();
            }
        }

        char[][] orderedBeta = new char[rows][cols];
        for (int c = 0; c < cols; c++) {
            for (int r = 0; r < rows; r++) {
                orderedBeta[r][c] = transpose[c][r];
            }
        }

        if (debug) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    System.out.printf("%c ", orderedBeta[r][c]);
                }
                System.out.println();
            }
        }

        char[][] beta = new char[rows][cols];

        for (int c = 0; c < cols; c++) {
            int order = keyOrder.indexOf(key.get(c));
            for (int r = 0; r < rows; r++) {
                beta[r][c] = orderedBeta[r][order];
            }
        }

        if (debug) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    System.out.printf("%c ", beta[r][c]);
                }
                System.out.println();
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows * cols; i++) {
            if ((int)beta[i/cols][i%cols] == 0x00) {
                if (padding) {
                    sb.append(alphabet.get(Rand.randInt(alphabet.size())));
                    continue;
                } else {
                    continue;
                }
            }
            sb.append(beta[i/cols][i%cols]);
        }

        return sb.toString();
    }

    /**
     * Splice the ciphertext into each of it's bigrams, returns an ArrayList.
     * @param ciphertext The ciphertext to splice.
     * @return ArrayList of Strings for bigrams.
     */
    public static ArrayList<String> splice(String ciphertext) {
        ArrayList<String> bigrams = new ArrayList<>();
        for(int i = 0; i < ciphertext.length(); i++ ) {
            if(i + 2 < ciphertext.length())
                bigrams.add(ciphertext.substring(i, i+2));
            else if(i != ciphertext.length() - 1)
                bigrams.add(ciphertext.substring(i));
        }

        return bigrams;
    }

    /**
     * Sort a HashMap by its values. Only works for Integer values.
     * @param map The HashMap to sort.
     * @return A new HashMap sorted by values.
     */
    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> map) {
        // Convert the map to a list of entries
        List<HashMap.Entry<String, Integer>> entryList = new ArrayList<>(map.entrySet());

        // Sort the list by values
        entryList.sort(HashMap.Entry.comparingByValue());

        // Rebuild the sorted map (LinkedHashMap to maintain insertion order)
        HashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        for (HashMap.Entry<String, Integer> entry : entryList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    /**
     * Sort a HashMap by its values. Only works for Float values.
     * @param map The HashMap to sort.
     * @return A new HashMap sorted by values.
     */
    public static HashMap<String, Float> sortByFloatValue(HashMap<String, Float> map) {
        // Convert the map to a list of entries
        List<HashMap.Entry<String, Float>> entryList = new ArrayList<>(map.entrySet());

        // Sort the list by values
        entryList.sort(HashMap.Entry.comparingByValue());

        // Rebuild the sorted map (LinkedHashMap to maintain insertion order)
        HashMap<String, Float> sortedMap = new LinkedHashMap<>();
        for (HashMap.Entry<String, Float> entry : entryList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }



    /**
     * Sort the bigrams by frequency. Modifies the ArrayList in place. Removes duplicate bigrams after sorting.
     * @param bigrams The bigrams to sort.
     * @return The sorted bigrams. Duplicates should be removed.
     */
    public static HashMap<String, Integer> sortByFrequency(ArrayList<String> bigrams) {
        // Sort the bigrams by frequency
        ArrayList<String> encounteredBigrams = new ArrayList<>(); // Stores what bigrams have been encountered
        HashMap<String, Integer> frequencies = new HashMap<>(); // Stores key value pairs of bigram and frequency.

        // Count the frequency of each bigram
        for (String bigram : bigrams) {
            // If the bigram is not already in the sorted list, add it and count its frequency
            if (!(encounteredBigrams.contains(bigram))) { // New Bigram Encounter
                encounteredBigrams.add(bigram); // Add the bigram to the list of encountered bigrams
                frequencies.put(bigram, 1); // Add the first time bigram to the list
            } else { // Bigram already encountered
                frequencies.put(bigram, frequencies.get(bigram) + 1); // Increment the frequency of the bigram
            }
        }
        
        return sortByValue(frequencies);
    }

    /**
     * Get the total number of bigrams in the ciphertext to enable us to get frequency percentages.
     * @param frequencies The frequencies of the bigrams.
     * @return The total number of bigrams.
     */
    public static int getTotalBigramCount(HashMap<String, Integer> frequencies) {
        int total = 0;
        for (String bigram : frequencies.keySet()) {
            total += frequencies.get(bigram);
        }
        return total;
    }

    /**
     * Normalize the frequencies of the bigrams to decimal percentages.
     * @param frequencies The frequencies of the bigrams.
     * @return A HashMap of normalized frequencies.
     */
    public static HashMap<String, Float> normalizeFrequencies(HashMap<String, Integer> frequencies) {
        HashMap<String, Float> normalizedFrequencies = new HashMap<>();
        int total = getTotalBigramCount(frequencies);
        for (String bigram : frequencies.keySet()) {
            float frequency = (float) frequencies.get(bigram) / (float) total; // Normalize the frequency
            normalizedFrequencies.put(bigram, frequency); // Add the normalized frequency to the map
        }
        return sortByFloatValue(normalizedFrequencies); // Return the normalized frequencies sorted by value
    }

    /**
     * Get the bigrams from bigrams.txt and creates a new list matching the passed through bigrams. 
     * @param cipherBigrams The sorted bigrams from the ciphertext.
     * @return ArrayList of Strings for bigrams.
     */
    public ArrayList<String> getBigrams(ArrayList<String> cipherBigrams) {
        ArrayList<String> bigrams = new ArrayList<>();
        
        return bigrams;
    }

    /**
     * Replace the bigrams in the ciphertext with the most common bigrams. Replaces all instances with UPPER CASE.
     * @param ciphertext The ciphertext to replace bigrams in.
     * @param cipherBigrams The bigrams from the ciphertext.
     * @param bigrams The bigrams to replace with.
     * @return The plaintext.
     */
    public String replaceByBigrams(String ciphertext, ArrayList<String> cipherBigrams, ArrayList<String> bigrams) {
        String plaintext = "";
        
        return plaintext;
    }

    /**
     * Load the bigrams from a file and return them as a HashMap.
     * @param filePath The path to the file containing the bigrams.
     * @return A HashMap of bigrams and their frequencies.
     */
    public static HashMap<String, Float> loadBigrams(String filePath) {
        HashMap<String, Float> bigramMap = new HashMap<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Assuming each line is in the format "bigram frequency"
                String[] parts = line.split("\\s+");
                if (parts.length == 2) { // Ensure there are two parts
                    bigramMap.put(parts[0], Float.parseFloat(parts[1])); // Add the bigram and its frequency to the map
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return bigramMap;
    }

    /**
     * Score the bigrams based on their frequency in the ciphertext and the dictionary. Gets the absolute value of the difference.
     * @param bigrams The bigrams from the ciphertext.
     * @param bigramDictionary The dictionary of bigrams and their frequencies.
     * @return The score of the bigrams.
     */
    public static float scoreBigrams(HashMap<String, Float> bigrams, HashMap<String, Float> bigramDictionary) {
        float totalScore = 0.0f;
        for (String bigram : bigrams.keySet()) {
            if (bigramDictionary.containsKey(bigram)) { // Ensure the bigram is in the dictionary
                // Calculate the score based on the frequency of the bigram in the ciphertext and the dictionary
                totalScore += Math.abs(bigrams.get(bigram) - bigramDictionary.get(bigram)); // Add the score to the total
            }
        }
        return totalScore;
    }
    
    /**
     * Get the ciphertext matrix from the ciphertext and key length.
     * @param ciphertext The ciphertext to get the matrix from.
     * @param keyLength The length of the key.
     * @return The ciphertext matrix.
     */
    public static char[][] getCiphertextMatrix(String ciphertext, int keyLength) {
        int rows = (int)(Math.ceil((double)ciphertext.length() / (double)keyLength));
        char[][] matrix = new char[rows][keyLength];
        for (int i = 0; i < ciphertext.length(); i++) {
            matrix[i/keyLength][i%keyLength] = ciphertext.charAt(i);
        }
        return matrix;
    }

    /**
     * Crack the ciphertext passed to the function.
     * @param ciphertext The ciphertext to crack.
     * @return The plaintext.
     */
    public static void crack(String ciphertext) {
        
        // When replacing bigrams with the most common bigrams, replace UPPER CASE.

        ArrayList<String> bigrams = splice("thisisateststring");

        HashMap<String, Integer> sortedFrequencies = sortByFrequency(bigrams);
        
        System.out.println(sortedFrequencies);

        int total = getTotalBigramCount(sortedFrequencies);
        System.out.println("Total: " + total);

        HashMap<String, Float> normalizedFrequencies = normalizeFrequencies(sortedFrequencies);
        System.out.println(normalizedFrequencies);

        HashMap<String, Float> bigramDictionary = loadBigrams("./bigrams.txt"); // Load bigrams from file using relative path
        System.out.println(bigramDictionary.get("te"));

        float score = scoreBigrams(normalizedFrequencies, bigramDictionary);
        System.out.println("Score: " + score);

        char[][] matrix = getCiphertextMatrix("abcdefghijklm", 3);

        // Print out matrix of ciphertext
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        
        // for(String bigram : bigrams) {
        //     System.out.print(bigram + " ");
        // }
        
    }

    public static int findKeyLength(String text) {
        int maxKeyLength = Math.min(20, text.length() / 2); // Limit the search range
        double expectedIC = 0.068; // Approximate IC for English text
        int bestKey = 1;
        double closestDiff = Double.MAX_VALUE;

        for (int keyLength = 2; keyLength <= maxKeyLength; keyLength++) {
            double ic = averageIC(text, keyLength);
            double diff = Math.abs(expectedIC - ic);

            if (diff < closestDiff) {
                closestDiff = diff;
                bestKey = keyLength;
            }
        }

        return bestKey;
    }

    private static double averageIC(String text, int keyLength) {
        double totalIC = 0.0;
        int validColumns = 0;

        for (int i = 0; i < keyLength; i++) {
            StringBuilder column = new StringBuilder();
            for (int j = i; j < text.length(); j += keyLength) {
                column.append(text.charAt(j));
            }
            double ic = calculateIC(column.toString());
            if (!Double.isNaN(ic)) {
                totalIC += ic;
                validColumns++;
            }
        }

        return validColumns > 0 ? totalIC / validColumns : 0.0;
    }

    private static double calculateIC(String text) {
        if (text.length() < 2) return Double.NaN;

        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : text.toCharArray()) {
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }

        double sum = 0.0;
        for (int count : freqMap.values()) {
            sum += count * (count - 1);
        }

        return sum / (text.length() * (text.length() - 1));
    }


    ////////////////////////////////////////////////////////////////////
    /// Main                                                          //
    ////////////////////////////////////////////////////////////////////
    
    public static void main(String[] args) {
        ColTransCipher ctc = new ColTransCipher("2123", null, true, false);
        String plaintext = "thequickbrownfoxjumpedoverthelazydogs";
        System.out.println(plaintext);
        String ciphertext = ctc.encrypt(plaintext);
        System.out.println(ciphertext);
        String decrypted = ctc.decrypt(ciphertext);
        System.out.println(decrypted);

        int length = findKeyLength(ciphertext);
        System.out.println("Key Length: " + length);

        crack("thisisateststring");
        
    }
}
