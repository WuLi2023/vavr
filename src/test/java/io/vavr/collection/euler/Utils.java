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

import io.vavr.API;
import io.vavr.Function1;
import io.vavr.Tuple;
import io.vavr.collection.Iterator;
import io.vavr.collection.Stream;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.net.URL;
import java.util.Scanner;

import static io.vavr.API.$;

final class Utils {

    private Utils() {
    }

    /**
     * 实现了一个生成阶乘的方法。这个算法的时间复杂度是 O(n)，可以高效地生成任意长度的阶乘序列。
     */
    static final Function1<Integer, BigInteger> MEMOIZED_FACTORIAL = Function1.of(Utils::factorial).memoized();

    /**
     * 实现了一个生成素数的方法。这个算法的时间复杂度是 O(n^2)，可以高效地生成任意长度的素数序列。
     */
    static final Function1<Long, Boolean> MEMOIZED_IS_PRIME = Function1.of(Utils::isPrime).memoized();

    /**
     * 实现了一个生成斐波那契数列的方法。这个算法的时间复杂度是 O(n)，可以高效地生成任意长度的斐波那契数列。
     */
    static Stream<BigInteger> fibonacci() {
        /* 初始时包含了 0 和 1 两个斐波那契数列的初始值。 */
        return Stream.of(BigInteger.ZERO, BigInteger.ONE)
                // 然后使用 appendSelf 方法对该流进行扩展，每次在流的末尾添加一个新的元素，该元素的值为前两个元素之和，通过 zip 和 map 方法实现。
                .appendSelf(self -> self
                        /*
                         * 将当前流和当前流的尾部流（去掉第一个元素的子流）进行配对（pairing），返回一个新的流，
                         * 其中每个元素都是一个二元组（Tuple），第一个元素是当前流的元素，第二个元素是当前流的尾部流对应位置的元素。
                         * 将斐波那契数列的当前元素和下一个元素进行配对，生成一个新的流。
                         * 这个流的每个元素都是一个二元组，第一个元素是当前斐波那契数列元素，第二个元素是下一个斐波那契数列元素。
                         */
                        .zip(self.tail())
                        // 将每个二元组转换成它们的和，就得到了一个新的斐波那契数列流
                        .map(t -> t._1.add(t._2)));
    }

    /**
     * 实现阶乘
     */
    static BigInteger factorial(int n) {
        return Stream
                // 生成从1到n的整数流
                .rangeClosed(1, n)
                .map(BigInteger::valueOf)
                // 使用fold方法将所有BigInteger值乘起来，并返回结果
                .fold(BigInteger.ONE, BigInteger::multiply);
    }

    /*
     * 接受一个长整型作为参数，并返回一个长整型的流。
     * 这个方法的作用是计算给定数字的所有因数，因数的计算方式是遍历从 1 到数字平方根的所有整数，
     * 如果能整除数字，则将这个整数和数字除以这个整数的商加入到流中，并去除重复的元素。
     */
    static Stream<Long> factors(long number) {
        return Stream.rangeClosed(1, (long) Math.sqrt(number))
                // 筛选出能够整除该数的数字，即该数的因子。
                .filter(d -> number % d == 0)
                // 将每个因子 d 映射为包含 d 和 number/d 的 Stream，并使用 distinct() 方法去除重复的因子，最终返回一个包含所有因子的
                // Stream<Long>。
                .flatMap(d -> Stream.of(d, number / d))
                .distinct();
    }

    /**
     * 接受一个长整型作为参数，并返回一个长整型的流。
     * 这个方法的作用是计算给定数字的所有真因数，真因数的计算方式是遍历从 1 到数字平方根的所有整数，
     * 如果能整除数字，则将这个整数加入到流中，并去除重复的元素。
     */
    static Stream<Long> divisors(long l) {
        return factors(l)
                // 筛选出真因子，即不包含数字本身的因子。（过滤掉任何等于或大于l的因数）
                .filter((d) -> d < l);
    }

    /**
     * 返回一个无限的 Stream，其中元素为素数。
     * 使用 Stream.iterate() 函数生成从 2 开始的自然数序列，并使用过滤器筛选出素数。
     * 过滤器的测试方法是 Utils 类中的 isPrime() 方法，该方法接受一个整数并返回布尔值。
     *
     * @return 一个包含所有素数的 Stream<Integer> 对象。
     */
    static Stream<Integer> primes() {
        return Stream.iterate(2, i -> i + 1)
                .filter(Utils::isPrime);
    }

