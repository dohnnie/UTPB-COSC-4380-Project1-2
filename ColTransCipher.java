/* Columnar Transposition Cipher
 * Created by: Cameron Glenn, Johnny Ngo, and Kevin Franco
 * March 23, 2025
 */

import java.util.ArrayList;

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
    public ArrayList<String> splice(String ciphertext) {
        ArrayList<String> bigrams = new ArrayList<>();
        int length = 1;
        String bigram = "";
        for(int i = 0; i < ciphertext.length(); i++) {
           if(length > 2) 
                bigram = "";
            
            bigram += ciphertext.charAt(i);
        }
        bigrams.add(bigram);
        return bigrams;
    }

    /**
     * Sort the bigrams by frequency. Modifies the ArrayList in place. Removes duplicate bigrams after sorting.
     * @param bigrams The bigrams to sort.
     * @return The sorted bigrams. Duplicates should be removed.
     */
    public ArrayList<String> sortByFrequency(ArrayList<String> bigrams) {
        // Sort the bigrams by frequency
        ArrayList<String> sortedBigrams = new ArrayList<>();
        
        return sortedBigrams;
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
     * Crack the ciphertext passed to the function.
     * @param ciphertext The ciphertext to crack.
     * @return The plaintext.
     */
    public void crack(String ciphertext) {
        
        // When replacing bigrams with the most common bigrams, replace UPPER CASE.
        
    }

    ////////////////////////////////////////////////////////////////////
    /// Main                                                          //
    ////////////////////////////////////////////////////////////////////
    
    public static void main(String[] args) {
        ColTransCipher ctc = new ColTransCipher("35718", null, true, false);
        String plaintext = "thequickbrownfoxjumpedoverthelazydogs";
        System.out.println(plaintext);
        String ciphertext = ctc.encrypt(plaintext);
        System.out.println(ciphertext);
        String decrypted = ctc.decrypt(ciphertext);
        System.out.println(decrypted);
    }
}
