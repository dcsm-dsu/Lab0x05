package com.dmoracco;

import java.util.Random;

public class MyBigInteger {
    private String value;
    private boolean Negative;

    // TODO: Handle all cases of negative integers, currently only handles enough to process Fib numbers
    MyBigInteger(String value){
        // Validate string value
        if (null == value) throw new NumberFormatException("BigInteger created with null value");

        this.Negative = false;
        if (value.toCharArray()[0] == '-'){
            this.Negative = true;
            value = value.substring(1);
        }
        for (char number: value.toCharArray()){
            if(!Character.isDigit(number)) throw new NumberFormatException("BigInteger created with bad string");
        }

        this.value = value;
    }

    MyBigInteger(int expectedSize){
        Random r = new Random();
        int rDigit;
        String rValue = "";
        for (int i = 0; i < expectedSize; i++){
            rDigit = r.nextInt(9);
            rValue = (char)(rDigit+48) + rValue;
        }
        this.value = rValue;
        this.Negative = false;
    }

    public MyBigInteger Plus(MyBigInteger x){
        int a, b, sum = 0;
        int carry = 0;
        String rv = "";

        // pad the shorter number with leading zeros
        int diff = 0;
        String padding = "";
        if (this.value.length() < x.value.length()){
            diff = x.value.length() - this.value.length();
            for (int it = 0; it < diff; it++){
                this.value = "0" + this.value;
            }
        } else if (this.value.length() > x.value.length()){
            diff = this.value.length() - x.value.length();
            for (int ix = 0; ix < diff; ix++){
                x.value = "0" + x.value;
            }

        }

        boolean negativeFlag = this.Negative;
        for (int i = 0; i < Math.max(this.value.length(), x.value.length()); i++){

            // Convert A
            a = this.value.toCharArray()[Math.max(this.value.length(), x.value.length()) - i -1] - 48;
            if (this.Negative) a = 0 - a;
            // Convert B
            b = x.value.toCharArray()[Math.max(this.value.length(), x.value.length()) - i - 1] - 48;
            if (x.Negative) b = 0 - b;
            // Sum A and B and carry
            sum = a + b + carry;
            // Handle carry
            if (sum >= 10){
                carry = 1;
                sum = sum - 10;
            } else if (sum < 0){
                carry = -1;
                sum = sum + 10;
            } else {
                carry = 0;
            }

            // Convert to string
            rv = (char)(sum+48) + rv;


        }
        // Handle final carry
        if (carry == 1){
            rv = "1" + rv;
        } else if (carry == -1){
            this.Negative = true;

        }

        // Handle extra padding
        while (rv.length() > 1 && rv.toCharArray()[0] == '0'){
            rv = rv.substring(1);
        }

        // Handle negative final sum
        if (negativeFlag){
            rv = "-" + rv;
        }

        return new MyBigInteger(rv);
    }

    public MyBigInteger Plus(String x){
        MyBigInteger newBigint = new MyBigInteger(x);
        return this.Plus(newBigint);
    }

    public MyBigInteger Times(MyBigInteger x){

        if (this.value.length() > x.value.length()){
             return bigMultiply(this, x);
        } else {
            return bigMultiply(x, this);
        }
    }

    private MyBigInteger bigMultiply(MyBigInteger largerInt, MyBigInteger smallerInt){
        MyBigInteger rv = new MyBigInteger("0");

        String sum;
        int product, carry;

        int smaller = smallerInt.value.length();
        int larger = largerInt.value.length();

        System.out.printf("  %s\n  %s\n-----------------------------\n", largerInt.value, smallerInt.value);

        for (int i = 0; i < smaller; i++){      // Iterate over smaller string of digits
            carry = 0;
            sum = "";
            for (int j = 0; j < larger; j++){   // Iterate over larger string of digits
                // Convert and multiply
                product = ((smallerInt.value.toCharArray()[smaller-1-i]-48) *
                        (largerInt.value.toCharArray()[larger-1-j]-48)) +carry;
                // Handle carry
                if (product >= 10){
                    carry = product / 10;
                    product = product % 10;
                }
                // Convert back to string
                sum = ((char)(product+48)) + sum;
            }
            // Handle last carry
            if (carry > 0){
                sum = carry + sum;
            }
            // Handle powers of ten without conversion
            for (int k = i; k > 0; k--){
                sum = sum + "0";
            }
            System.out.printf("  %s\n+", sum);
            // Convert sum to BigInt
            MyBigInteger s = new MyBigInteger(sum);
            // Add to overall total using bigInt
            rv = rv.Plus(s);
            System.out.println("     subtotal: " + rv.value);
        }
        System.out.printf("--------------------------------\n  %s\n", rv.value);
        return rv;
    }

    public String Value(){
        if (this.Negative == false){
            return this.value;
        } else return "-" + this.value;
    }

}
