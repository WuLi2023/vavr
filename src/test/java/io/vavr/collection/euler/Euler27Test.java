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
import io.vavr.Tuple;
import io.vavr.collection.Iterator;
import io.vavr.collection.List;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <strong>Problem 27: Quadratic primes</strong>
 * <p>
 * Euler discovered the remarkable quadratic formula:
 * <p>
 * n² + n + 41
 * <p>
 * It turns out that the formula will produce 40 primes for the consecutive
 * values n = 0 to 39. However, when n = 40, 40^2 + 40 + 41 = 40(40 + 1) + 41 is
 * divisible by 41, and certainly when n = 41, 41² + 41 + 41 is clearly
 * divisible by 41.
 * <p>
 * The incredible formula n² − 79n + 1601 was discovered, which produces 80
 * primes for the consecutive values n = 0 to 79. The product of the
 * coefficients, −79 and 1601, is −126479.
 * <p>
 * Considering quadratics of the form:
 * <p>
 * n² + an + b, where |a| < 1000 and |b| < 1000 <p>
 * where |n| is the modulus/absolute value of n e.g. |11| = 11 and |−4| = 4
 * <p>
 * Find the product of the coefficients, a and b, for the quadratic expression
 * that produces the maximum number of primes for consecutive values of n,
 * starting with n = 0.
 * <p>
 * See also
 * <a href="https://projecteuler.net/problem=27">projecteuler.net problem 27
 * </a>.
 */
public class Euler27Test {

    /*素数是指除了1和它本身以外，不能被任何整数整除的数。*/

    /*分析：首先，我们可以分析一下系数a与b需要满足的性质。
    假设n=0，f(0)=02+0+b=b必须是一个素数，也就是说b必须是一个素数；
    假设n=1，则f(1)=1+a+b必须是一个素数，我们已经知道b必须是一个素数，又因为除2以外所有素数都是奇数，则在b≠2时，则a必须是一个奇数。
    如果b=2，取n=2，则f(2)=22+2a+2=2(a+3)为偶数则必不为素数，则n的取值只能是0和1，此公式最多只能产生两个素数，肯定不是产生素数最多的公式，所以可以把b=2的情况排除。
    综上所述，b为 `除二以外的素数`，a为 `奇数`，这样我们大幅度缩小筛选系数的范围。*/

    /*在确定了筛选的系数范围后，只需要对每一对系数求其产生的素数个数，方法是从n=0开始依次代入不同的n，看所产生的数是否为素数。
    如果是素数，则对n累加一，再求数值判断是否为素数。
    如果不是素数则跳出循环，得到的n的值即这个公式可以产生的素数个数。
    然后我们对系数范围内的值形成的公式依次求它们可以产生的素数个数并存储相应的字典中，字典的键为系数对可以产生的素数个数，值为相应的系数对，最后我们求最大的键对应的系数对的乘积，即为所求。*/

    @Test
    public void shouldSolveProblem27() {
        assertThat(numberOfConsecutivePrimesProducedByFormulaWithCoefficients(1, 41)).isEqualTo(40);
        assertThat(numberOfConsecutivePrimesProducedByFormulaWithCoefficients(-79, 1601)).isEqualTo(80);

        assertThat(productOfCoefficientsWithMostConsecutivePrimes(-999, 999)).isEqualTo(-59231);
    }

    /**
     * 时间复杂度是 O(n^2)，其中 n 是区间范围的大小。
     * <p>计算所有可能的a和b的乘积，找到产生最多连续素数的a和b的乘积。</p>
     */
    private static int productOfCoefficientsWithMostConsecutivePrimes(int coefficientsLowerBound, int coefficientsUpperBound) {
        /*生成一个整数区间范围*/
        final Iterable<Integer> coefficients = List.rangeClosed(coefficientsLowerBound, coefficientsUpperBound);
        // 遍历两个范围内的数字
        return API.For(coefficients, coefficients)
                // 生成一个元组流，其中每个包含a和b的值。
                .yield(Tuple::of)
                // 计算给定a和b的二次方程产生的连续素数的数量。
                .map(c -> Tuple.of(c._1, c._2, numberOfConsecutivePrimesProducedByFormulaWithCoefficients(c._1, c._2)))
                // 找到素数个数最大的元组，并返回两个系数的乘积。
                .fold(Tuple.of(0, 0, -1), (n, m) -> n._3 >= m._3 ? n : m)
                .apply((a, b, p) -> a * b);
    }

    /**
     * 返回从0开始的二次表达式n^2 + an + b产生的连续质数的数量。
     *
     * @param a 二次表达式中n的系数
     * @param b 二次表达式中的常数项
     * @return 二次表达式产生的连续质数的数量
     */
    private static int numberOfConsecutivePrimesProducedByFormulaWithCoefficients(int a, int b) {
        // 生成从0开始的无限整数序列
        return Iterator.from(0L)
                // 将二次表达式应用于每个整数
                .map(n -> (long) Math.pow(n, 2) + a * n + b)
                // 从序列中取出元素，同时使用一个记忆化的isPrime函数确定二次表达式的结果是否为质数
                .takeWhile(Utils.MEMOIZED_IS_PRIME::apply)
                // 返回结果序列的长度，即二次表达式产生的连续质数的数量
                .length();
    }
}
