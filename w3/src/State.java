public enum State {
    START,
    VARIABLE,
    IDENTIFIER,
    VARIABLE_START,
    SIMPLE_OPER,
    SINGLE_OPER,
    MAY_DOUBLE_OPER,
    COMPARE_OPER,
    EQUAL,
    COMMENT_START,
    COMMENT,
    MULTI_LINE_COMMENT,
    MULTI_LINE_COMMENT_START,
    ERROR,
    BINARY,
    BINARY_N,
    OCT,
    OCT_N,
    HEX,
    HEX_N,
    NUMBER_D,
    NUMBER,
    NUMBER_DOT,
    ZERO,
    CHECK_AFTER_VAR,
    MINUS,
    CHAR_LIT,
    CHAR_REC,
    STRING_LIT,
    STRING_REC,
    SIMPLE_SYN,
    COMPLEX_SYN,
    CHECK_VAR_CB,
    OPEN_B,
    CLOSE_B,
    ARRAY,
    KEY,
    VARINSIDE,
    CHECK_VAR,
    FIELD,
    IND,
    CLS,
    CURLE_B,
    CL_ID,
    VAR_AR,
    VAR_AR_S,
    CURLY_B,
    STR_ERR
}