package com.hs3.utils.algorithm;
import java.math.BigInteger;

/**
 * @author jason.wang
 * 階乘類, ex: 1*2*3*4*5*6....n
 */
public final class Calculator {

    private Calculator() {}
    //local cache
    private static final BigInteger[] FACT_RESULT_POOL = new BigInteger[1024];
    public static BigInteger factorial(int number) {

        if( number < 0 )
            throw new IllegalArgumentException();

        BigInteger result = null;

        if( number < FACT_RESULT_POOL.length )
            result = FACT_RESULT_POOL[number];

        if( result == null ) {

            result = BigInteger.ONE;
            for(int i = 2; i <= number; i++)
                result = result.multiply(BigInteger.valueOf(i));
            if( number < FACT_RESULT_POOL.length )
                FACT_RESULT_POOL[number] = result;
        }
        return result;
    }
}
