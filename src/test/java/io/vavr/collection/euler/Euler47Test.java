package io.vavr.collection.euler;

import io.vavr.collection.Stream;
import org.junit.Test;

import static io.vavr.collection.euler.PrimeNumbers.primeFactors;

/**
 * @author fc
 * @date 2023年04月24日 11:29
 */
public class Euler47Test {

    private final int targetConsecutiveNumbers = 4;

    @Test
    void test() {
        Integer head = Stream.from(1)
                .sliding(targetConsecutiveNumbers)
                .find(window -> window.forAll(n -> primeFactors(n).size() == targetConsecutiveNumbers))
                .get().head();
        System.out.println(head);
    }


}
