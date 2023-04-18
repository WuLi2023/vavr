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

import static org.assertj.core.api.Assertions.assertThat;

public class Euler40Test {

    /**
     * <strong>Problem 40 Champernowne's constant</strong>
     * <p>
     * An irrational decimal fraction is created by concatenating the positive
     * integers:
     * <p>
     * 0.12345678910<b>1</b>112131415161718192021...
     * <p>
     * It can be seen that the 12<sup>th</sup> digit of the fractional part is
     * 1.
     * <p>
     * If <i>d</i><sub>n</sub> represents the <i>n</i><sup>th</sup> digit of the
     * fractional part, find the value of the following expression.
     * <pre>
     * <i>d</i><sub>1</sub> × <i>d</i><sub>10</sub> × <i>d</i><sub>100</sub> × <i>d</i><sub>1000</sub> × <i>d</i><sub>10000</sub> × <i>d</i><sub>100000</sub> × <i>d</i><sub>1000000</sub>
     * </pre>
     * <p>
     * See also <a href="https://projecteuler.net/problem=40">projecteuler.net
     * problem 40</a>.
     */
    @Test
    public void shouldSolveProblem40() {
        assertThat(decimalDigitAtPosition(12)).isEqualTo('1');

        assertThat(solution()).isEqualTo(210);
    }

    /*通过分析钱珀努恩数的规律，我们可以发现它小数点后的构成方法如下：首先是九个一位数、然后是九十个二位数，再是九百个的三位数，依次类推。
      根据这个规律，我们可以推断出每个位数的从第几位开始，到第几位结束，比如一位数从第一位开始，到第九位结束；
      二位数的区域共有九十个数，因此需要占180个位置，则应该在第189位结束；
      三位数区域共有九百个数，需要占2700个位置，则应该在第2889位结束。
      依次类推，我们可以推算出每个区域开始和结束的位置。*/

    private static int solution() {
        // 生成从1开始的、每次扩大10倍的无限序列
        return Stream.iterate(1, i -> i * 10)
                // 取小于等于1,000,000的元素
                .takeWhile(i -> i <= 1_000_000)
                // 根据位置获取数字
                .map(Euler40Test::decimalDigitAtPosition)
                // 将数字字符转换为数字，然后计算乘积
                .map(c -> Character.digit(c, 10))
                .fold(1, (i1, i2) -> i1 * i2);
    }

    /**
     * 返回指定位置上的十进制数字字符。
     *
     * @param num 要获取的数字在序列中的位置，从1开始计数
     * @return 位于num位置上的十进制数字字符
     * @throws IndexOutOfBoundsException 如果num超出有效范围[1, 1000000]
     */
    private static char decimalDigitAtPosition(int num) {
        // 从预先生成的流中获取指定位置上的数字字符
        return FIRST_1_000_000_DECIMALS.get(num - 1);
    }

    /*创建了一个包含前一百万个十进制数的字符流。具体操作是：从整数1开始创建一个无限流，将每个整数转换成字符串后使用flatMap方法将其转换为CharSeq字符流，最后使用take方法截取前1000000个字符。*/
    private static final Stream<Character> FIRST_1_000_000_DECIMALS = Stream.from(1)
            .map(String::valueOf)
            .flatMap(CharSeq::of)
            .take(1_000_000);
}
