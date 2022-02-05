package com.company;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Map;
import java.util.HashMap;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.lang.Character;
import java.lang.Double;
import java.lang.StringBuilder;

public class Lexer {
    final Map<String, TokenType> func_map = init_func_map();
    ArrayList<Token> tokens;
    CharacterIterator chars;
    char current_char;

    private static Map<String, TokenType> init_func_map(){
        Map<String, TokenType> map = new HashMap<>();
        map.put("log", TokenType.LOG);
        map.put("sqrt", TokenType.SQRT);
        map.put("sin", TokenType.SIN);
        map.put("cos", TokenType.COS);
        map.put("tan", TokenType.TAN);
        return map;
    }

    public Lexer(String expression){
        this.chars = new StringCharacterIterator(expression);
        this.current_char = chars.current();
        this.tokens = new ArrayList<>();
    }

    private void advance(){
        this.current_char = chars.next();
    }

    public void generateTokens() throws Exception{
        String str;
        int idx = 0; // for position (might not need)
        NumberToken numtoken;
        OpToken optoken;
        while (this.current_char != CharacterIterator.DONE) {
            if(Character.isWhitespace(this.current_char)) {
                advance();
            } else if(Character.isDigit(this.current_char) || this.current_char == '.'){
                numtoken = new NumberToken(TokenType.NUMBER, generateNumber());
                numtoken.setPosition(idx++);
                this.tokens.add(numtoken);
            } else if (Character.isLetter(this.current_char)) {
                str = generateString();
                if (func_map.containsKey(str)){
                    optoken = new OpToken(func_map.get(str), str);
                    optoken.setPosition(idx++);
                    this.tokens.add(optoken);
                } else {
                    throw new Exception("Illegal Function. " + str);
                }
            } else {
                switch (this.current_char) {
                    case '+':
                        optoken = new OpToken(TokenType.PLUS, "+");
                        optoken.setPosition(idx++);
                        this.tokens.add(optoken);
                        advance();
                        break;
                    case '-':
                        optoken = new OpToken(TokenType.MINUS, "-");
                        optoken.setPosition(idx++);
                        this.tokens.add(optoken);
                        advance();
                        break;
                    case '*':
                        optoken = new OpToken(TokenType.MULTIPLY, "*");
                        optoken.setPosition(idx++);
                        this.tokens.add(optoken);
                        advance();
                        break;
                    case '/':
                        optoken = new OpToken(TokenType.DIVIDE, "/");
                        optoken.setPosition(idx++);
                        this.tokens.add(optoken);
                        advance();
                        break;
                    case '(':
                        optoken = new OpToken(TokenType.LPAREN, "(");
                        optoken.setPosition(idx++);
                        this.tokens.add(optoken);
                        advance();
                        break;
                    case ')':
                        optoken = new OpToken(TokenType.RPAREN, ")");
                        optoken.setPosition(idx++);
                        this.tokens.add(optoken);
                        advance();
                        break;
                    case '^':
                        optoken = new OpToken(TokenType.EXPONENT, "^");
                        optoken.setPosition(idx++);
                        this.tokens.add(optoken);
                        advance();
                        break;
                    case '!':
                        optoken = new OpToken(TokenType.FACTORIAL, "!");
                        optoken.setPosition(idx++);
                        this.tokens.add(optoken);
                        advance();
                        break;
                    default:
                        throw new Exception("Illegal Character. " + this.current_char);
                }
            }
        }
        prefixUnaryCheck();
    }

    private void prefixUnaryCheck(){
        EnumSet<TokenType> binaryChecks = EnumSet.of(TokenType.NUMBER, TokenType.RPAREN);
        EnumSet<TokenType> unaryChecks = EnumSet.complementOf(binaryChecks);
        Token token, prev_token;
        if (this.tokens.size() > 1) {
            prev_token = this.tokens.get(0);
            switch(prev_token.type){ // for expressions beginning in a unary operator
                case PLUS:
                    prev_token.setType(TokenType.UNARYPLUS);
                    this.tokens.set(0, prev_token);
                    break;
                case MINUS:
                    prev_token.setType(TokenType.UNARYMINUS);
                    this.tokens.set(0, prev_token);
                    break;
            }
            for (int i = 1; i < this.tokens.size(); i++) {
                prev_token = this.tokens.get(i-1);
                token = this.tokens.get(i);
                switch (token.type) {
                    case PLUS:
                        if (unaryChecks.contains(prev_token.type)){
                            token.setType(TokenType.UNARYPLUS);
                            this.tokens.set(i, token);
                        }
                        break;
                    case MINUS:
                        if (unaryChecks.contains(prev_token.type)){
                            token.setType(TokenType.UNARYMINUS);
                            this.tokens.set(i, token);
                        }
                        break;
                }
            }
        }
    }

    private double generateNumber() throws NumberFormatException{
        StringBuilder number_str = new StringBuilder(String.valueOf(this.current_char));
        advance();

        while (Character.isDigit(this.current_char) || this.current_char == '.'){
            number_str.append(this.current_char);
            advance();
        }
        return Double.parseDouble(number_str.toString());
    }

    private String generateString(){
        StringBuilder str = new StringBuilder(String.valueOf(this.current_char));
        advance();

        while (Character.isLetter(this.current_char)){
            str.append(this.current_char);
            advance();
        }
        return str.toString();
    }

    // Utilities
    public String tokensToString(){
        StringBuilder output = new StringBuilder();
        NumberToken numtoken;
        OpToken optoken;
        for (Token token : this.tokens) {
            if (token instanceof NumberToken){
                numtoken = (NumberToken) token;
                output.append("NumberToken");
                output.append(": (");
                output.append(numtoken.value);
                output.append(", ");
                output.append(numtoken.position);
            } else if (token instanceof OpToken){
                optoken = (OpToken) token;
                output.append(optoken.type);
                output.append(", ");
                output.append(optoken.position);
            }
            output.append("), ");
        }
        String str_output = output.toString();
        return "[" + str_output.substring(0, str_output.length()-2) + "]";
    }
    public String tokenValues(){
        StringBuilder output = new StringBuilder();
        NumberToken numtoken;
        OpToken optoken;
        for (Token token : this.tokens) {
            if (token instanceof NumberToken){
                numtoken = (NumberToken) token;
                output.append(numtoken.value);
            } else if (token instanceof OpToken){
                optoken = (OpToken) token;
                output.append(optoken.value);
            }
            output.append(", ");
        }
        String str_output = output.toString();
        return "[" + str_output.substring(0, str_output.length()-2) + "]";
    }
}