    /**
     * 生成一个不大于n的质数流。
     *
     * @param n 质数上限（包含）
     * @return 不大于n的质数流
     */
    static Stream<Integer> primes(int n) {
        // 初始值为2，然后重复调用 nextPrime 方法获取下一个质数，直到达到n或者更大的值。
        return Stream.iterate(2, Utils::nextPrime).takeWhile(p -> p <= n);
    }

    // 返回比给定整数n大的下一个质数
    static int nextPrime(int n) {
        // 从n+1开始构造无限流（Stream）
        return Stream.from(n + 1)
                // 过滤出所有是质数的元素
                .filter(Utils::isPrime)
                // 取第一个符合要求的元素作为结果
                .head();
    }

    static boolean isPrime(long val) {
        // 使用Vavr的模式匹配功能，对val的值进行匹配，如果val小于2，则返回false，如果val等于2，则返回true，否则执行后面的代码。
        return API.Match(val).of(
                API.Case($(n -> n < 2L), false),
                API.Case($(2L), true),
                API.Case($(), n -> {
                    // 计算数字的平方根，作为遍历的上限。
                    final double upperLimitToCheck = Math.sqrt(n);
                    return !PrimeNumbers.primes().takeWhile(d -> d <= upperLimitToCheck).exists(d -> n % d == 0);
                }));
    }

    /**
     * 接受一个File对象作为输入，并返回一个Stream对象，其中包含了文件中的所有行
     */
    static Stream<String> readLines(File file) {
        try {
            // 使用了Vavr库提供的Stream.ofAll方法，将Iterator对象转换为Stream对象。可以用StreamSupport.stream(iterator,
            // false)来替换它，以达到相同的结果。
            return Stream.ofAll(new Iterator<String>() {

                // 使用Scanner对象读取文件的每一行，然后创建一个自定义的Iterator对象来迭代文件的每一行。
                final Scanner scanner = new Scanner(file);

                @Override
                public boolean hasNext() {
                    // 该Iterator对象的hasNext方法检查文件是否还有更多的行可读取，如果没有，则关闭Scanner对象，否则返回true。next方法返回下一行的内容。
                    final boolean hasNext = scanner.hasNextLine();
                    if (!hasNext) {
                        scanner.close();
                    }
                    return hasNext;
                }

                @Override
                public String next() {
                    return scanner.nextLine();
                }
            });
        } catch (FileNotFoundException e) {
            // 如果文件未找到，则方法返回一个空的Stream对象。
            return Stream.empty();
        }
    }

    static File file(String fileName) {
        final URL resource = Utils.class.getResource(fileName);
        if (resource == null) {
            throw new RuntimeException("resource not found");
        }
        return new File(resource.getFile());
    }

    static String reverse(String s) {
        return new StringBuilder(s).reverse().toString();
    }

    static boolean isPalindrome(String val) {
        return val.equals(reverse(val));
    }

    static boolean isPalindrome(int val) {
        return isPalindrome(Long.toString(val));
    }

    // 该方法返回一个Stream<Long>对象，生成五边形数序列
    static Stream<Long> pentagonal() {
        // 仅包含（1L, 1）的新stream
        return Stream.of(Tuple.of(1L, 1))
                // 不断地将当前元素映射为下一个元素
                .appendSelf(self ->
                        // 对于当前元素(t._2 +
                        // 1)，计算出它的下一个五边形数((t._2+1)*(3*(t._2+1)-1)/2)，并将其包装成Tuple(Long类型的五边形数值和Integer类型的五边形数编号)
                        self.map(t -> Tuple.of((t._2 + 1) * (3L * (t._2 + 1) - 1) / 2, t._2 + 1)))
                // 取出每个Tuple中的五边形数值(Long类型)，组成新的stream
                .map(t -> t._1);
    }

    /**
     * 判断一个数是否是五边形数
     *
     * @param number 待判断的数字
     * @return 如果该数字是五边形数，则返回 true；否则返回 false。
     */
    static boolean isPentagonal(long number) {
        // 通过公式计算判断是否为五边形数，返回结果
        return ((1 + Math.sqrt(1 + 24 * number)) / 6) % 1 == 0;
    }
}
