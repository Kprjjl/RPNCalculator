package com.company;

public class OpToken extends Token{
    String value;
    public OpToken(TokenType type, String value) {
        super(type);
        this.value = value;
    }
}
