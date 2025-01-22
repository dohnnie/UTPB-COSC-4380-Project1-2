import java.util.ArrayList;
import java.util.Locale;

public abstract class Cipher {
    char[] lowercaseAlpha = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    char[] uppercaseAlpha = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    char[] numbers = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    char[] symbols = {'@', '#', '$', '%', '^', '&', '*', '(', ')', '{', '}', '[', ']', '-', '_', '=', '+', '<', '>', '|', '\\', '`', '~'};
    char[] punctuation = {'.', ',', '!', '?', '\'', '\"', ';', ':', '/'};
    char[] whitespace = {' ', '\t', '\n', '\r'};
    protected ArrayList<Character> alphabet;

    public Cipher() {

    }

    char[] getAlphabet(String name) {
        return switch (name.toLowerCase()) {
            case "lowercasealpha", "lowercase", "lower" -> lowercaseAlpha;
            case "uppercasealpha", "uppercase", "upper" -> uppercaseAlpha;
            case "numbers", "nums" -> numbers;
            case "symbols", "syms", "math" -> symbols;
            case "punctuation" -> punctuation;
            case "whitespace", "space", "spaces" -> whitespace;
            default -> new char[] {};
        };
    }

    ArrayList<Character> concatAlphabet(ArrayList<Character> a, char[] b) {
        for (char c : b) {
            a.add(c);
        }
        return a;
    }

    ArrayList<Character> getAlphabet(String[] names) {
        ArrayList<Character> a = new ArrayList<>();
        for (String name : names) {
            a = concatAlphabet(a, getAlphabet(name));
        }
        return a;
    }

    boolean validate(String s) {
        for (char c : s.toCharArray()) {
            if (!alphabet.contains(c)) {
                return false;
            }
        }
        return true;
    }

    public abstract String encrypt(String plaintext);
    public abstract String decrypt(String ciphertext);
}
