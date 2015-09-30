package com.example.jared.calculator;

/*
    Calculator Class

    keyPress() accepts a string of the button pressed, a number or instruction
    outputs a String to update the calculator screen

 */
public class Calculator {

    // quick way to sort incoming strings
    protected String[] numbers = {"0","1","2","3","4","5","6","7","8","9"};
    protected String[] operators = {"plus","minus","divide","multiply","equals"};

    // the current string displayed on the calculator screen
    protected String currentVal = "0";
    protected String currentOperator = "";
    protected String history = "";

    // last button pressed bool
    protected boolean operatorPressed = false;

    // input numbers
    protected double numLeft = 0;
    protected double numRight = 0;

    // main function called by GUI class
    public String keyPress(String val) {

        boolean operator = validateKeyPress(val);

        // if the key was a number
        if (!operator) {
            history = "";
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
                history = "";
                if (val == "clear") {
                    return clear();
                } else if (val == "sign") {
                    return sign();
                } else if (val == "backspace") {
                    return backspace();
                } else if (val == ".") {
                    currentVal += ".";
                    return currentVal;
                }
            }
        }
        return "end keyPress()";
    }

    // return true if the input is not a number
    public boolean validateKeyPress(String val) {
        for (int i = 0; i < numbers.length;i++) {
            if (val == numbers[i])
                return false;
        }
        return true;
    }

    // return true if input is an operator
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
            currentOperator = val;
            return currentVal;
        } else {
            numRight = Double.parseDouble(currentVal);
            currentVal = calculate(currentOperator);
            if (val == "equals")
                numLeft = 0;
            currentOperator = val;
            return currentVal;
        }
    }

    public String calculate(String val) {
        double result = 0;
        String op = "";
        if (val == "plus") {
            result = add(numLeft, numRight);
            op = "+";
        } else if (val == "minus") {
            result = minus(numLeft, numRight);
            op = "-";
        } else if (val == "multiply") {
            result = multiply(numLeft, numRight);
            op = "*";
        } else if (val == "divide") {
            result = divide(numLeft, numRight);
            op = "/";
        }
        history = numLeft + "  " + op + "  " + numRight;
        if (op.equals("/") && numRight == 0)
            return "NaN";
        numLeft = result;
        numRight = 0;
        return Double.toString(result);
    }

    public String clear() {
        currentVal = "0";
        numLeft = 0;
        numRight = 0;
        operatorPressed = false;
        return currentVal;
    }

    public String sign() {
        if (currentVal.substring(0,1).equals("-"))
            currentVal = currentVal.replace("-", "");
        else {
            if (operatorPressed || currentVal.equals("0")) {
                currentVal = "-";
                operatorPressed = false;
            } else {
                currentVal = "-" + currentVal;
            }
        }

        return currentVal;
    }

    public String backspace() {
        if (currentVal.length() > 0 )
            currentVal = currentVal.substring(0, currentVal.length()-1);
        return currentVal;
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
