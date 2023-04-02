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
import io.vavr.collection.Stream;
import org.junit.Test;

import static io.vavr.collection.euler.Utils.MEMOIZED_FACTORIAL;
import static org.assertj.core.api.Assertions.assertThat;

public class Euler34Test {

    /*先通过好奇数需要满足的条件确定一个合理的上界，然后在上界之内寻找符合要求的值并求和即可。*/
    /*假设符合要求的数字n总共有d位，则10^d−1<n<10^d。此外，由于该数字等于它各位数的阶乘之和，则各位数的阶乘之和最大只能为d⋅9!，即n<d⋅9!
      综合上面的两个条件我们有：
      10^d−1<d⋅9!   ⇔   d−1<log(9!)+log(d)  ⇔   d−log(d)<log(9!)+1
      解此不等式，我们有d<7.43，因为d为整数则d最大为7。
      因此有：n<7⋅9!=2540160 */

    /**
     * <strong>Problem 34 Digit factorials</strong>
     * <p>
     * 145 is a curious number, as 1! + 4! + 5! = 1 + 24 + 120 = 145.
     * <p>
     * Find the sum of all numbers which are equal to the sum of the factorial
     * of their digits.
     * <p>
     * F
     * Note: as 1! = 1 and 2! = 2 are not sums they are not included.
     * <p>
     * See also <a href="https://projecteuler.net/problem=34">projecteuler.net
     * problem 34</a>.
     */
    @Test
    public void shouldSolveProblem34() {
        assertThat(sumOfDigitFactorial(145)).isEqualTo(145);
        assertThat(sumOfOfAllNumbersWhichAreEqualToSumOfDigitFactorial()).isEqualTo(40730);
    }

    /**
     * 计算所有满足条件“数字等于其各位数字阶乘之和”的数的总和
     */
    private static int sumOfOfAllNumbersWhichAreEqualToSumOfDigitFactorial() {
        // 所求数字的首位数只能是1或者2，则可进一步把上界缩小为2!+6⋅9!=2177282，经过分析，我们还可以进一步缩小上界，不过对于题目中的数字规模，这个上界已经足够了。
        return Stream.rangeClosed(3, 2_540_160) // 9! * 7 = 2 540 160 is a seven digit number, as is 9! * 8, therefor 9! * 7 is the definitive upper limit we have to investigate.
                .filter(i -> i == sumOfDigitFactorial(i))
                .sum().intValue();
    }

    /**
     * 计算给定数字的各位数字阶乘之和
     */
    private static int sumOfDigitFactorial(int num) {
        return CharSeq.of(Integer.toString(num))
                // 使用 map 方法对 CharSeq 中的每个字符进行操作，将其转换为对应的数字，并返回数字的阶乘值
                .map(c -> Character.digit(c, 10))
                .map(MEMOIZED_FACTORIAL)
                .sum().intValue();
    }
}
