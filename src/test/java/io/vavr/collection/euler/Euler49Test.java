package io.vavr.collection.euler;

import io.vavr.collection.List;
import io.vavr.collection.Stream;

import static io.vavr.collection.euler.Utils.isPrime;

/**
 * @author Muggle
 * @date 2023年04月24日 22:35
 */
public class Euler49Test {

    private static List<String> findPrimePermutations() {
        return Stream.rangeClosed(1000, 9999)
                .filter(Utils::isPrime)
                .filter(n -> n != 1487)
                .filter(n -> isPrime(n + 3330) && isPrime(n + 6660) && isPermutation(n, n + 3330) && isPermutation(n, n + 6660))
                .map(String::valueOf)
                .toList();
    }

    private static boolean isPermutation(int a, int b) {
        return Stream.ofAll(String.valueOf(a).toCharArray())
                .permutations()
                .map(chars -> Integer.parseInt(chars.mkString()))
                .exists(i -> i == b);
    }

}
