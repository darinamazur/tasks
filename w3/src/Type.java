public enum Type {
    PUNCTUATION,// all
    OPERATOR,
    /*
    +    -    *    /    %    ++    --
    =    +=   -=   *=   /=   .=    %=
    ==   !=   ><   >   <   >=   <=
    &&   ||   !   xor   or  and <<   >>
    &    **(exponentiation in php 5)    |   . (<-is used to concatenate strings)
     */
    ERROR, //some
    NUMBER, // decimal, octal,float, hex
    VARIABLE,// $a
    IDENTIFIER,//simply a name
    LITERAL,//data value that appears directly in a program
    COMMENT,// // # /* */
    KEYWORD//a word reserved by the language for its core functionality
}