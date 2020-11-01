package com.dmoracco;

import java.util.ArrayList;
import java.util.Hashtable;

import static com.dmoracco.GetCpuTime.getCpuTime;

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


            //FibRecur
            overflow = false;
            if (fibrecur && lastRecurTime < maxTime){
                for (z = 0; z < 10; z++){
                    startTime = getCpuTime();
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
                    if(FibCache(inputNumber) < 0){
                        fibcache = false;
                        overflow = true;
                        break;
                    }
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
                    if(FibLoop(inputNumber) < 0){
                        fibloop = false;
                        overflow = true;
                        break;
                    }
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
                    if(FibMatrix1(inputNumber)< 0 ){
                        overflow = true;
                        fibmatrix = false;
                        break;
                    }
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

        int tests = 10;
        System.out.printf("\n\t%s\n", "Testing FibRecur");
        for (int t = 0; t <= tests; t++){
            System.out.printf("\t%d: %d", t, FibRecur(t));
        }
        //System.out.println("f(92): " + FibRecur(47));
        System.out.printf("\n\t%s\n", "Testing FibCache");
        for (int s = 0; s <= tests; s++){
            System.out.printf("\t%d: %d, ", s, FibCache(s));
        }
        //System.out.println("f(92): " + FibCache(47));
        System.out.printf("\n\t%s\n", "Testing FibLoop");
        for (int r = 0; r <= tests; r++){
            System.out.printf("\t%d: %d, ", r,  FibLoop(r));
        }
        //System.out.println("f(92): " + FibLoop(47));
        System.out.printf("\n\t%s\n", "Testing FibMatrix1");
        for (int r = 0; r <= tests; r++){
            System.out.printf("\t%d: %d, ", r, FibMatrix1(r));
        }
        //System.out.println("f(92): " + FibMatrix1(47));
    }


    public static MyBigInteger FibRecur(long number){
/*
        if (number.Value == "0" || number.Value == "1") return number;
        else return FibRecur(number-1)+FibRecur(number-2);
*/
        return null;
    }

    public static long FibCache(long number){
        // Initialize table
        Hashtable<Long, Long> resultsCache = new Hashtable<Long,Long>();

        return fibCacheHelper(number, resultsCache);
    }

    public static long fibCacheHelper(long number, Hashtable<Long, Long> resultsCache){
        if (number < 2) return number; // skip 0, 1
        else if (resultsCache.get(number) != null){
            return resultsCache.get(number);
        }
        else { // otherwise, find and fill in result, then return.
            resultsCache.put(number, (fibCacheHelper(number-1, resultsCache) +
                    (fibCacheHelper(number-2, resultsCache))));
            return resultsCache.get(number);
        }
    }

    public static long FibLoop(long number){
        long A = 0;
        long B = 1;
        long next = 0;

        // skip 0, 1
        if (number < 2) return number;
        // Loop through storing the values for last two in sequence.
        for (long i = 2; i <= number; i++){
            next = A + B;
            A = B;
            B = next;
        }

        return next;
    }

    public static long FibMatrix1(long number){

        if (number == 0) return 0;

        // Create base matrix
        long[][] matrix = {{1, 1}, {1, 0}};

        // Iterate through x-2 powers of base matrix
        for (long i = 0; i < number-2; i++){
            fibMatrixMultiply(matrix);
        }

        // return top left element of matrix
        return matrix[0][0];
    }

    public static void fibMatrixMultiply(long[][] matrix){
        // multiply provided 2x2 matrix by the Fibonacci base matrix
        // note: I got rid of the unnecessary arithmetic, though this would likely have been optimized anyway.
        long tl = matrix[0][0] + matrix[0][1];
        long tr = matrix[0][0];
        long bl = matrix[0][0] + matrix[0][1];
        long br = matrix[0][0];

        matrix[0][0] = tl;
        matrix[0][1] = tr;
        matrix[1][0] = bl;
        matrix[1][1] = br;
    }

}






