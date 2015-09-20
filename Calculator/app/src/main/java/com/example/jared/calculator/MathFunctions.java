package com.example.jared.calculator;

/**
 * Created by Jared on 9/19/2015.
 */
public class MathFunctions {

    String[] numbers = {"0","1","2","3","4","5","6","7","8","9"};
    String[] operators = {"plus","minus","divide","multiply"};
    String currentVal = "0";

    boolean operatorPressed = false;
    double numLeft = 0;
    double numRight = 0;
    double runningTotal = 0;

    public String keyPress(String val) {
        // int value of the key pressed if it is a number
        // will be 10 if the key was something else
        int intKeyVal = validateKeyPress(val);

        // if the key was a number
        if (intKeyVal != 10) {
            if (!operatorPressed) {
                // concat number to current output
                if (currentVal == "0") {
                    currentVal = "";
                }
                currentVal = currentVal + val;
                return currentVal;
            } else {
                // replace output with key press
                currentVal = val;
                operatorPressed = false;
                return currentVal;
            }

        // key press other then a number
        } else {
            if (validateOperator(val)) {
                currentVal = processOperator(val);
                return currentVal;
            } else {
                if (val == "clear") {
                    currentVal = clear();
                    return currentVal;
                } else if (val == "sign") {
                    currentVal = sign();
                    return currentVal;
                } else if (val == "backspace") {
                    currentVal = backspace();
                    return currentVal;
                }
            }
        }
        return "end keyPress()";
    }

    public int validateKeyPress(String val) {
        for (int i = 0; i < numbers.length;i++) {
            if (val == numbers[i])
                return Integer.parseInt(numbers[i]);
        }
        return 10;
    }

    public boolean validateOperator(String val) {
        for (int i = 0; i < operators.length;i++) {
            if (val == operators[i])
                return true;
        }
        return false;
    }

    public String processOperator(String val) {
        operatorPressed = true;
        if (numLeft == 0) {
            numLeft = Double.parseDouble(currentVal);
            return currentVal;
        } else {
            numRight = Double.parseDouble(currentVal);
            return calculate(val);
        }
    }

    public String calculate(String val) {
        double result = 0;

        if (val == "plus") {
            result = add(numLeft, numRight);
        } else if (val == "minus") {
            result = minus(numLeft, numRight);
        } else if (val == "multiply") {
            result = multiply(numLeft, numRight);
        } else if (val == "divide") {
            result = divide(numLeft, numRight);
        }

        numLeft = result;
        numRight = 0;

        return Double.toString(result);
    }

    public String clear() {
        return "42";
    }

    public String sign() {
        return "42";
    }

    public String backspace() {
        return "42";
    }

    public double add(double num1, double num2) {
        double result = num1 + num2;
        return result;
    }

    public double minus(double num1, double num2) {
        double result = num1 - num2;
        return result;
    }

    public double multiply(double num1, double num2) {
        double result = num1 * num2;
        return result;
    }

    public double divide(double num1, double num2) {
        double result = num1 / num2;
        return result;
    }
}
