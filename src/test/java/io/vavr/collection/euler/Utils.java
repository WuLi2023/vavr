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

    static final Function1<Integer, BigInteger> MEMOIZED_FACTORIAL = Function1.of(Utils::factorial).memoized();

    static final Function1<Long, Boolean> MEMOIZED_IS_PRIME = Function1.of(Utils::isPrime).memoized();

    static Stream<BigInteger> fibonacci() {
        return Stream.of(BigInteger.ZERO, BigInteger.ONE)
                .appendSelf(self -> self.zip(self.tail()).map(t -> t._1.add(t._2)));
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

    /*接受一个长整型作为参数，并返回一个长整型的流。
      这个方法的作用是计算给定数字的所有因数，因数的计算方式是遍历从 1 到数字平方根的所有整数，
      如果能整除数字，则将这个整数和数字除以这个整数的商加入到流中，并去除重复的元素。*/
    static Stream<Long> factors(long number) {
        return Stream.rangeClosed(1, (long) Math.sqrt(number))
                // 筛选出能够整除该数的数字，即该数的因子。
                .filter(d -> number % d == 0)
                // 将每个因子 d 映射为包含 d 和 number/d 的 Stream，并使用 distinct() 方法去除重复的因子，最终返回一个包含所有因子的 Stream<Long>。
                .flatMap(d -> Stream.of(d, number / d))
                .distinct();
    }

    static Stream<Long> divisors(long l) {
        return factors(l).filter((d) -> d < l);
    }

    static boolean isPrime(long val) {
        return API.Match(val).of(
                API.Case($(n -> n < 2L), false),
                API.Case($(2L), true),
                API.Case($(), n -> {
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

    static Stream<Long> pentagonal() {
        return Stream.of(Tuple.of(1L, 1))
                .appendSelf(self -> self.map(t -> Tuple.of((t._2 + 1) * (3L * (t._2 + 1) - 1) / 2, t._2 + 1)))
                .map(t -> t._1);
    }

    static boolean isPentagonal(long number) {
        return ((1 + Math.sqrt(1 + 24 * number)) / 6) % 1 == 0;
    }
}
