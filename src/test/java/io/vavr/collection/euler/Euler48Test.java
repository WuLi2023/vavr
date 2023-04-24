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
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Euler48Test {

    // 定义一个 long 类型的常量 MOD，表示模数为 10^10，即取模时只保留后十位数
    private static final long MOD = 10_000_000_000L;

    /**
     * <strong>Problem 48: Self powers</strong>
     * <p>
     * The series, 1<sup>1</sup> + 2<sup>2</sup> + 3<sup>3</sup> + ... + 10<sup>10</sup> = 10405071317.
     * <p>
     * Find the last ten digits of the series, 1<sup>1</sup> + 2<sup>2</sup> + 3<sup>3</sup> + ... + 1000<sup>1000</sup>.
     * <p>
     * See also <a href="https://projecteuler.net/problem=48">projecteuler.net problem 48</a>.
     */
    @Test
    public void shouldSolveProblem48() {
        assertThat(sumPowers(10)).isEqualTo(405_071_317L);
        assertThat(sumPowers(1000)).isEqualTo(9_110_846_700L);
    }

    /*分析：此题的解法非常直接，不需要做太多的分析，直接求出题目中给出的指数和，再求其模 10<sup>10</sup> 就可以得到其最后十位数。*/

    /**
     * 计算给定整数范围内所有数值的自幂结果之和
     */
    private static long sumPowers(int max) {
        // 创建一个整数流，范围为从1到max-1
        return Stream.range(1, max)
                // 将当前数字替换为其自幂结果
                .map(Euler48Test::selfPower)
                // 将所有自幂结果相加，然后对 MOD 取模，从而得到最终的结果。
                .reduce(Euler48Test::sumMod);
    }

    /**
     * 计算给定 long 类型数值的自乘幂
     */
    private static long selfPower(long v) {
        // 生成一个无限流 powers，其中每个元素都是 v 不断自乘并取模的结果。
        final Stream<Long> powers = Stream.iterate(v, el -> multMod(el, el));
        return bits(v)
                .map(powers::get)
                // 在幂指数流的开头加入值 1，表示 v^0 = 1。
                .prepend(1L)
                // 使用 reduce 方法将流中的所有元素进行模乘运算，从而得到最终的自乘幂结果。
                .reduce(Euler48Test::multMod);
    }

    /**
     * 用于计算两个数相乘并对一个固定的模数取模后的结果。
     *
     * @param v1 要相乘的第一个数。
     * @param v2 要相乘的第二个数。
     * @return 相乘并对 MOD 取模后的结果。
     */
    private static long multMod(long v1, long v2) {
        // 生成 Stream<Long> 对象，用于保存每次“乘 2”后的值（即左移 1 位），
        // 每个元素都是前一个元素对 MOD 取模后的值再乘以 2 后对 MOD 取模后的值
        final Stream<Long> shifts = Stream.iterate(v1, el -> sumMod(el, el)); // 生成一个无限流 shifts，其中每个元素都是 v1 不断自乘并取模的结果。

        // 获取 v2 的二进制表示中所有为 1 的位数，并将它们转换为 Long 类型保存到 Stream<Integer> 对象中
        // 再获取每个为 1 的位在 shifts 中对应的值，并将所有值相加
        return bits(v2)
                // 将 v2 的每一位转换为对应的 shift 值
                .map(shifts::get)
                // 在最前面插入一个 0L
                .prepend(0L)
                // 将这些 shift 值与对应的 v1 的模乘结果相加，从而计算出最终的模乘结果。
                .reduce(Euler48Test::sumMod);
    }

    /*接受两个 long 类型参数 v1 和 v2，将它们相加，然后对 MOD 取模，最后返回计算结果。这样可以确保返回值始终在 0 到 MOD-1 之间，避免了出现溢出或负数的情况。*/
    /*取模运算在密码学和算法竞赛等领域非常常见，因为它可以避免数值过大而导致计算错误或安全问题。*/

    /**
     * 对两个 long 类型的整数进行求和并对结果取模，防止溢出。
     *
     * @param v1 第一个整数
     * @param v2 第二个整数
     * @return 求和后对 10_000_000_000L 取模的结果
     */
    private static long sumMod(long v1, long v2) {
        return (v1 + v2) % MOD;
    }

    /**
     * 通过 long 值的二进制位，返回其非 0 位所在的位置组成的流。
     * <p>
     * 符号 & 的作用是用来检查某个二进制位是否为 1。如果表达式 ((v >> b) & 1) 不为 0，说明 v 的二进制表示中第 b 位是 1，即该位为非 0 位。
     * </p>
     *
     * @param v 要处理的 long 值。
     * @return 非 0 位所在位置组成的流。
     */
    private static Stream<Integer> bits(long v) {
        // 从 0 开始的整数流
        return Stream.from(0)
                // 使用 takeWhile 方法来取出大于等于 0 的所有元素，直到元素对应的位数超过了 v 的二进制表示中最高位的位置。
                .takeWhile(b -> (v >> b) > 0)
                // 过滤掉值为 0 的元素，即只保留标记为非 0 的元素
                .filter(b -> ((v >> b) & 1) != 0);
    }

}
