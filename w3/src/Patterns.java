import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Patterns {

    public static final String[] VALUES_KEY =  new String[]{ "abstract", "and", "array", "as", "break", "callable", "case", "catch", "class",
            "clone", "const", "continue", "declare", "default", "die", "do", "echo", "else", "elseif", "empty", "enddeclare", "endfor", "endforeach",
            "endif", "endswitch",	"endwhile",	"eval",	"exit",	"extends", "final",	"finally", "for",	"foreach",	"function", "global", "goto",
            "if", "implements",	"include", "include_once",	"instanceof", "insteadof","interface", "isset", "list", "namespace", "new", "or", "print",
            "private", "protected",	"public",	"require",	"require_once", "return",	"static",	"switch",	"throw",	"trait",
            "try", "unset", "use", "var",	"while", "xor",	"yield", "yield from"};
    public static final Character[] VALUES_PUN = new Character[] {'-','+','=','{','}','[',']',';',':','"','<','>','?',',','/',' ', '\n', '\t', '!','%','&','*','(',')'};

    public static final String[] VALUES_LIT = new String[]{"true", "false", "null"};



    private static Set<String> keywords = new HashSet<String>(Arrays.asList(VALUES_KEY));
    private static Set<Character> punctuation = new HashSet<Character>(Arrays.asList(VALUES_PUN));
    private static Set<String> literals = new HashSet<String>(Arrays.asList(VALUES_LIT));

    public static boolean isKeyword(String word)
    {
        return keywords.contains(word);
    }

    public static boolean isPunctuation(char word)
    {
        return punctuation.contains(word);
    }
    public static boolean isLiteral(String word)
    {
        return literals.contains(word);
    }

}