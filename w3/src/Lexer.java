import java.io.*;
import java.util.ArrayList;

public class Lexer {

    ArrayList<Token> tokens = new ArrayList<>();
    String buffer = "";
    State curState = State.START;
    boolean eof = false;
    int braces = 1;
    boolean addToBuff ;
    boolean flag = false;

    public Lexer(String filePath){
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            int c;

            while ((c = br.read()) != -1) {
                analyzeChar((char)c);
            }
            br.close();
            eof = true;
            // analyzeChar('\n');
            //  tokens.remove(tokens.size() - 1);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void analyzeChar(Character character){
        boolean finish = false;
        while (!finish) {
            //System.out.println(curState);
            //System.out.println(character);
            //System.out.println(buffer);
            addToBuff = true;
            switch (curState) {

                case START: {
                    if (character == '$') curState = State.VARIABLE_START;
                    else if ((character >= 'a' && character <= 'z') || ((character >= 'A' && character <= 'Z')))
                        curState = State.IDENTIFIER;
                    else if (character == '%' || character == '!' || character == '.')
                        curState = State.SIMPLE_OPER;
                    else if (character == '+' || character == '&' || character == '|' || character == '*' )
                        curState = State.MAY_DOUBLE_OPER;
                    else if (character == '>' || character == '<')
                        curState = State.COMPARE_OPER;
                    else if (character == '^' || character == '~')
                        curState = State.SINGLE_OPER;
                    else if (character >= '1' && character <= '9')
                        curState = State.NUMBER;
                    else if (character == '=')
                        curState = State.EQUAL;
                    else if (character == '0')
                        curState = State.ZERO;
                    else if (character == '/')
                        curState = State.COMMENT_START;
                    else if ( character == '#' )
                        curState = State.COMMENT;
                    else if (character == '-')
                        curState = State.MINUS;
                    else if (character == '\'')
                        curState = State.CHAR_LIT;
                    else if (character == '\"')
                        curState = State.STRING_LIT;
                    else {
                        if(Patterns.isPunctuation(character))
                            tokens.add(new Token(Type.PUNCTUATION, Character.toString(character)));
                        setStart();
                    }
                    finish = true;
                }
                break;

                case VARIABLE_START:{
                    if (!((character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z') || character == '_'))
                        curState = State.ERROR;
                    else curState = State.VARIABLE;
                }
                case VARIABLE:{
                    if (!((character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z') || character == '_' || (character >='0' && character <='9'))) {
                        if (Patterns.isPunctuation(character) || character == '.') {
                            tokens.add(new Token(Type.VARIABLE, buffer));
                            setStart();
                            finish = false;
                        } else {
                            tokens.add(new Token(Type.ERROR, buffer+character));
                            setStart();
                            finish = true;
                        }
                    } else
                        finish = true;
                }
                break;

                case IDENTIFIER: {
                    if (!((character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z') || character == '_' || (character >='0' && character <='9'))) {
                        if (Patterns.isPunctuation(character) || character == '.') {
                            tokens.add(new Token(Type.IDENTIFIER, buffer));
                            setStart();
                            finish = false;
                        } else {
                            tokens.add(new Token(Type.ERROR, buffer));
                            setStart();
                            finish = true;
                        }
                    } else
                        finish = true;
                }
                break;

                case SIMPLE_OPER: {
                    if (character == '=') {
                        tokens.add(new Token(Type.OPERATOR, buffer + character));
                        setStart();
                        finish = true;
                    } else {
                        tokens.add(new Token(Type.OPERATOR, buffer));
                        setStart();
                        finish = false;
                    }
                }
                break;

                case SINGLE_OPER :{
                    tokens.add(new Token(Type.OPERATOR, buffer));
                    setStart();
                    finish = true;
                }
                break;

                case MAY_DOUBLE_OPER: {
                    if (character == '=' || Character.toString(character).equals(buffer)) {
                        tokens.add(new Token(Type.OPERATOR, buffer + character));
                        setStart();
                        finish = true;
                    }
                    else {
                        tokens.add(new Token(Type.OPERATOR, buffer));
                        setStart();
                        finish = false;
                    }
                }
                break;

                case COMPARE_OPER:{
                    if (character=='<' || character =='>' || character == '=') {
                        tokens.add(new Token(Type.OPERATOR, buffer + Character.toString(character)));
                        setStart();
                        finish = true;
                    } else {
                        tokens.add(new Token(Type.OPERATOR, buffer));
                        setStart();
                        finish = false;
                    }
                }
                break;

                case EQUAL:{
                    if ( character =='>') {
                        tokens.add(new Token(Type.PUNCTUATION, buffer + character));
                        setStart();
                        finish = true;
                    }
                    else if ( character == '='){
                        curState = State.SIMPLE_OPER;
                        finish = true;
                    } else {
                        tokens.add(new Token(Type.OPERATOR, buffer));
                        setStart();
                        finish = false;
                    }
                }
                break;

                case COMMENT_START: {
                    if (character == '/') {
                        curState = State.COMMENT;
                        finish = true;
                    } else if (character == '=') {
                        tokens.add(new Token(Type.OPERATOR, buffer + character));
                        setStart();
                        finish = true;
                    } else if (character == '*') {
                        curState = State.MULTI_LINE_COMMENT;
                        finish = true;
                    } else {
                        tokens.add(new Token(Type.OPERATOR, buffer));
                        setStart();
                        finish = false;
                    }
                }
                break;

                case COMMENT: {
                    if (character == '\r' || character == '\n') {
                        tokens.add(new Token(Type.COMMENT, buffer));
                        setStart();
                        finish = false;
                    } else
                        finish = true;
                }
                break;

                case MULTI_LINE_COMMENT: {
                    if (character == '*') {
                        curState = State.MULTI_LINE_COMMENT_START;
                    }
                    else if (eof) {
                        tokens.add(new Token(Type.ERROR, buffer));
                        //tokens.add(new Token(Type.PUNCTUATION, "\n"));
                    }
                    finish = true;
                }
                break;

                case MULTI_LINE_COMMENT_START: {
                    if (character == '/') {
                        tokens.add(new Token(Type.COMMENT, buffer + character));
                        setStart();
                    } else {
                        curState = State.MULTI_LINE_COMMENT;
                    }
                    finish = true;
                }
                break;

                case NUMBER: {
                    if (character == '.') {
                        curState = State.NUMBER_D;
                        finish = true;
                    }
                    else if (!(character >= '0' && character <= '9')) {
                        checkEndOfNumber(character);
                        finish = false;
                    }
                    else if ((character >='a'&& character<='z')||(character >='A'&& character<='Z'))
                        curState = State.ERROR;
                    else
                        finish = true;
                }
                break;

                case NUMBER_D: {
                    if (character >= '0' && character <= '9') {
                        curState = State.NUMBER_DOT;
                        finish = true;
                    }
                    else {
                        checkEndOfNumber(character);
                        finish = false;
                    }
                }
                break;

                case NUMBER_DOT: {
                    if (!(character >= '0' && character <= '9')) {
                        checkEndOfNumber(character);
                        finish = false;
                    }
                    else
                        finish = true;
                }
                break;

                case MINUS: {
                    if (character == '=' || character == '-' || character == '>') {
                        tokens.add(new Token(Type.OPERATOR, buffer + character));
                        setStart();
                        finish = true;
                    }
                    else if (character >= '1' && character <= '9') {
                        curState = State.NUMBER;
                        finish = true;
                    } else if (character == '0') {
                        curState = State.ZERO;
                        finish = true;
                    } else {
                        tokens.add(new Token(Type.OPERATOR, buffer));
                        setStart();
                        finish = false;
                    }
                }
                break;

                case ZERO: {
                    if (character == '.') {
                        curState = State.NUMBER_D;
                    }
                    else if (character == 'B' || character == 'b') {
                        curState = State.BINARY;
                    }
                    else if (character == 'x' || character == 'X') {
                        curState = State.HEX;
                    }
                    else if (character >= '0' && character <= '7') {
                        curState = State.OCT;
                    }
                    else {
                        if(Patterns.isPunctuation(character)) {
                            tokens.add(new Token(Type.NUMBER, buffer));
                            tokens.add(new Token(Type.PUNCTUATION,Character.toString(character)));
                            setStart();
                        }
                        else {
                            curState = State.ERROR;
                        }
                    }
                    finish = true;
                }
                break;

                case HEX: {
                    if ((character >= '0' && character <= '9') || (character >= 'A' && character <= 'F') || (character >= 'a' && character <= 'f'))
                        curState = State.HEX_N;
                    else {
                        curState = State.ERROR;
                    }
                    finish = true;
                }
                break;

                case HEX_N: {
                    if (!((character >= '0' && character <= '9') || (character >= 'A' && character <= 'F') || (character >= 'a' && character <= 'f'))) {
                        checkEndOfNumber(character);
                        finish = false;}
                    else  finish = true;

                }
                break;

                case BINARY: {
                    if (character =='0' || character=='1')
                        curState = State.BINARY_N;
                    else {
                        curState = State.ERROR;
                    }
                    finish = true;
                }
                break;

                case BINARY_N: {
                    if (!(character =='0' || character=='1')) {
                        checkEndOfNumber(character);
                        finish = false;}
                    else  finish = true;

                }
                break;

                case OCT: {
                    if (character >= '0' && character <= '7')
                        curState = State.OCT_N;
                    else {
                        curState = State.ERROR;
                    }
                    finish = true;                }
                break;

                case OCT_N: {
                    if (!((character >= '0' && character <= '7'))) {
                        checkEndOfNumber(character);
                        finish = false;}
                    else  finish = true;

                }
                break;

                case CHAR_LIT: {
                    if (character =='\\') curState = State.CHAR_REC;
                    if (character == '\'') {
                        tokens.add(new Token(Type.LITERAL, buffer + Character.toString(character)));
                        setStart();
                    } else if (eof) {
                        tokens.add(new Token(Type.ERROR, buffer));
                        tokens.add(new Token(Type.PUNCTUATION, "\n"));
                    }

                    finish = true;
                }
                break;

                case CHAR_REC:{
                    if (character =='\'' || character =='\\')
                        buffer = buffer.substring(0, buffer.length()-1);
                    curState = State.CHAR_LIT;
                    finish = true;
                }
                break;

                case STRING_LIT: {
                    if (character =='\\') curState = State.STRING_REC;
                    if (character == '$') curState = State.SIMPLE_SYN;
                    if (character == '{') curState = State.COMPLEX_SYN;
                    if (character == '\"') {
                        if (flag && braces==0) {
                            if (buffer.contains("$")) tokens.add(new Token(Type.VARIABLE, buffer));
                            buffer = "";
                            curState = State.STR_ERR;
                        }
                        else{
                            tokens.add(new Token(Type.LITERAL, buffer + character));
                            setStart();}
                    } else if (eof) {
                        tokens.add(new Token(Type.ERROR, buffer));
                        tokens.add(new Token(Type.PUNCTUATION, "\n"));
                    }
                    finish = true;
                }
                break;

                case STRING_REC:{
                    if (character == 'n'|| character == 'r'||character == 't'){
                        char help = ' ';
                        switch (character){
                            case 'n' : help = '\n';
                                break;
                            case 't' : help = '\t';
                                break;
                            case 'r' : help = '\r';
                                break;
                        }
                        buffer = buffer.substring(0, buffer.length()-1);
                        character = help;
                    }
                    if (character == '\\'||character == '\"')
                        buffer = buffer.substring(0, buffer.length()-1);
                    curState = State.STRING_LIT;
                    finish = true;
                }
                break;


                case COMPLEX_SYN:{
                    if (character == '$') {
                        curState = State.CHECK_VAR_CB;
                        if (braces == 1) {
                            tokens.add(new Token(Type.LITERAL, buffer.substring(0, buffer.lastIndexOf("{"))));
                            //tokens.add(new Token(Type.PSEUDOOPERATOR, "."));
                            tokens.add(new Token(Type.PUNCTUATION, "{"));
                            flag = true;
                        }
                        buffer = "";
                        curState = State.CHECK_VAR_CB;
                    }
                    else if (character == '}') {
                        braces--;
                        //System.out.println("braces number"+braces);
                        if (braces==0){
                            if (buffer.length()>0)tokens.add(new Token(Type.VARIABLE, buffer));
                            tokens.add(new Token(Type.PUNCTUATION, "}"));
                            flag = false;
                            //tokens.add(new Token(Type.PSEUDOOPERATOR, "."));
                           // buffer = "\"";
                            addToBuff = false;
                            braces=1;
                            curState = State.STRING_LIT;

                        }
                        else {
                            tokens.add(new Token(Type.PUNCTUATION, "}"));
                            curState = State.COMPLEX_SYN;
                        }

                    }
                    else if (flag && character =='\"'){
                        if (buffer.contains("$"))tokens.add(new Token(Type.VARIABLE, buffer));
                        buffer="";
                        curState = State.STR_ERR;
                    }
                    else {
                        curState = State.STRING_LIT;
                    }
                    finish = true;
                }
                break;

                case CHECK_VAR_CB:{
                    if (!((character >='a' && character <='z')||(character >='A' && character <='Z')||(character >=0 && character <=9)|| character =='_'))
                    {
                        if (character =='[' || character == '-' || character =='{'|| character=='}'){
                            if (buffer.contains("$"))tokens.add(new Token(Type.VARIABLE, buffer.substring(buffer.lastIndexOf('$'), buffer.length())));
                            buffer="";
                            curState = State.CHECK_AFTER_VAR;
                            finish = false;
                        }
                        else {
                            // if (buffer.contains("$"))tokens.add(new Token(Type.VARIABLE, buffer));
                            //buffer = "";
                            curState = State.STR_ERR;
                            finish = true;
                        }
                    }
                    else finish = true;
                }
                break;

                case CHECK_AFTER_VAR:{
                    if (character == '['){
                        buffer = "";
                        curState = State.ARRAY;
                        finish = true;
                    }
                    else if (character =='-'){
                        curState = State.CLS;
                        finish = true;
                    }
                    else if (character == '{'){
                        braces++;
                        curState = State.STRING_LIT;
                        tokens.add(new Token(Type.PUNCTUATION, "{"));
                        buffer="";
                        addToBuff = false;
                        finish = false;

                    }
                    else if (character == '}'){
                        braces--;
                        if (braces==0){
                            if (buffer.contains("$"))tokens.add(new Token(Type.VARIABLE, buffer));
                            tokens.add(new Token(Type.PUNCTUATION, "}"));
                            //tokens.add(new Token(Type.PSEUDOOPERATOR, "."));
                            //buffer = "\"";
                            addToBuff = false;
                            braces=1;
                            curState = State.STRING_LIT;
                        }
                        else {
                            tokens.add(new Token(Type.PUNCTUATION, "}"));
                        }
                        finish = true;
                    }
                    else if (character =='\"') {
                        curState = State.STR_ERR;
                    }
                }
                break;

                case SIMPLE_SYN: {
                    if (character == ' ' || character == '$' || !((character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z') || character == '_')) {
                        curState = State.STRING_LIT;
                        finish = false;
                    } else {
                        curState = State.CHECK_VAR;
                        tokens.add(new Token(Type.LITERAL, buffer.substring(0, buffer.lastIndexOf('$'))));
                        //tokens.add(new Token(Type.PSEUDOOPERATOR, "."));
                        buffer = buffer.substring(buffer.lastIndexOf('$'), buffer.length());
                        finish = true;
                    }
                }
                break;

                case CHECK_VAR:{
                    if (character == '['){
                        curState = State.ARRAY;
                        // tokens.add(new Token(Type.PUNCTUATION, "["));
                        buffer = buffer.substring(buffer.lastIndexOf('$'), buffer.length());
                    }
                    if (character =='-'){
                        curState = State.CLS;
                        buffer = buffer.substring(buffer.lastIndexOf('$'), buffer.length());
                    }
                    if (character !='[' && character!='-' && !((character >='a' && character <='z') || (character >='A' && character <='Z') || character =='_')){
                        if (flag && character =='\"') curState = State.STR_ERR;
                        else {
                            tokens.add(new Token(Type.VARIABLE, buffer));

                            //tokens.add(new Token(Type.PSEUDOOPERATOR, "."));
                            if (character == '\"') {
                                tokens.add(new Token(Type.LITERAL, "\""));
                                setStart();
                            } else {
                                curState = State.STRING_LIT;
                                //buffer = "\"";
                            }
                        }

                    }
                    finish = true;

                }
                break;


                case CLS:{
                    if (character !='>') {
                        tokens.add(new Token(Type.VARIABLE, buffer.substring(0, buffer.lastIndexOf('-'))));
                        if (flag) curState = State.STR_ERR;
                        else {
                            //tokens.add(new Token(Type.PSEUDOOPERATOR, "."));
                            buffer = "\"-";
                            curState = State.STRING_LIT;
                        }
                    }
                    else {
                        curState = State.CL_ID;
                    }
                    finish = true;
                }
                break;

                case CL_ID :{
                    if (!((character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z') || character == '_' )){
                        if (character == '{'){
                            curState = State.CHECK_VAR_CB;
                            tokens.add(new Token(Type.PUNCTUATION, "->"));
                            buffer = "";
                        }else {
                            tokens.add(new Token(Type.VARIABLE, buffer.substring(0, buffer.lastIndexOf("-"))));
                            buffer = "\"->";
                            if (flag) curState = State.STR_ERR;
                            else {
                                //tokens.add(new Token(Type.PSEUDOOPERATOR, "."));

                                curState = State.STRING_LIT;
                            }
                        }
                    }
                    else curState = State.FIELD;
                }
                break;

                case FIELD:{
                    if (!((character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z') || character == '_' || (character >='0' && character <='9'))) {
                        if (Patterns.isPunctuation(character) || character == '.') {
                            if (flag && character =='\"') curState = State.STR_ERR;
                            else{
                                if (buffer.contains("$"))tokens.add(new Token(Type.VARIABLE, buffer.substring(buffer.lastIndexOf("$"), buffer.lastIndexOf('-'))));
                                tokens.add(new Token(Type.PUNCTUATION, "->"));
                                tokens.add(new Token(Type.IDENTIFIER, buffer.substring(buffer.lastIndexOf(">") + 1, buffer.length())));
                                if (!flag){
                                    //tokens.add(new Token(Type.PSEUDOOPERATOR, "."));
                                    if (character=='\"'){
                                        tokens.add(new Token(Type.LITERAL, "\"\""));
                                        setStart();
                                    }
                                    else {
                                        //buffer = "\"";
                                        curState = State.STRING_LIT;
                                    }
                                    finish = true;}
                                else {
                                    buffer = "";
                                    addToBuff = false;
                                    curState = State.CHECK_VAR_CB;
                                    finish = false;
                                }
                            }
                        }}
                    else finish = true;
                }
                break;

                case ARRAY:{
                    if ((character>='0' && character<='9')|| character == '-') {
                        if (buffer.contains("$"))tokens.add(new Token(Type.VARIABLE, buffer.substring(0, buffer.lastIndexOf('['))));
                        tokens.add(new Token(Type.PUNCTUATION, "["));
                        buffer = "";
                        curState = State.IND;
                    }
                    else if ((character >='a' && character <='z') || (character >='A' && character <='Z') || character =='_') {
                        if (buffer.contains("$"))tokens.add(new Token(Type.VARIABLE, buffer.substring(0, buffer.lastIndexOf('['))));
                        tokens.add(new Token(Type.PUNCTUATION, "["));
                        buffer = "";
                        curState = State.KEY;
                    }
                    else if (character =='$'){
                        curState = State.VAR_AR_S;
                    }
                    else {
                        tokens.add(new Token(Type.ERROR, buffer+ character));
                        buffer = "";
                        addToBuff = false;
                        curState = State.STRING_LIT;
                    }
                    finish = true;
                }
                break;

                case VAR_AR_S:{
                    if (!((character >='a' && character <='z') || (character >='A' && character <='Z') || character =='_')){
                        curState = State.STR_ERR;
                    }
                    else curState = State.VAR_AR;
                    finish = true;
                }
                break;

                case VAR_AR:{
                    if (!((character >='a' && character <='z') || (character >='A' && character <='Z') || character =='_')){
                        if (character ==']') {
                            buffer +=character;
                            if (!flag)tokens.add(new Token(Type.VARIABLE, buffer.substring(0, buffer.lastIndexOf('['))));
                            tokens.add(new Token(Type.PUNCTUATION, "["));
                            tokens.add(new Token(Type.VARIABLE, buffer.substring(buffer.lastIndexOf('$'), buffer.lastIndexOf(']'))));
                            tokens.add(new Token(Type.PUNCTUATION, "]"));
                            if (!flag) {
                                buffer = buffer.substring(buffer.lastIndexOf('['), buffer.length());
                                //tokens.add(new Token(Type.PSEUDOOPERATOR, "."));
                                curState = State.STRING_LIT;
                                //buffer = "\"";
                                addToBuff = false;
                            }
                            else {
                                curState = State.CHECK_VAR_CB;
                                buffer = "";
                            }
                        }

                        else if (character =='\"'){
                            if (buffer.contains("$")) tokens.add(new Token(Type.VARIABLE, buffer));
                            buffer = "";
                            curState = State.STR_ERR;
                        }
                    }
                    finish = true;
                }
                break;

                case STR_ERR:{
                    if (!((character >='a' && character <='z') || (character >='A' && character <='Z') || character =='_')) {
                        if (!flag)
                            tokens.add(new Token(Type.ERROR, buffer));
                        else {
                            if (buffer.contains("\"") )
                                tokens.add(new Token(Type.ERROR, buffer));
                            else if (buffer.contains("$"))  tokens.add(new Token(Type.VARIABLE, buffer));
                        }
                        if (character == ']' || character == '}')
                            tokens.add(new Token(Type.PUNCTUATION, Character.toString(character)));
                        if (flag && braces != 0) {
                            if (!buffer.contains("\""))tokens.add(new Token(Type.ERROR, "\""));
                            //tokens.add(new Token(Type.PSEUDOOPERATOR, "."));
                            //buffer = "\"";
                            addToBuff = false;
                            curState = State.STRING_LIT;
                        }
                        else {
                            //tokens.add(new Token(Type.PSEUDOOPERATOR, "."));
                            //buffer = "\"";
                            addToBuff = false;
                            curState = State.STRING_LIT;
                        }
                    }
                    finish = true;
                }
                break;

                case KEY:{
                    if (!((character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z') || character == '_' || (character >='0' && character <='9'))) {
                        if (character == ']') {
                            tokens.add(new Token(Type.IDENTIFIER, "\'"+ buffer));
                            tokens.add(new Token(Type.PUNCTUATION, "]"));
                            if (!flag) {
                                //tokens.add(new Token(Type.PSEUDOOPERATOR, "."));
                                //buffer = "\"";
                                addToBuff = false;
                                curState = State.STRING_LIT;
                            }
                            else curState = State.CHECK_VAR_CB;
                        } else {
                            tokens.add(new Token(Type.ERROR, buffer));
                            curState = State.STRING_LIT;
                            buffer = "";
                            addToBuff = false;
                        }
                    }
                    finish = true;

                }
                break;

                case IND:{
                    if (!((character >= '0' && character <= '9'))) {
                        if (character == ']') {
                            tokens.add(new Token(Type.NUMBER, buffer));
                            tokens.add(new Token(Type.PUNCTUATION, "]"));
                            if (!flag) {
                                //tokens.add(new Token(Type.PSEUDOOPERATOR, "."));
                               // buffer = "\"";
                                addToBuff = false;
                                curState = State.STRING_LIT;
                            }
                            else {
                                buffer = "";
                                addToBuff = false;
                                curState = State.CHECK_VAR_CB;
                            }
                        } else {
                            curState = State.STR_ERR;
                            buffer = buffer+'\'';
                        }
                    }
                    finish = true;
                }
                break;


                case ERROR: {
                    if (Patterns.isPunctuation(character)) {
                        tokens.add(new Token(Type.ERROR, buffer));
                        setStart();
                        finish = false;
                    }
                    else{
                        finish = true;}
                }
                break;
                default:
                    break;
            }
        }

        if(curState != State.START) {
            if (addToBuff){
                buffer += character;}
        }
    }

    public void setStart(){
        buffer = "";
        curState = State.START;
    }

    public void checkEndOfNumber(Character character){
       // System.out.println(curState);
       // System.out.println(character);
       // System.out.println(buffer);
        if(Patterns.isPunctuation(character)) {
            tokens.add(new Token(Type.NUMBER, buffer));
            setStart();
        }
        else {
            curState = State.ERROR;
        }

    }

    public void printTokens(){
        try(FileWriter writer = new FileWriter("result.txt", false))
        {

            for (Token t: tokens)
                if (!(t.getTokenType() == Type.PUNCTUATION && (t.getContent().equals(" ")||(t.getContent().equals("\\n")))))
                    writer.write(t.getTokenType().toString() + ": " + t.getContent()+'\n');
            writer.flush();
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }


    public void printAll(){
        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_BLACK = "\u001B[30m";
        final String ANSI_RED = "\u001B[31m";
        final String ANSI_GREEN = "\u001B[32m";
        final String ANSI_YELLOW = "\u001B[33m";
        final String ANSI_BLUE = "\u001B[34m";
        final String ANSI_PURPLE = "\u001B[35;1m";
        final String ANSI_CYAN = "\u001B[36m";
        final String ANSI_WHITE = "\u001B[37m";
        final String ANSI_MAGENTA= "\u001b[35m";
        final String ANSI_BACK_BLUE = "\u001b[44;1m";
        final String ANSI_BACK_RED = "\u001b[41;1m";
        final String ANSI_DEEP_BLUE = "\u001B[34;1m";

        System.out.println(ANSI_CYAN + "OPERATOR");
        System.out.println(ANSI_BLUE + "KEYWORD");
        System.out.println(ANSI_GREEN + "VARIABLE");
        System.out.println(ANSI_YELLOW + "COMMENT");
        System.out.println(ANSI_PURPLE + "IDENTIFIER");
        System.out.println(ANSI_WHITE + "LITERAL");
        System.out.println(ANSI_BACK_BLUE + "NUMBER"+ANSI_RESET);
        System.out.println(ANSI_BACK_RED + "ERROR" + ANSI_RESET+'\n');
        System.out.println("*********************************************************************************************");

        for (Token t: tokens){
            if (t.getContent().equals("\\n")) t.setContent("\n");
            if(t.getTokenType().toString()=="KEYWORD")
                System.out.print(ANSI_BLUE + t.getContent()+ ANSI_RESET);
            else if(t.getTokenType().toString()=="VARIABLE")
                System.out.print(ANSI_GREEN + t.getContent()+ ANSI_RESET);
            else if(t.getTokenType().toString()=="COMMENT")
                System.out.print(ANSI_YELLOW + t.getContent()+ ANSI_RESET);
            else if(t.getTokenType().toString()=="OPERATOR")
                System.out.print(ANSI_CYAN + t.getContent()+ ANSI_RESET);
            else if(t.getTokenType().toString()=="IDENTIFIER")
                System.out.print(ANSI_PURPLE + t.getContent()+ ANSI_RESET);
            else if(t.getTokenType().toString()=="LITERAL")
                System.out.print(ANSI_WHITE + t.getContent()+ ANSI_RESET);
            else if(t.getTokenType().toString()=="NUMBER")
                System.out.print(ANSI_BACK_BLUE + t.getContent()+ ANSI_RESET);
            else if(t.getTokenType().toString()=="ERROR")
                System.out.print(ANSI_BACK_RED + t.getContent()+ ANSI_RESET);
            else
                System.out.print(ANSI_DEEP_BLUE + t.getContent()+ ANSI_RESET);
        }
    }

}