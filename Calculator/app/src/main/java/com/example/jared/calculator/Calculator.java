package com.example.jared.calculator;

/*
    Calculator Class

    keyPress() accepts a string of the button pressed, a number or instruction
    outputs a String to update the calculator screen

 */
public class Calculator {

    // quick way to sort incoming strings
    String[] numbers = {"0","1","2","3","4","5","6","7","8","9"};
    String[] operators = {"plus","minus","divide","multiply"};

    // the current string displayed on the calculator screen
    String currentVal = "0";

    // last button pressed bool
    boolean operatorPressed = false;

    // input numbers
    double numLeft = 0;
    double numRight = 0;

    // main function called by GUI class
    public String keyPress(String val) {

        boolean operator = validateKeyPress(val);

        // if the key was a number
        if (!operator) {
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

    public boolean validateKeyPress(String val) {
        for (int i = 0; i < numbers.length;i++) {
            if (val == numbers[i])
                return false;
        }
        return true;
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
