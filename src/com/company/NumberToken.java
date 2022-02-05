package com.company;

public class NumberToken extends Token{
    double value;
    public NumberToken(TokenType type, double value) {
        super(type);
        this.value = value;
    }

    public void setValue(double value){
        this.value = value;
    }
}
