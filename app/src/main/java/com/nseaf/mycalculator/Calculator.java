package com.nseaf.mycalculator;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;


import java.text.DecimalFormat;
import java.util.Arrays;

public class Calculator {
    String numberString="0";
    String detailsString="";
    long intNumber;
    double realNumber;
    boolean isIntNumber=true;
    boolean numHasRadixPoint=false;
    long memoryInt=0;
    double memoryDouble=0.0;
    boolean isIntMemory=true;


    String num1 = "";
    String num2 = "";
    String operation = "";
    String previousOperator = "";
    String nextNumInScreen = "";
    String PI = "\uD835\uDF0B";
    String memory = "0";
    Boolean clearJustPressed = false;

    public Calculator() {
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void processNumber(String i) {
        clearJustPressed = false;
        clean();

        if(numberString.length()<12) {  // limit of 12 digits

            nextNumInScreen += i;
            updateDetailsString();
            numberString = nextNumInScreen;

        }
        else
            detailsString="The number is too long..";
    }

    private void clean() {

        if(detailsString.length()>1 && detailsString.charAt(detailsString.length()-1) == '%'){
            detailsString = "";
        }

        if(nextNumInScreen.contains(PI)){
            nextNumInScreen = "";
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void piPressed() {
        clearJustPressed = false;

        nextNumInScreen = PI;
        updateDetailsString();
        numberString = nextNumInScreen;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateDetailsString() {

        if(detailsString.contains("=") && previousOperator.equals("=") ){
            detailsString = String.valueOf(nextNumInScreen);
            num1 = "";
            num2 = "";
            operation = "";
            previousOperator = "";
            return;
        }

        if(detailsString.contains("=")){
            detailsString = numberString + " " + operation + " " + nextNumInScreen;
            return;
        }


        if(containsOperator(detailsString)){
            if(detailsString.charAt(0) == '-' && containsOperator(detailsString.substring(1))){
                detailsString = detailsString.substring(0, detailsString.substring(1).indexOf(operation)+2);
            }else{
                detailsString = detailsString.substring(0, detailsString.indexOf(operation)+1);
            }
            detailsString +=  String.valueOf(nextNumInScreen);
        }else{
            detailsString = String.valueOf(nextNumInScreen);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean containsOperator(String strToCheck) {


        String[] operators = {"+", "-", "÷", "×"};

        return Arrays.stream(operators).anyMatch(strToCheck::contains);

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void processOperation(String operation) {
        clearJustPressed = false;
        this.operation = operation;
        nextNumInScreen = "";
        if(operation.equals("%")){
            if(detailsString.length() > 3 && containsOperator(detailsString.substring(1))){

                num2 = String.valueOf(Double.valueOf(num1)*Double.valueOf(numberString)/100);

                result();
                num2 = "";

                detailsString += "% = " + num1;


            }else{
                detailsString += operation;
                numberString = String.valueOf(Double.valueOf(numberString)/100);

            }


        }else if(num1.isEmpty() || previousOperator.equals("e") && !operation.equals("=")){
            detailsString += operation;
            num1 = numberString;
        }else if(previousOperator.equals("=")){
            detailsString = numberString + " "+ operation;
            previousOperator = operation;
        }else if(num2.isEmpty() || operation.equals("=")){
            if(operation.equals("="))previousOperator = "=";
            num2 = numberString;
            result();
            num2 = "";
            numberString = String.valueOf(num1);
            if(minusIsUsedAsNegative()){
                detailsString += " " + operation;
            }else{
                detailsString += " = " + num1;
            }
        }
    }

    public void processE() {
        clearJustPressed = false;
        String eToXResult = eOperation();
        //num1 = eToXResult;
        detailsString = "e^"+numberString;
        numberString = eToXResult;
        num1 = eToXResult;
        previousOperator = "e";
    }

    private String eOperation() {
        return getDoubleWithFormat(Math.pow(2.71828, cleanValueInScreen(numberString)));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean minusIsUsedAsNegative() {
        if(detailsString.charAt(0) == '-' && detailsString.length() >= 2 && !containsOperator(detailsString.substring(1))){
            return true;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void result() {

        prepareNumStrings();

        Double defFormatResult = new Double(0);
        if(detailsString.contains("+")){
            defFormatResult = Double.valueOf(num1) + Double.valueOf(num2);
        }else if(detailsString.contains("÷")){
            defFormatResult = Double.valueOf(num1) / Double.valueOf(num2);
        }else if(detailsString.contains("×")){
            defFormatResult = Double.valueOf(num1) * Double.valueOf(num2);
        }else if(detailsString.contains("-")){
            defFormatResult = Double.valueOf(num1) - Double.valueOf(num2);
        }

        //Check if it has radix
        if(defFormatResult % 1 == 0)
            num1 = String.valueOf(defFormatResult.intValue());
        else{
            num1 = getDoubleWithFormat(defFormatResult);
        }
    }

    private void prepareNumStrings() {
        num1 = num1.replace(PI, String.valueOf(Math.PI));
        num2 = num2.replace(PI, String.valueOf(Math.PI));
    }

    private String getDoubleWithFormat(Double d){
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        return df.format(d);
    }

    public void clearClicked() {

        if(!clearJustPressed){
            clearJustPressed = true;
            numberString = "0";
            nextNumInScreen = "";

            return;
        }

        numberString="0";
        detailsString="";
        intNumber=0;
        realNumber=0.0;
        isIntNumber=true;
        numHasRadixPoint=false;

        num1="";
        num2="";
        operation="";
        previousOperator = "";
        nextNumInScreen = "";
        memory = "0";
    }

    public void mPlus() {
        clearJustPressed = false;
        Double doubleFormatMemory = Double.valueOf(memory);
        Double valueToSum = cleanValueInScreen(numberString);
        doubleFormatMemory += valueToSum;

        memoryWithFormat(doubleFormatMemory);


    }

    private Double cleanValueInScreen(String screenNumStringFormat) {
        screenNumStringFormat = screenNumStringFormat.replace(PI, String.valueOf(Math.PI));

        return Double.valueOf(screenNumStringFormat);
    }

    private void memoryWithFormat(Double doubleFormatMemory) {
        if(doubleFormatMemory % 1 == 0){
            memory = String.valueOf(doubleFormatMemory.intValue());
        }else{
            memory = getDoubleWithFormat(doubleFormatMemory);
        }
    }

    public void mMinus() {
        clearJustPressed = false;
        Double doubleFormatMemory = Double.valueOf(memory);
        Double valueToSum = cleanValueInScreen(numberString);
        doubleFormatMemory -= valueToSum;

        memoryWithFormat(doubleFormatMemory);
    }

    public void mC() {
        clearJustPressed = false;
        memory = "0";
    }

}
