package com.company;

public enum TokenType {
    NUMBER(1),
    PLUS(2),
    MINUS(2),
    MULTIPLY(3),
    DIVIDE(3),
    EXPONENT(5),
    LOG(5),
    FACTORIAL(5),
    SQRT(5),
    SIN(5),
    COS(5),
    TAN(5),
    UNARYMINUS(6),
    UNARYPLUS(6),
    LPAREN(10),
    RPAREN(10);

    private final int value;
    TokenType(int value){
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
