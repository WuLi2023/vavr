/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2023 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.collection.euler;

import io.vavr.Function1;
import io.vavr.collection.CharSeq;
import io.vavr.collection.Stream;
import io.vavr.collection.List;
import org.junit.Test;

import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <strong>Problem 35: Circular primes</strong>
 * <p>The number, 197, is called a circular prime because all rotations of the digits: 197, 971, and 719, are themselves prime.</p>
 * <p>There are thirteen such primes below 100: 2, 3, 5, 7, 11, 13, 17, 31, 37, 71, 73, 79, and 97.</p>
 * <p>How many circular primes are there below one million?</p>
 * See also <a href="https://projecteuler.net/problem=35">projecteuler.net problem 35</a>.
 */
public class Euler35Test {

    @Test
    public void shouldSolveProblem35() {
        assertThat(rotations(197)).isEqualTo(List.of(197, 971, 719));
        assertThat(circularPrimes(100)).isEqualTo(13);
        assertThat(circularPrimes(1000000)).isEqualTo(55);
    }

    /**
     * 求小于 n 的循环质数的个数
     */
    private static int circularPrimes(int n) {
        // 将 isPrime 方法转换为 Function1，然后调用 memoized 方法，将其转换为一个记忆化的函数。
        final Predicate<Integer> memoizedIsPrime = Function1.of(Euler35Test::isPrime).memoized()::apply;
        // 生成一个从 2 到 n 的整数序列，然后过滤掉不是质数的数，再过滤掉旋转后不是质数的数，最后返回序列的长度。
        return Stream.rangeClosed(2, n)
                .filter(memoizedIsPrime)
                .map(Euler35Test::rotations)
                .filter(list -> list.forAll(memoizedIsPrime))
                .length();
    }

    /**
     * 判断一个整数是否为质数
     */
    private static boolean isPrime(int n) {
        // 判断这个数是否等于 2，如果是，则返回 true，因为 2 是质数。判断它是否为偶数，如果是，则返回 false，因为偶数不可能是质数。
        return n == 2 || n % 2 != 0 &&
                // 生成一个从 3 到 sqrt(n) 的奇数序列，并且步长为 2，即只包含奇数。
                Stream.rangeClosedBy(3, (int) Math.sqrt(n), 2)
                        // 判断序列中的每个数是否能整除 n，如果有一个数能整除 n，则 n 不是质数，返回 false。
                        .find(x -> n % x == 0)
                        // 如果序列中的每个数都不能整除 n，则 n 是质数，返回 true。
                        .isEmpty();
    }

    private static List<Integer> rotations(int n) {
        final CharSeq seq = CharSeq.of(String.valueOf(n));
        return Stream.range(0, seq.length())
                // 对字符序列的每个位置 i，将其后面的所有字符移至序列的开头，再将序列转换为整数，即得到一个旋转数。
                .map(i -> seq.drop(i).appendAll(seq.take(i)))
                // 将旋转数转换为整数，然后将其转换为一个 List。
                .map(s -> Integer.valueOf(s.toString()))
                .toList();
    }

}
