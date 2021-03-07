import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
	Machine mach = new Machine();
	mach.ReadFile();
	mach.split();
    }
}
