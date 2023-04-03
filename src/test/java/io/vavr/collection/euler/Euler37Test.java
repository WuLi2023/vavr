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

import io.vavr.collection.CharSeq;
import io.vavr.collection.List;
import org.junit.Test;

import static io.vavr.API.*;
import static org.assertj.core.api.Assertions.assertThat;

public class Euler37Test {

    /**
     * <strong>Problem 37 Truncatable primes</strong>
     * <p>
     * The number 3797 has an interesting property. Being prime itself, it is
     * possible to continuously remove digits from left to right, and remain
     * prime at each stage: 3797, 797, 97, and 7. Similarly we can work from
     * right to left: 3797, 379, 37, and 3.
     * <p>
     * Find the sum of the only eleven primes that are both truncatable from
     * left to right and right to left.
     * <p>
     * NOTE: 2, 3, 5, and 7 are not considered to be truncatable primes.
     * <p>
     * See also <a href="https://projecteuler.net/problem=37">projecteuler.net
     * problem 37</a>.
     */
    @Test
    public void shouldSolveProblem37() {
        assertThat(isTruncatablePrime(3797)).isTrue();
        List.of(2, 3, 5, 7).forEach(i -> assertThat(isTruncatablePrime(7)).isFalse());

        assertThat(sumOfTheElevenTruncatablePrimes()).isEqualTo(748_317);
    }

    private static int sumOfTheElevenTruncatablePrimes() {
        // 返回一个无限的素数流，然后使用 filter 方法过滤出可截断素数，再使用 take 方法取出前 11 个
        return PrimeNumbers.primes()
                .filter(Euler37Test::isTruncatablePrime)
                .take(11)
                .sum().intValue();
    }

    /**
     * 判断输入的整数是否为可截断素数（truncatable prime）。
     */
    private static boolean isTruncatablePrime(int prime) {
        return Match(prime).of(
                // 2, 3, 5, 7 are not considered to be truncatable primes
                Case($(p -> p > 7), p -> {
                    /*如果 prime 大于 7，就执行对应的 Lambda 表达式，使用 CharSeq 将 prime 转换为字符串，
                    然后生成两个截取后的字符串，再将这两个字符串转换为数值类型，
                    并使用 Utils.MEMOIZED_IS_PRIME 判断它们是否为素数。*/
                    // primeSeq 是一个 CharSeq 类型的字符串，表示输入的整数。
                    final CharSeq primeSeq = CharSeq.of(Integer.toString(p));
                    return List.rangeClosed(1, primeSeq.length() - 1)
                            // primeSeq.drop(i) 从 primeSeq 中截取 i 个字符，生成一个新的 CharSeq。第一个元素是 primeSeq 从第 i 位开始的剩余部分，第二个元素是 primeSeq 从右边删除 i 位后的剩余部分。
                            .flatMap(i -> List.of(primeSeq.drop(i), primeSeq.dropRight(i)))
                            .map(CharSeq::mkString)
                            .map(Long::valueOf)
                            // Utils.MEMOIZED_IS_PRIME 是一个 Function1<Long, Boolean> 类型的函数，表示判断一个 Long 类型的数是否为素数。
                            .forAll(Utils.MEMOIZED_IS_PRIME::apply);
                }),
                Case($(), false)
        );
    }
}
