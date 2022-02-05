package com.company;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Input expression: ");
        String expression = scan.nextLine();
        System.out.println(expression);

        Lexer lexer = new Lexer(expression);
        try {
            lexer.generateTokens();
        } catch(Exception e) {
            e.printStackTrace();
        }
        String tokens = lexer.tokenValues();
        System.out.print("Tokens: ");
        System.out.println(tokens);
        System.out.println(lexer.tokensToString());

        ShuntingYard yard = new ShuntingYard(lexer.tokens);
        try {
            yard.infixToRPN();
        } catch(Exception e) {
            e.printStackTrace();
        }
        tokens = yard.tokenValues();
        System.out.print("RPN: ");
        System.out.println(tokens);

        Interpreter interpreter = new Interpreter();
        try {
            double output = interpreter.interpretRPN(yard.output);
            System.out.print("Evaluation: ");
            System.out.println(output);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
