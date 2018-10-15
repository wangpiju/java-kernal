package com.hs3.utils.algorithm;


import java.math.BigInteger;

public interface CombinationAlgorithm {

    public int getMaxSupportedSize();

    public BigInteger getCombinationCount(int numberOfElements, int numberToPick);

    public void fetchCombination(Object[] source, Object[] target, BigInteger ordinal);
}

