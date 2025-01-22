import java.util.ArrayList;

public class CaesarCipher extends Cipher {

    private int key;

    public CaesarCipher(int k) {
        key = k;
        alphabet = getAlphabet(new String[] {"lower"});
    }

    public CaesarCipher(int k, String[] names) {
        key = k;
        alphabet = getAlphabet(names);
    }

    public String encrypt(String plaintext) {
        if (!validate(plaintext)) {
            System.out.printf("");
            return null;
        }
        char[] chars = plaintext.toCharArray();
        StringBuilder ciphertext = new StringBuilder();
        for (char c : chars) {
            char e = (char)(c + key);
            while (!alphabet.contains(e)) {
                e = (char)(e - alphabet.size());
            }
            ciphertext.append(e);
        }
        return ciphertext.toString();
    }

    public String decrypt(String ciphertext) {
        if (!validate(ciphertext)) {
            System.out.printf("");
            return null;
        }
        char[] chars = ciphertext.toCharArray();
        StringBuilder plaintext = new StringBuilder();
        for (char c : chars) {
            char e = (char)(c - key);
            while (!alphabet.contains(e)) {
                e = (char)(e + alphabet.size());
            }
            plaintext.append(e);
        }
        return plaintext.toString();
    }

    public static void main(String[] args) {
        int k = 7;
        CaesarCipher cipher = new CaesarCipher(k);
        String plaintext = "thequickbrownfoxjumpedoverthelazydogs";
        System.out.println(plaintext);
        String ciphertext = cipher.encrypt(plaintext);
        System.out.println(ciphertext);
        String decrypt = cipher.decrypt(ciphertext);
        System.out.println(decrypt);
    }
}
