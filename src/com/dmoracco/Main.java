package com.dmoracco;

import java.util.ArrayList;
import java.util.Hashtable;

import static com.dmoracco.GetCpuTime.getCpuTime;
import static com.dmoracco.MyBigInteger.*;

public class Main {

    public static void main(String[] args) {
        validate();

        // Testing
        boolean fibrecur = true, fibcache = true, fibloop = true, fibmatrix = true;
        int maxX = 200;
        long maxTime = 600000;
        int testCount = 4;

        long startTime = 0, endTime = 0, lastRecurTime = 0, lastCacheTime = 0, lastLoopTime = 0, lastMatrixTime = 0;

        ArrayList<long[]> testTimes = new ArrayList();

        System.out.printf("\n\n%5s%5s%18s%50s%50s%50s\n",
                "X", "N", "FibRecur", "FibCache", "FibLoop ", "FibMatrx");
        System.out.printf("%12s", "");
        for (int k = 0; k < testCount; k++){
            System.out.printf("%10s%20s%20s", "t(X)", "Ratio", "Ex.Ratio");
        }
        System.out.printf("\n");
        //System.out.printf("%10s%28s%28s%28s%28s\n", "", "T(x) - Ratio - Ex. Ratio", "T(x) - Ratio - Ex. Ratio", "T(x) - Ratio - Ex. Ratio","T(x) - Ratio - Ex. Ratio");

        int inputNumber = 1;
        while ((fibrecur || fibcache || fibloop || fibmatrix) && inputNumber <= maxX){
            long[] currentRoundTimes = new long[testCount];
            for (int i = 0; i < testCount; i++){
                currentRoundTimes[i] = -1;
            }

            int index = 0;
            int z = 0;
            long total = 0;
            boolean overflow;

            MyBigInteger input = new MyBigInteger(Integer.toString(inputNumber));

            //FibRecur
            overflow = false;
            if (fibrecur && lastRecurTime < maxTime){
                for (z = 0; z < 10; z++){
                    startTime = getCpuTime();
                    FibRecur(input);
                    endTime = getCpuTime();
                    total = total + ((endTime-startTime)/1000); // converted to ms then added
                }
                if (!overflow){
                    lastRecurTime = total/10; // average
                    currentRoundTimes[index++] =  lastRecurTime;
                } else {
                    currentRoundTimes[index++] = -2;
                }

            } else{
                fibrecur = false;
                index++;
            }

            //FibCache
            overflow = false;
            total = 0;
            if (fibcache && lastCacheTime < maxTime){
                for (z = 0; z < 10000; z++){
                    startTime = getCpuTime();
                    FibCache(input);
                    endTime = getCpuTime();
                    total = total + ((endTime-startTime));
                }
                if (!overflow){
                    lastCacheTime = total/10000; // average
                    currentRoundTimes[index++] =  lastCacheTime;
                } else {
                    currentRoundTimes[index++] = -2;
                }

            } else{
                fibcache = false;
                index++;
            }

            //FibLoop
            overflow = false;
            total = 0;
            if (fibloop && lastLoopTime < maxTime){
                for (z = 0; z < 10000; z++){
                    startTime = getCpuTime();
                    FibLoop(input);
                    endTime = getCpuTime();
                    total = total + ((endTime-startTime));
                }
                if (!overflow){
                    lastLoopTime = total/10000; // average
                    currentRoundTimes[index++] =  lastLoopTime;
                } else{
                    currentRoundTimes[index++] = -2;
                }

            } else{
                fibloop = false;
                index++;
            }

            //FibMatrix
            overflow = false;
            total =0;
            if (fibmatrix && lastMatrixTime < maxTime){
                for (z = 0; z < 10000; z++){
                    startTime = getCpuTime();
                    FibMatrix1(input);
                    endTime = getCpuTime();
                    total = total + ((endTime-startTime));
                }
                if (!overflow){
                    lastMatrixTime = total/10000; // average
                    currentRoundTimes[index++] =  lastMatrixTime;
                } else{
                    currentRoundTimes[index++] = -2;
                }
            } else{
                fibmatrix = false;
                index++;
            }

            testTimes.add(currentRoundTimes);

            // Output
            // X
            System.out.printf("%5s", inputNumber);
            // N
            System.out.printf("%5s", (int)Math.ceil((Math.log(inputNumber+1)/Math.log(2))));
            // https://www.geeksforgeeks.org/how-to-calculate-log-base-2-of-an-integer-in-java/
            // Tests
            for (int t = 0; t < testCount; t++){
                if (currentRoundTimes[t] == -1){
                    System.out.printf("%10s%20s%20s", "na", "na", "na");
                } else if ( currentRoundTimes[t] == -2){
                    System.out.printf("%10s%20s%20s", "ovr", "ovr", "ovr");
                } else{
                    //Time
                    System.out.printf("%10d", (int)currentRoundTimes[t]);
                    //Ratio
                    if (inputNumber % 2 == 0){
                        //System.out.printf("%20.2f", ((double)(currentRoundTimes[t]) / (testTimes.get((inputNumber/2) - 1)[t]))); // Tx(X) / Tx(X/2)
                        long[] prt = testTimes.get((inputNumber/2)-1);
                        System.out.printf("%20.2f", ((double)(currentRoundTimes[t]) / prt[t])); // Tx(X) / Tx(X/2)
                        //System.out.printf("%20s", "");
                    } else System.out.printf("%20s", "");
                    //Expected Ratio
                    if (inputNumber % 2 == 0){
                        if (t == 0){
                            // Exponential 1.6180^x or 2^(x/2)
                            System.out.printf("%20.2f", (Math.pow(2.0, (inputNumber/2.0))));
                            //System.out.printf("%20.2f", (Math.pow(1.618, inputNumber)));
                        } else if (t == 1 || t == 2){
                            // Linear x
                            System.out.printf("%20d", inputNumber/(inputNumber/2));
                        } else if (t == 3) {
                            // Log_2(x)
                            System.out.printf("%20.2f", ((Math.log(inputNumber)/Math.log(2)) /
                                    (Math.log(inputNumber/2)/Math.log(2))));
                        }
                    } else System.out.printf("%20s", "");
                }
            }
            System.out.printf("\n");
            inputNumber++;
        }
    }

