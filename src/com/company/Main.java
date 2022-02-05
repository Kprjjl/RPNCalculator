package com.company;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Input expression: ");
        String expression; // 1. expression input (String datatype)
        expression = scan.nextLine();
//        System.out.println(expression);

        // 2. Lexical analysis
        Lexer lexer = new Lexer(expression);
        try {
            lexer.generateTokens();
        } catch(Exception e) {
            e.printStackTrace();
        }
        // ---------------------------------------
//        String tokens = lexer.tokenValues();
//        System.out.print("Tokens: ");
//        System.out.println(tokens);
//        System.out.println(lexer.tokensToString());

        // 3. processing of tokens to reverse postfix notation using shunting yard algorithm
        ShuntingYard yard = new ShuntingYard(lexer.tokens);
        try {
            yard.infixToRPN();
        } catch(Exception e) {
            e.printStackTrace();
        }
        // ---------------------------------
//        tokens = yard.tokenValues();
//        System.out.print("RPN: ");
//        System.out.println(tokens);

        // 4. Evaluating the RPN formatted tokens
        Interpreter interpreter = new Interpreter();
        try {
            double output = interpreter.interpretRPN(yard.output); // output
            System.out.print("Evaluation: ");
            System.out.println(output);
        } catch(Exception e){
            e.printStackTrace();
        }
        // ------------------------------------
    }
}
