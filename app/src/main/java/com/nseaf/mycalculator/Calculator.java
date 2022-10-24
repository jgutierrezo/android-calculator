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

    public Calculator() {
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void processNumber(String i) {

        if(numberString.length()<12) {  // limit of 12 digits

            //intNumber = intNumber * 10 + i;
            nextNumInScreen += i;
            updateDetailsString();
            numberString = nextNumInScreen;

        }
        else
            detailsString="The number is too long..";
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

        this.operation = operation;
        nextNumInScreen = "";
        if(num1.isEmpty()){
            detailsString += operation;
            num1 = numberString;
        }else if(previousOperator.equals("=")){
            detailsString = numberString + " "+ operation;
            previousOperator = operation;
        } else if(num2.isEmpty() || operation.equals("=")){
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
        String eToXResult = eOperation();
        numberString = eToXResult;
        detailsString = eToXResult;
    }

    private String eOperation() {
        return getDoubleWithFormat(Math.pow(2.71828, Double.valueOf(numberString)));
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

    private String getDoubleWithFormat(Double d){
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        return df.format(d);
    }

    public void clearClicked() {
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
    }

    public void memPlusClicked() {
        if(isIntMemory){
            if(isIntNumber) {
                memoryInt += intNumber;
                detailsString = "Memory: "+memoryInt;
            }
            else {
                isIntNumber=false;
                memoryDouble = memoryInt + realNumber;
            }
        }
    }

    public void piPressed() {
        
    }
}
