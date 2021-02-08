import java.io.*;
import java.util.*;

public class FileReader {

    FileReader() {
    };

    final int length = 28;

    int wordCounter = 0;

    String[] words;
    ArrayList<WordCharacteristic> finalwords = new ArrayList<WordCharacteristic>(wordCounter);

    float VowelsIndex(String str) {
        String vowels = "aeiouAEIOU";
        int vowelCount = 0, wlength = str.length();
        for (int i = 0; i < wlength; i++) {
            char currentChar = str.charAt(i);
            if (vowels.indexOf(currentChar) >= 0)
                vowelCount++;
        }
        return (float) vowelCount / wlength;

    }

    void read() throws FileNotFoundException {

        File file = new File("C:\\Users\\Дарина\\IdeaProjects\\w1\\src\\file.txt");
        Scanner scan = new Scanner(file);

        String fileContent = "";
        while (scan.hasNextLine()) {
            fileContent = fileContent.concat(scan.nextLine() + "\n");
        }
        words = fileContent.split("[()+-/0123456789#*—“”\"\'\\s.,;:!&?$’‘\\[\\]\\\\]+");
    }

    void findUniqueWords() {
        HashSet<String> uniqueWords = new HashSet<String>(Arrays.asList(words));
        wordCounter = uniqueWords.size();
        for (String s : uniqueWords) {
            if (s.length() <= 28 && s.length() > 0) {
                float temp = VowelsIndex(s);
                if (temp != 0)
                    finalwords.add(new WordCharacteristic(s, temp));
                else wordCounter--;
            }
            else wordCounter--;
        }
        Collections.sort(finalwords);
       //for (int i = 0; i < wordCounter; i++) {
         //   System.out.format("%28s%15f%n",finalwords.get(i).word,finalwords.get(i).index);
        //}
    }

    void write() throws IOException {
        FileWriter writer = new FileWriter("C:\\Users\\Дарина\\IdeaProjects\\w1\\src\\outputfile.txt");
        for (int i = 0; i < wordCounter; i++) {
            writer.write((String.format("%28s" ,finalwords.get(i).word)));
            writer.write((String.format("%15f%n" ,finalwords.get(i).index)));
        }
        writer.close();
    }


}

