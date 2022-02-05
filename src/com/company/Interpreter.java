package com.company;

import java.util.ArrayList;
import java.util.Stack;

public class Interpreter {
    private double factorial(double num){
        double f = 1;
        for(double i = 2; i <= num; i++){
            f = f * i;
        }
        return f;
    }
    public double interpretRPN(ArrayList<Token> tokens) throws Exception{
        Stack<NumberToken> numbers = new Stack<>();
        NumberToken num1, num2;
        for(Token token : tokens){
            if (token instanceof NumberToken){
                numbers.push((NumberToken) token);
            } else if (token instanceof OpToken){
                switch (token.type) {
                    case UNARYPLUS:
                        break;
                    case UNARYMINUS:
                        num1 = numbers.pop();
                        num1.setValue(-num1.value);
                        numbers.push(num1);
                        break;
                    case PLUS:
                        num2 = numbers.pop(); // this is the number adjacent to current opToken
                        num1 = numbers.pop();
                        num1.setValue(num1.value + num2.value);
                        numbers.push(num1);
                        break;
                    case MINUS:
                        num2 = numbers.pop();
                        num1 = numbers.pop();
                        num1.setValue(num1.value - num2.value);
                        numbers.push(num1);
                        break;
                    case MULTIPLY:
                        num2 = numbers.pop();
                        num1 = numbers.pop();
                        num1.setValue(num1.value * num2.value);
                        numbers.push(num1);
                        break;
                    case DIVIDE:
                        num2 = numbers.pop(); // num2 must not be zero
                        num1 = numbers.pop();
                        num1.setValue(num1.value / num2.value);
                        numbers.push(num1);
                        break;
                    case EXPONENT:
                        num2 = numbers.pop();
                        num1 = numbers.pop();
                        num1.setValue(Math.pow(num1.value, num2.value));
                        numbers.push(num1);
                        break;

                    case SQRT:
                        num1 = numbers.pop();
                        if(num1.value >= 0) {
                            num1.setValue(Math.sqrt(num1.value));
                        } else{
                            throw new Exception("sqrt operation on negative value");
                        }
                        numbers.push(num1);
                        break;
                    case LOG:
                        num1 = numbers.pop();
                        num1.setValue(Math.log10(num1.value));
                        numbers.push(num1);
                        break;
                    case FACTORIAL:
                        num1 = numbers.pop();
                        if (num1.value % 1 == 0){
                            num1.setValue(factorial(num1.value));
                        } else{
                            throw new Exception("factorial on non-integer");
                        }
                        numbers.push(num1);
                        break;
                    // Trigonometry functions: number is automatically assumed to be in radians
                    case SIN:
                        num1 = numbers.pop();
                        num1.setValue(Math.sin(Math.toRadians(num1.value)));
                        numbers.push(num1);
                        break;
                    case COS:
                        num1 = numbers.pop();
                        num1.setValue(Math.cos(Math.toRadians(num1.value)));
                        numbers.push(num1);
                        break;
                    case TAN:
                        num1 = numbers.pop();
                        num1.setValue(Math.tan(Math.toRadians(num1.value)));
                        numbers.push(num1);
                        break;
                }
            }
        }
        if (numbers.size() == 1) {
            return numbers.pop().value;
        } else {
            throw new Exception("Invalid syntax.");
        }
    }
}