    public static void validate(){
        System.out.println("Validating Algorithms:");

        int tests = 9;
        MyBigInteger test;
        System.out.printf("\n\t%s\n", "Testing FibRecur");
        for (int t = 0; t <= tests; t++){
            test = new MyBigInteger("" + (char)(t+48));
            System.out.printf("\t%d: %s", t, FibRecur(test).Value());
        }
        System.out.printf("\n\t%s\n", "Testing FibCache");
        for (int s = 0; s <= tests; s++){
            test = new MyBigInteger("" + (char)(s+48));
            System.out.printf("\t%d: %s, ", s, FibCache(test).Value());
        }
        System.out.println();
        System.out.println("f(1000): " + FibCache(new MyBigInteger("1000")).Value());
        System.out.printf("\n\t%s\n", "Testing FibLoop");
        for (int r = 0; r <= tests; r++){
            test = new MyBigInteger("" + (char)(r+48));
            System.out.printf("\t%d: %s, ", r,  FibLoop(test).Value());
        }
        System.out.println();
        System.out.println("f(1000): " + FibLoop(new MyBigInteger("1000")).Value());
        System.out.printf("\n\t%s\n", "Testing FibMatrix1");
        for (int q = 0; q <= tests; q++){
            test = new MyBigInteger("" + (char)(q+48));
            System.out.printf("\t%d: %s, ", q, FibMatrix1(test).Value());
        }
        System.out.println();
        System.out.println("f(1000): " + FibMatrix1(new MyBigInteger("1000")).Value());
    }


    public static MyBigInteger FibRecur(MyBigInteger number){
        if (number.Value().equals("0") || number.Value().equals("1")) return number;
        else{
            MyBigInteger previousOne = FibRecur(number.Plus("-1"));
            MyBigInteger previousTwo = FibRecur(number.Plus("-2"));
            return previousOne.Plus(previousTwo);
        }
    }

    public static MyBigInteger FibCache(MyBigInteger number){
        // Initialize table
        Hashtable<String, MyBigInteger> resultsCache = new Hashtable<String,MyBigInteger>();

        return fibCacheHelper(number, resultsCache);
    }

    public static MyBigInteger fibCacheHelper(MyBigInteger number, Hashtable<String, MyBigInteger> resultsCache){
        if (number.Value().equals("0") || number.Value().equals("1")) return number;
        else if (resultsCache.get(number.Value()) != null){
            return resultsCache.get(number.Value());
        }
        else { // otherwise, find and fill in result, then return.
            MyBigInteger previousOne = fibCacheHelper(number.Plus("-1"), resultsCache);
            MyBigInteger previousTwo = fibCacheHelper(number.Plus("-2"), resultsCache);
            resultsCache.put(number.Value(), previousOne.Plus(previousTwo));
            return resultsCache.get(number.Value());
        }
    }

    public static MyBigInteger FibLoop(MyBigInteger number){
        MyBigInteger A = new MyBigInteger("0");
        MyBigInteger B = new MyBigInteger("1");
        MyBigInteger next = new MyBigInteger("0");

        // skip 0, 1
        if (number.Value().equals("0") || number.Value().equals("1")) return number;
        // Loop through storing the values for last two in sequence.
        MyBigInteger i = new MyBigInteger("2");
        MyBigInteger end = number.Plus("1"); // for terminator
        // for loop replacement for BigInts
        while (!i.Value().equals(end.Value())){
            next = A.Plus(B);
            A = B;
            B = next;
            i = i.Plus("1"); //i++
        }

        return next;
    }

    public static MyBigInteger FibMatrix1(MyBigInteger number){

        if (number.Value().equals("0") || number.Value().equals("1")) return number;

        // Create base matrix
        //long[][] matrix = {{1, 1}, {1, 0}};
        MyBigInteger A = new MyBigInteger("1");
        MyBigInteger B = new MyBigInteger("1");
        MyBigInteger C = new MyBigInteger("1");
        MyBigInteger D = new MyBigInteger("0");
        MyBigInteger[][] matrix = {{A, B}, {C, D}};

        // Iterate through x-2 powers of base matrix
        MyBigInteger i = new MyBigInteger("0");
        MyBigInteger end = number.Plus("-2"); // for terminator
        // for loop replacement for BigInts
        while (!i.Value().equals(end.Value())){
            fibMatrixMultiply(matrix);
            i = i.Plus("1"); //i++
        }

        // return top left element of matrix
        return matrix[0][0];
    }

    public static void fibMatrixMultiply(MyBigInteger[][] matrix){
        // multiply provided 2x2 matrix by the Fibonacci base matrix
        // note: I got rid of the unnecessary arithmetic, though this would likely have been optimized anyway.
        MyBigInteger tl = matrix[0][0].Plus(matrix[0][1]);
        MyBigInteger tr = matrix[0][0];
        MyBigInteger bl = matrix[0][0].Plus(matrix[0][1]);
        MyBigInteger br = matrix[0][0];

        matrix[0][0] = tl;
        matrix[0][1] = tr;
        matrix[1][0] = bl;
        matrix[1][1] = br;
    }

}






