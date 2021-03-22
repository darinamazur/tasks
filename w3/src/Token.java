public class Token {
    private String content;
    private Type tokenType;

    public Token(Type tokenType, String content){
        this.content = content;
        //System.out.println(tokenType + content);
        if(tokenType.equals(Type.IDENTIFIER)){
            if(Patterns.isLiteral(content.toLowerCase())){
                this.tokenType = Type.LITERAL;
            }
            else
            if(Patterns.isKeyword(content)){

                if (content.equals("xor") || content.equals("or") || content.equals("and"))
                    this.tokenType = Type.OPERATOR;
                else this.tokenType = Type.KEYWORD;
            }
            else
                this.tokenType = tokenType;
        }  else
            this.tokenType = tokenType;

        if(tokenType.equals(Type.PUNCTUATION)){
            switch (content) {
                case "\n":
                    this.content = "\\" + "n";
                    break;
                case "\t":
                    this.content = "\\" + "t";
                    break;
            }
        }

    }

    public Type getTokenType() {
        return tokenType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String cont){
        content = cont;
    }
}