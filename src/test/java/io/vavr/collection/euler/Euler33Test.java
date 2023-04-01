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
import io.vavr.Tuple2;
import io.vavr.collection.List;
import org.junit.Test;

import static io.vavr.collection.euler.Utils.factors;
import static org.assertj.core.api.Assertions.assertThat;

public class Euler33Test {

    /**
     * <strong>Problem 33: Digit cancelling fractions</strong>
     * <p>
     * The fraction 49/98 is a curious fraction, as an inexperienced
     * mathematician in attempting to simplify it may incorrectly believe that
     * 49/98 = 4/8, which is correct, is obtained by cancelling the 9s.
     * <p>
     * We shall consider fractions like, 30/50 = 3/5, to be trivial examples.
     * <p>
     * There are exactly four non-trivial examples of this type of fraction,
     * less than one in value, and containing two digits in the numerator and
     * denominator.
     * <p>
     * If the product of these four fractions is given in its lowest common
     * terms, find the value of the denominator.
     * <p>
     * See also <a href="https://projecteuler.net/problem=33">projecteuler.net
     * problem 33</a>.
     */
    @Test
    public void shouldSolveProblem33() {
        assertThat(isNonTrivialDigitCancellingFraction(Tuple.of(49, 98))).isTrue();
        assertThat(isNonTrivialDigitCancellingFraction(Tuple.of(30, 50))).isFalse();
        assertThat(isNonTrivialDigitCancellingFraction(Tuple.of(21, 22))).isFalse();
        assertThat(lowestCommonDenominatorOfProductOfTheFourNonTrivialdigitCancellingFractions()).isEqualTo(100);
    }

    /**
     * 计算四个非平凡数字约分分数的乘积的最小公倍数
     */
    private static int lowestCommonDenominatorOfProductOfTheFourNonTrivialdigitCancellingFractions() {
        // 生成10到98之间的整数，并对它们进行排列组合得到所有可能的分数
        return List.rangeClosed(10, 98)
                .flatMap(n -> List.rangeClosed(n + 1, 99).map(d -> Tuple.of(n, d)))
                // 过滤掉其中的平凡数字约分分数
                .filter(Euler33Test::isNonTrivialDigitCancellingFraction)
                // 将所有分数相乘，得到分数的乘积。将剩下的元组（分数）依次相乘，得到一个新的元组（分数）。
                .fold(Tuple.of(1, 1), Euler33Test::multiplyFractions)
                // 将新的元组（分数）作为参数，调用之前定义的simplifyFraction方法，得到一个最简分数。返回最简分数的分母。
                .apply(Euler33Test::simplifyFraction)._2;
    }

    /**
     * 接受一个Tuple2<Integer, Integer>类型的参数fraction，表示一个分数，返回一个boolean类型的结果，表示该分数是否可以通过约分其中的一个数字来化简。
     * 为非平凡的数字约分分数。
     */
    private static boolean isNonTrivialDigitCancellingFraction(Tuple2<Integer, Integer> fraction) {
        // 将分数的分子转换为CharSeq对象，并过滤掉其中的0字符
        return CharSeq.of(fraction._1.toString())
                .filter(d -> d != '0')
                // 查找分母中是否包含与分子中相同的字符
                .find(d -> CharSeq.of(fraction._2.toString()).contains(d))
                // 如果包含，则判断该分数是否可以通过约分该字符来化简
                .map(d -> fractionCanBeSimplifiedByCancellingDigit(fraction, d))
                // 如果不包含，则返回false
                .getOrElse(false);
    }

    /**
     * 接受一个Tuple2<Integer, Integer>类型的参数fraction，表示一个分数，以及一个char类型的参数d，表示要约分的数字，返回一个boolean类型的结果，表示该分数是否可以通过约分d来化简。
     */
    private static boolean fractionCanBeSimplifiedByCancellingDigit(Tuple2<Integer, Integer> fraction, char d) {
        return Tuple.of(CharSeq.of(fraction._1.toString()).remove(d), CharSeq.of(fraction._2.toString()).remove(d))
                .map(CharSeq::mkString, CharSeq::mkString)
                .map(Double::valueOf, Double::valueOf)
                .apply((d1, d2) -> fraction._1 / d1 == fraction._2 / d2);
    }

    /**
     * 接受两个Tuple2<Integer, Integer>类型的参数f1和f2，表示两个分数，返回一个Tuple2<Integer, Integer>类型的结果，表示两个分数的乘积。
     */
    private static Tuple2<Integer, Integer> multiplyFractions(Tuple2<Integer, Integer> f1, Tuple2<Integer, Integer> f2) {
        // 将两个分数的分子和分母分别相乘，得到新的分子和分母
        return Tuple.of(f1._1 * f2._1, f1._2 * f2._2);
    }

    /**
     * 接受两个整数参数numerator和denominator，表示分数的分子和分母，返回一个Tuple2<Integer, Integer>类型的结果，表示化简后的分数。
     */
    private static Tuple2<Integer, Integer> simplifyFraction(int numerator, int denominator) {
        // 使用factors方法获取numerator的所有因子
        return factors(numerator)
                // 转换为int类型
                .map(Long::intValue)
                // 过滤掉 1
                .filter(f -> f != 1)
                // 从大到小排序
                .sorted()
                // 从大到小遍历因子,找到第一个可以整除denominator的因子f
                .findLast(f -> denominator % f == 0)
                // 递归调用simplifyFraction方法，将numerator和denominator分别除以f，得到新的分子和分母，继续进行化简
                .map(f -> simplifyFraction(numerator / f, denominator / f))
                // 如果没有找到可以整除denominator的因子，则返回原始的分数。
                .getOrElse(Tuple.of(numerator, denominator));
    }
}
