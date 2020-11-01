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

        if (this.Value.length() > x.Value.length()){
             return bigMultiply(this, x);
        } else {
            return bigMultiply(x, this);
        }
    }

    private MyBigInteger bigMultiply(MyBigInteger largerInt, MyBigInteger smallerInt){
        MyBigInteger rv = new MyBigInteger("0");

        String sum;
        int product, carry;

        int smaller = smallerInt.Value.length();
        int larger = largerInt.Value.length();

        System.out.printf("  %s\n  %s\n-----------------------------\n", largerInt.Value, smallerInt.Value);

        for (int i = 0; i < smaller; i++){      // Iterate over smaller string of digits
            carry = 0;
            sum = "";
            for (int j = 0; j < larger; j++){   // Iterate over larger string of digits
                // Convert and multiply
                product = ((smallerInt.Value.toCharArray()[smaller-1-i]-48) *
                        (largerInt.Value.toCharArray()[larger-1-j]-48)) +carry;
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
            System.out.println("     subtotal: " + rv.Value);
        }
        System.out.printf("--------------------------------\n  %s\n", rv.Value);
        return rv;
    }
}
