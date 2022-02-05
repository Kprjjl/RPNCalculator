package com.company;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Stack;

public class ShuntingYard {
    EnumSet<TokenType> funcs = EnumSet.of(TokenType.SQRT, TokenType.LOG, TokenType.SIN, TokenType.COS, TokenType.TAN);
    EnumSet<TokenType> rightAssoc = EnumSet.of(TokenType.UNARYMINUS, TokenType.UNARYPLUS, TokenType.EXPONENT, TokenType.SQRT, TokenType.LOG, TokenType.SIN, TokenType.COS, TokenType.TAN);
    EnumSet<TokenType> leftAssoc = EnumSet.complementOf(rightAssoc);
    ArrayList<Token> tokens;
    ArrayList<Token> output = new ArrayList<>();

    public ShuntingYard(ArrayList<Token> tokens){
        this.tokens = tokens;
    }

    public void infixToRPN() throws Exception{
        Stack<Token> opStack = new Stack<>();
        for(Token token : this.tokens){
            if (token instanceof NumberToken){
                this.output.add(token);
                continue;
            }

            if (token instanceof OpToken){
                switch(token.type){
                    case LPAREN:
                        opStack.push(token);
                        break;
                    case RPAREN:
                        // might be a good place to place "wall" operator
                        while (!opStack.isEmpty() && opStack.peek().type != TokenType.LPAREN){
                            this.output.add(opStack.pop());
                            if(opStack.isEmpty()){
                                throw new Exception("Mismatched parenthesis.");
                            }
                        }
                        opStack.pop();
                        if(funcs.contains(opStack.peek().type)){
                            this.output.add(opStack.pop());
                        }
                        break;
                    default:
                        while((!opStack.isEmpty() && opStack.peek().type != TokenType.LPAREN) &&
                                (token.type.getValue() < opStack.peek().type.getValue() ||
                                (token.type.getValue() == opStack.peek().type.getValue() && leftAssoc.contains(token.type))
                                )
                        ){
                            this.output.add(opStack.pop());
                        }
                        opStack.push(token);
                }
            }
        }
        while (!opStack.isEmpty()){
            if (opStack.peek().type == TokenType.LPAREN){
                throw new Exception("Invalid expression.");
            }
            this.output.add(opStack.pop());
        }
    }

    // Utilities
    public String tokensToString(){
        StringBuilder output = new StringBuilder();
        NumberToken numtoken;
        OpToken optoken;
        for (Token token : this.output) {
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
        for (Token token : this.output) {
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
