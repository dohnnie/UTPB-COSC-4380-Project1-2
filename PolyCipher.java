import java.util.ArrayList;
import java.util.HashMap;

public class PolyCipher extends Cipher {

    private String key;
    private char[][] square;

    public PolyCipher(String k) {
        key = k;
        alphabet = getAlphabet(new String[] {"lower"});
        square = new char[alphabet.size()][alphabet.size()];
    }

    public PolyCipher(String k, String[] names) {
        key = k;
        alphabet = getAlphabet(names);
        square = new char[alphabet.size()][alphabet.size()];
    }

    @Override
    public String encrypt(String plaintext) {
        return null;
    }

    @Override
    public String decrypt(String ciphertext) {
        return null;
    }

    public void generateSquare() {
        CaesarCipher generator = new CaesarCipher(0, alphabet);
        StringBuilder plaintext = new StringBuilder();
        for (char c : alphabet) {
            plaintext.append(c);
        }
        for (int row = 0; row < square.length; row++) {
            square[row] = generator.encrypt(plaintext.toString()).toCharArray();
            generator.setKey(row+1);
        }
    }

    public void scrambleSquare() {
        for(int row = 0; row < square.length * 10; row++) {
            int a = (int)(Math.random() * square.length);
            int b = (int)(Math.random() * square.length);
            char[] swap = square[a];
            square[a] = square[b];
            square[b] = swap;
        }
        for (int col = 0; col < square.length * 10; col++) {
            int a = (int)(Math.random() * square.length);
            int b = (int)(Math.random() * square.length);
            for(int row = 0; row < square.length; row++) {
                char c = square[row][a];
                square[row][a] = square[row][b];
                square[row][b] = c;
            }
        }
    }

    public void printSquare() {
        for (int row = 0; row < square.length; row++) {
            for (int col = 0; col < square.length; col++) {
                System.out.print(square[row][col]);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        PolyCipher cipher = new PolyCipher("thequickbrownfoxjumpsoverthelazydog", new String[]{"lower", "upper", "numbers", "punctuation"});
        cipher.generateSquare();
        cipher.printSquare();
        cipher.scrambleSquare();
        cipher.printSquare();
    }
}
