package com.company;

public abstract class Token {
    TokenType type;
    int position;

    public Token(TokenType type){
        this.type = type;
    }

    public void setPosition(int position){
        this.position = position;
    }

    public void setType(TokenType type){
        this.type = type;
    }
}
