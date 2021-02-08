public class WordCharacteristic implements Comparable<WordCharacteristic> {
    String word = "";
    float index = 0;

    public WordCharacteristic(String str, float f){
        this.word += str;
        this.index = f;
    }

    @Override
    public int compareTo(WordCharacteristic other) {
        if(this.index == other.index) return 0;
        return this.index > other.index ? 1 : -1;
    }
}
