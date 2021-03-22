public class Main {
    public static void main(String[] args) {
        Lexer lexer = new Lexer("C:\\Users\\Дарина\\IdeaProjects\\w3\\src\\test2.txt");
        lexer.printTokens();
        lexer.printAll();
    }
}