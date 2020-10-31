package com.dmoracco;

public class MyBigInteger {
    public String Value;
    public boolean Negative;

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

        this.Value = value;
    }

    public MyBigInteger Plus(MyBigInteger x){
        int a, b, sum = 0;
        int carry = 0;
        String rv = "";

        for (int i = 0; i < Math.max(this.Value.length(), x.Value.length()); i++){

            // pad the shorter number with leading zeros
            int diff = 0;
            String padding = "";

            if (this.Value.length() < x.Value.length()){
                diff = x.Value.length() - this.Value.length();
                for (int it = 0; it < diff; it++){
                    this.Value = "0" + this.Value;
                }
            } else if (this.Value.length() > x.Value.length()){
                diff = this.Value.length() - x.Value.length();
                for (int ix = 0; ix < diff; ix++){
                    x.Value = "0" + x.Value;
                }

            }
            // Convert A
            a = this.Value.toCharArray()[Math.max(this.Value.length(), x.Value.length()) - i -1] - 48;
            //if (this.Negative) a = 0 - a;
            // Convert B
            b = x.Value.toCharArray()[Math.max(this.Value.length(), x.Value.length()) - i - 1] - 48;
            //if (x.Negative) b = 0 - b;
            // Sum A and B and carry
            sum = a + b + carry;
            // Handle carry
            if (sum >= 10){
                carry = 1;
                sum = sum - 10;
            } else {
                carry = 0;
            }

            // Convert to string
            rv = (char)(sum+48) + rv;


        }
        if (carry == 1){
            rv = "1" + rv;
        }
/*
        if (carry == 1){
            if (rv.indexOf(0) == 9+48){
                rv.toCharArray()[0] = 48; //as in 0
                rv = "1" + rv;
            } else {
                rv.toCharArray()[0] = rv.toCharArray()[0]++; // increment value by one
            }
        }
*/
        // Handle negative final sum
/*
        if (sum < 0){
            rv = "-" + rv;
        }
        System.out.println(rv);
*/
        return new MyBigInteger(rv);
    }

    public MyBigInteger Times(MyBigInteger x){
        MyBigInteger returnValue = new MyBigInteger("0");

        return returnValue;
    }
}
