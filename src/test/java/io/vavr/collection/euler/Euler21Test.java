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
import io.vavr.Tuple;
import io.vavr.collection.Stream;
import org.junit.Test;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <strong>Problem 21: Amicable numbers</strong>
 *
 * <p>Let d(<i>n</i>) be defined as the sum of proper divisors of <i>n</i> (numbers less than <i>n</i> which divide evenly into <i>n</i>).<br />
 * If d(<i>a</i>) = <i>b</i> and d(<i>b</i>) = <i>a</i>, where <i>a</i> ≠ <i>b</i>, then <i>a</i> and <i>b</i> are an amicable pair and each of <i>a</i> and <i>b</i> are called amicable numbers.</p>
 * <p>For example, the proper divisors of 220 are 1, 2, 4, 5, 10, 11, 20, 22, 44, 55 and 110; therefore d(220) = 284. The proper divisors of 284 are 1, 2, 4, 71 and 142; so d(284) = 220.</p>
 * <p>Evaluate the sum of all the amicable numbers under 10000</p>
 * See also <a href="https://projecteuler.net/problem=21">projecteuler.net problem 21</a>.
 */

public class Euler21Test {

    @Test
    public void shouldSolveProblem21() {
        assertThat(sumOfDivisors(220)).isEqualTo(1 + 2 + 4 + 5 + 10 + 11 + 20 + 22 + 44 + 55 + 110);
        assertThat(sumOfDivisors(284)).isEqualTo(1 + 2 + 4 + 71 + 142);
        assertThat(sumOfAmicablePairs(10000)).isEqualTo(31626);
    }

    /* 计算正整数n的所有因数之和的方法 */
    private static int sumOfDivisors(int n) {
        // 从2到n的平方根范围内找出n的除数
        return 1 + Stream.rangeClosed(2, (int) Math.sqrt(n))
                // 然后将找到的数对（除数和商）过滤掉不是n的因数、且不是n本身的数对，
                .map(d -> Tuple.of(d, n / d))
                .filter(t -> t._1 * t._2 == n && !Objects.equals(t._1, t._2))
                // 在剩下的数对中计算两个元素之和
                .map(t -> t._1 + t._2)
                // 将它们相加得到n的所有因数之和
                .foldLeft(0, Integer::sum);
    }

    /*计算亲和数对之和*/
    private static int sumOfAmicablePairs(int n) {
        // 计算一个数的真因数之和，并使用memoized方法缓存结果，以提高效率。
        final Function1<Integer, Integer> mSumOfDivisors = Function1.of(Euler21Test::sumOfDivisors).memoized();
        return Stream
                // 生成一个从1到n（不包括n）的整数流，其中n是要检查的最大数
                .range(1, n)
                // 真因数之和的真因数之和等于它本身，并且它的真因数之和大于它本身。这样可以避免重复计算亲和数对。
                .filter(x -> mSumOfDivisors.apply(mSumOfDivisors.apply(x)).intValue() == x && mSumOfDivisors.apply(x) > x)
                // 将过滤后的流中的所有数相加，并加上它们的真因数之和，得到最终结果。
                .foldLeft(0, (sum, x) -> sum + x + mSumOfDivisors.apply(x));
    }

}
