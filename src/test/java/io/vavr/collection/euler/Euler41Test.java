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

import io.vavr.collection.Stream;
import io.vavr.collection.List;
import org.junit.Test;

import static java.util.Comparator.reverseOrder;
import static io.vavr.collection.euler.Utils.isPrime;
import static org.assertj.core.api.Assertions.assertThat;

public class Euler41Test {

    /**
     * <strong>Problem 41 Pandigital prime</strong>
     * <p>
     * We shall say that an <i>n</i>-digit number is pandigital if it makes use
     * of all the digits 1 to <i>n</i> exactly once. For example, 2143 is a
     * 4-digit pandigital and is also prime.
     * <p>
     * What is the largest <i>n</i>-digit pandigital prime that exists?
     * <p>
     * See also <a href="https://projecteuler.net/problem=41">projecteuler.net
     * problem 41</a>.
     */
    @Test
    public void shouldSolveProblem41() {
        assertThat(nDigitPandigitalNumbers(4)).contains(2143);
        assertThat(isPrime(2143)).isTrue();

        assertThat(largestNPandigitalPrime()).isEqualTo(7652413);
    }

    /*当一个数字的所有数位之和是三的倍数，则这个数可以被三整除，因此不可能是一个素数。我们可以试着计算所有两位数以上的全数字各位数之和。*/
    /*很明显，只有四位和七位全数字的各位数之和不是三的倍数，因此只有这两个数位的全数字中可能存在素数。考虑到七位数必然大于四位数，所以我们从最大的七位全数字7654321开始筛选，依次寻找一个更小的素数，然后判断这个素数是否为全数字。*/

    /**
     * 返回最大的 n-全数字的质数，其中n是1~9的数字。
     *
     * @return 最大的 n-全数字的质数
     */
    private static int largestNPandigitalPrime() {
        // 使用 Stream.rangeClosedBy() 方法从数字 9 开始往下枚举，直到数字 1。这些数字对应着单个数字全排列的位数，即 n-全数字。
        return Stream.rangeClosedBy(9, 1, -1)
                // 对于每个n-全数字列表，筛选出其中的质数，并按降序排序
                .flatMap(n -> nDigitPandigitalNumbers(n)
                        .filter(Utils::isPrime)
                        .sorted(reverseOrder()))
                // 返回排完序后的第一个元素，即最大的 n-全数字的质数
                .head();
    }

    /**
     * 返回一个包含n位全排列数字的列表
     *
     * @param n 需要生成的数字的位数
     * @return 包含所有n位全排列数字的列表
     */
    private static List<Integer> nDigitPandigitalNumbers(int n) {
        // 生成从1到n的整数列表
        return List.rangeClosed(1, n)
                // 求出该整数列表的全排列
                .permutations()
                // 将每个全排列转换成字符串
                .map(List::mkString)
                // 将每个字符串转换成对应的整数
                .map(Integer::valueOf);
    }
}
