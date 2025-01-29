import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Dictionary {

    ArrayList<String> dict = new ArrayList<>();

    public Dictionary() {
        try {
            Scanner scan = new Scanner(new File("dict.txt"));
            while(scan.hasNext()) {
                dict.add(scan.nextLine());
            }
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
    }

    public boolean isWord(String word) {
        return dict.contains(word.toLowerCase());
    }
}
