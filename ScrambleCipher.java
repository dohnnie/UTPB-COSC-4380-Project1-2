import java.util.ArrayList;
import java.util.HashMap;

public class ScrambleCipher extends Cipher {

    ArrayList<Character> key = new ArrayList<>();

    public ScrambleCipher() {
        alphabet = getAlphabet(new String[] {"lower"});
        key = getPermutation();
    }

    public ScrambleCipher(String[] names) {
        alphabet = getAlphabet(names);
        key = getPermutation();
    }

    @Override
    public String encrypt(String plaintext) {
        char[] chars = plaintext.toCharArray();
        StringBuilder ciphertext = new StringBuilder();
        for (char c : chars) {
            int idx = alphabet.indexOf(c);
            if (idx < 0) {
                ciphertext.append(c);
                continue;
            }
            char e = key.get(idx);
            ciphertext.append(e);
        }
        return ciphertext.toString();
    }

    @Override
    public String decrypt(String ciphertext) {
        char[] chars = ciphertext.toCharArray();
        StringBuilder plaintext = new StringBuilder();
        for (char c : chars) {
            int idx = key.indexOf(c);
            if (idx < 0) {
                plaintext.append(c);
                continue;
            }
            char e = alphabet.get(idx);
            plaintext.append(e);
        }
        return plaintext.toString();
    }

    @Override
    public void crack(String ciphertext) {
        char[] chars = ciphertext.toCharArray();
        HashMap<Character, Integer> letterCounts = new HashMap<>();
        for (char c : chars) {
            if (!letterCounts.containsKey(c)) {
                letterCounts.put(c, 0);
            }
            int count = letterCounts.get(c);
            count += 1;
            letterCounts.put(c, count);
        }
        for (char c : letterCounts.keySet()) {
            System.out.printf("%c: %d%n", c, letterCounts.get(c));
        }
    }

    public void printKey() {
        for (char c : key) {
            System.out.print(c);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        ScrambleCipher cipher = new ScrambleCipher();
        cipher.printKey();
        //String plaintext = "The quick brown fox jumps over the lazy dog.";
        //System.out.println(plaintext);
        //String ciphertext = cipher.encrypt(plaintext);
        String ciphertext = "]31iO=3OP27\"T6vO]31iO=3OP27\"T6vO`WP8o7OV32TO\"X8WO8WP8OQ3$tO.O\"32ST6CO|39O]><iO=3OP27\"T6v";
        System.out.println(ciphertext);
        //String decrypted = cipher.decrypt(ciphertext);
        //System.out.println(decrypted);
        cipher.crack(ciphertext);
    }
}
