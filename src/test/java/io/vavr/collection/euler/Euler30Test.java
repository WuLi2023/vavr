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

import io.vavr.Tuple;
import io.vavr.collection.CharSeq;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <strong>Problem 30: Digit fifth powers</strong>
 * <p>
 * Surprisingly there are only three numbers that can be written as the sum of
 * fourth powers of their digits:
 * <pre>
 * 1634 = 1^4 + 6^4 + 3^4 + 4^4
 * 8208 = 8^4 + 2^4 + 0^4 + 8^4
 * 9474 = 9^4 + 4^4 + 7^4 + 4^4
 * </pre>
 * <p>
 * As 1 = 1^4 is not a sum it is not included.
 * <p>
 * The sum of these numbers is 1634 + 8208 + 9474 = 19316.
 * <p>
 * Find the sum of all the numbers that can be written as the sum of fifth
 * powers of their digits.
 * <p>
 * See also
 * <a href="https://projecteuler.net/problem=30">projecteuler.net problem 30
 * </a>.
 */
public class Euler30Test {

    @Test
    public void shouldSolveProblem26() {
        assertThat(sumOfAllTheNumbersThatCanBeWrittenAsTheSumOfPowersOfTheirDigits(4)).isEqualTo(19316);
        assertThat(sumOfAllTheNumbersThatCanBeWrittenAsTheSumOfPowersOfTheirDigits(5)).isEqualTo(443_839);
    }

    /**
     * 计算所有可以写成其数字的powers次幂之和的数字的总和
     */
    private static long sumOfAllTheNumbersThatCanBeWrittenAsTheSumOfPowersOfTheirDigits(int powers) {
        // 生成一个从10到最大可能的数字的列表
        return List.rangeClosed(10, maximalSumForPowers(powers))
                // 对列表中的每个数字进行过滤，只保留那些可以写成其数字的powers次幂之和的数字。
                .filter(i -> sumOfPowersOfDigits(powers, i) == i)
                // 将列表中的所有数字相加
                .sum().longValue();
    }

    /**
     * 找到一个具有指定幂次数（powers）的最大和
     */
    private static long maximalSumForPowers(int powers) {
        // 从1开始创建一个无限流
        return Stream.from(1)
                // 将每个整数i映射到一个二元组(x,y)，其中x是一个i位的数字，所有数字都是9，y 是 i 个 9^powers 的总和。
                .map(i -> Tuple.of((long) Math.pow(10, i) - 1, List.fill(i, () -> Math.pow(9, powers)).sum().longValue()))
                // 找到第一个二元组，其中x大于y，然后返回x。
                .find(t -> t._1 > t._2)
                // 可能会抛出NoSuchElementException异常
                .map(t -> t._1).get();
    }

    /**
     * 计算num的每个数字的powers次幂的总和
     */
    private static long sumOfPowersOfDigits(int powers, long num) {
        // 将num转化为字符串并创建一个字符序列
        return CharSeq.of(Long.toString(num))
                // 将字符序列中的每个字符转换为数字，使用Character.digit()方法并指定10进制
                .map(c -> Character.digit(c, 10))
                // 将每个数字提升为powers次幂，并将结果转换为long类型。
                .map(d -> (long) Math.pow(d, powers))
                // 将结果流中的所有值相加，并将结果转换为long类型。
                .sum().longValue();
    }
}
