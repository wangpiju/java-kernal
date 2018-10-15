package com.hs3.utils.algorithm;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

public class Combination<E> implements Iterable<List<E>> {

    private static final CombinationAlgorithm DEFAULT_ALGORITHM = AlgorithmVER01.INSTANCE;

    public static <E> Combination<E> of(Collection<E> elements,
                                        int numberToPick) {

        return of(elements, numberToPick, DEFAULT_ALGORITHM);
    }

    private static <E> Combination<E> of(Collection<E> elements,
                                         int numberToPick,
                                         CombinationAlgorithm algorithm) {

        if (elements == null)
            throw new NullPointerException();

        if (numberToPick < 0 || numberToPick > elements.size())
            throw new IllegalArgumentException(
                    "Invalid number of elements to pick : " + numberToPick +
                            " out of " + elements.size());

        if (algorithm == null)
            algorithm = DEFAULT_ALGORITHM;

        return new Combination<>(elements, numberToPick, algorithm);
    }

    @SuppressWarnings("unchecked")
    private E[] elements, picked;
    private CombinationAlgorithm algorithm;
    private BigInteger count;

    private Combination(Collection<E> elements,
                        int numberToPick,
                        CombinationAlgorithm algorithm) {

        assert elements != null;
        assert numberToPick >= 0;
        assert numberToPick <= elements.size();
        assert algorithm != null;

        this.elements = (E[]) elements.toArray();
        this.picked = (E[]) new Object[numberToPick];
        this.algorithm = algorithm;
        this.count = this.algorithm.getCombinationCount(
                this.elements.length, numberToPick);
    }

    private BigInteger getCombinationCount() {

        return count;
    }

    private List<E> getCombination(BigInteger ordinal) {

        algorithm.fetchCombination(elements, picked, ordinal);
        return Arrays.asList(picked);
    }

    @Override
    public Iterator<List<E>> iterator() {

        return new OrdinalIterator();
    }

    private class OrdinalIterator implements Iterator<List<E>> {

        private BigInteger ordinal;

        private OrdinalIterator() {

            ordinal = ZERO;
        }

        @Override
        public boolean hasNext() {

            return ordinal.compareTo(getCombinationCount()) < 0;
        }

        @Override
        public List<E> next() {

            List<E> result = getCombination(ordinal);
            ordinal = ordinal.add(ONE);
            return result;
        }

        @Override
        public void remove() {

            throw new UnsupportedOperationException();
        }
    }

    private enum AlgorithmVER01 implements CombinationAlgorithm {

        INSTANCE;

        @Override
        public int getMaxSupportedSize() {

            return MAX_SUPPORT;
        }

        @Override
        public BigInteger getCombinationCount(int numberOfElements, int numberToPick) {

            if (numberOfElements < 0)
                throw new IllegalArgumentException(
                        "Invalid number of elements : " + numberOfElements);

            if (numberOfElements > getMaxSupportedSize())
                throw new IllegalArgumentException(
                        "Number of elements out of range : " + numberOfElements);

            if (numberToPick < 0 || numberToPick > numberOfElements)
                throw new IllegalArgumentException(
                        "Invalid number to pick : " + numberToPick);

            if (numberToPick == 0 || numberToPick == numberOfElements)
                return ONE;
            else
                return Calculator.factorial(numberOfElements).divide(
                        Calculator.factorial(numberToPick).multiply(
                                Calculator.factorial(numberOfElements - numberToPick)));
        }

        @Override
        public void fetchCombination(Object[] source,
                                     Object[] target,
                                     BigInteger ordinal) {

            for (int i = 0, si = 0; i < target.length; i++, si++) {

                if (ordinal.compareTo(ZERO) > 0) {

                    BigInteger cLeft = getCombinationCount(
                            source.length - si - 1, target.length - i - 1);
                    while (ordinal.compareTo(cLeft) >= 0) {

                        si++;
                        ordinal = ordinal.subtract(cLeft);
                        if (ordinal.compareTo(ZERO) == 0)
                            break;
                        cLeft = getCombinationCount(
                                source.length - si - 1, target.length - i - 1);
                    }
                }
                target[i] = source[si];
            }
        }

        private static final int MAX_SUPPORT = 1024;
    }
}