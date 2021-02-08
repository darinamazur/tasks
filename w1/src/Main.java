import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {


    public static void main(String[] args) throws IOException {
        FileReader f = new FileReader();
        f.read();
        f.findUniqueWords();
        f.write();
    }
}