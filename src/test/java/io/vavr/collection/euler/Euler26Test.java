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
import io.vavr.Tuple2;
import io.vavr.collection.CharSeq;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import io.vavr.control.Option;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <strong>Problem 26: Reciprocal cycles</strong>
 * <p>
 * A unit fraction contains 1 in the numerator. The decimal representation of
 * the unit fractions with denominators 2 to 10 are given:
 * <pre>
 * 1/2	= 0.5
 * 1/3	= 0.(3)
 * 1/4	= 0.25
 * 1/5	= 0.2
 * 1/6	= 0.1(6)
 * 1/7	= 0.(142857)
 * 1/8	= 0.125
 * 1/9	= 0.(1)
 * 1/10	= 0.1
 * </pre> Where 0.1(6) means 0.166666..., and has a 1-digit recurring cycle. It
 * can be seen that 1/7 has a 6-digit recurring cycle.
 * <p>
 * Find the value of d < 1000 for which 1/d contains the longest recurring cycle
 * in its decimal fraction part. <p>
 * See also
 * <a href="https://projecteuler.net/problem=26">projecteuler.net problem 26
 * </a>.
 * <analysis>
 * 这道题的解题思路可以大致分为两个部分，
 * 第一个部分是如何求任意单位分数的循环节，这需要使用一个简单的迭代计算就可以实现。
 * 第二个部分是为了提高算法的效率，我们需要尽可能的减小筛选的单位分数的范围，这需要我们从数论角度来分析单位分数的循环节问题。
 * <p>
 * 首先我们来看第一个部分，要求循环节的长度，我们可以建立一个列表记下每次除法中的被除数，由于都是单位分数，所以第一个被除数为1，如果被除数小于除数，则需要将被除数进一位再去除，所得余数为下一次除法中的被除数。
 * 如此循环下去，直到新出现的被除数在我们建立的列表中已经出现过了，之后的计算会重复之前的模式。
 * 此时一个循环已经完成，列表的长度就是循环节的长度。
 * 第二个部分我们需要尽量的缩小筛选的范围，排除那些明显不满足题目要求的单位分数。
 * </p>
 * </analysis>
 */
public class Euler26Test {

    @Test
    public void shouldSolveProblem26() {
        assertThat(recurringCycleLengthForDivisionOf1(2)._2).isEqualTo(0);
        assertThat(recurringCycleLengthForDivisionOf1(3)._2).isEqualTo(1);
        assertThat(recurringCycleLengthForDivisionOf1(4)._2).isEqualTo(0);
        assertThat(recurringCycleLengthForDivisionOf1(5)._2).isEqualTo(0);
        assertThat(recurringCycleLengthForDivisionOf1(6)._2).isEqualTo(1);
        assertThat(recurringCycleLengthForDivisionOf1(7)._2).isEqualTo(6);
        assertThat(recurringCycleLengthForDivisionOf1(8)._2).isEqualTo(0);
        assertThat(recurringCycleLengthForDivisionOf1(9)._2).isEqualTo(1);
        assertThat(recurringCycleLengthForDivisionOf1(10)._2).isEqualTo(0);
        assertThat(denominatorBelow1000WithTheLongetsRecurringCycleOfDecimalFractions()).isEqualTo(983);
    }


    /**
     * 查找分母在1到999之间的所有分数中，循环节长度最长的分数的分母。
     */
    private static int denominatorBelow1000WithTheLongetsRecurringCycleOfDecimalFractions() {
        return List.range(2, 1000)
                // 生成一个从2到999的整数列表，然后对每个整数调用recurringCycleLengthForDivisionOf1()方法，计算出对应的循环节长度，并将结果打包成一个Tuple2对象。
                .map(Euler26Test::recurringCycleLengthForDivisionOf1)
                // 从所有的Tuple2对象中找到循环节长度最长的那个对象，并返回其对应的分母。
                .maxBy(Tuple2::_2)
                .get()._1;
    }

    /**
     * 用于计算1除以一个正整数的结果的循环节长度。
     * 具体来说，该方法接受一个正整数divisor，然后通过一系列的计算和转换，计算出1除以divisor的结果的循环节长度，并返回结果。
     */
    private static Tuple2<Integer, Integer> recurringCycleLengthForDivisionOf1(int divisor) {
        // 将除数和循环节长度打包成一个Tuple2对象，作为结果返回
        return Tuple.of(
                divisor,
                // 将新的字符串对象传递给recurringCycleLengthInDecimalFractionPart()方法，计算出循环节长度。
                recurringCycleLengthInDecimalFractionPart(
                        /*将BigDecimal.ONE除以divisor，得到一个表示十进制小数的BigDecimal对象。
                        将BigDecimal对象转换为表示十进制小数的字符串，并使用CharSeq.of()方法将其转换为CharSeq对象。*/
                        CharSeq.of(BigDecimal.ONE.divide(BigDecimal.valueOf(divisor), 2000, RoundingMode.UP).toString())
                                .transform(removeLeadingZeroAndDecimalPoint()) // 删除小数点和前导零。
                                .transform(removeRoundingDigit()) // 删除四舍五入产生的尾数
                                .transform(removeTrailingZeroes()) // 删除小数点后面的多余的零
                                .mkString() // 将CharSeq对象转换为字符串，并使用mkString()方法生成一个新的字符串对象。
                ));
    }

    /**
     * 计算一个十进制小数的循环节长度。
     * 具体来说，该方法接受一个表示十进制小数的字符串，然后通过一系列的转换和计算，计算出该小数的循环节长度，并返回结果。
     * <p>
     * 使用了一些高阶函数和惰性求值的技巧，使得代码更加简洁和高效。
     * 其中，createCandidateCycles()方法、removeCandidatesLongerThanHalfTheFullString()方法和findFirstRecurringCycle()方法都返回一个Function1对象，表示对输入进行一次转换操作。
     * 这些Function1对象可以组合在一起，形成一个函数流，以实现连续的转换和计算。
     * </p>
     * <p>
     * 该方法可能存在一些边界条件和性能问题。
     * 例如，如果输入的字符串不是一个合法的十进制小数，那么该方法可能会返回错误的结果。
     * 另外，由于该方法需要反转字符串并生成Stream对象，可能会占用较多的内存和计算资源。
     * 在实际应用中，可能需要增加一些校验和优化的逻辑，以保证程序的正确性和性能。
     * </p>
     */
    private static int recurringCycleLengthInDecimalFractionPart(String decimalFractionPart) {
        return CharSeq.of(decimalFractionPart)// 将输入字符串转换为CharSeq对象
                .reverse()// 将CharSeq对象反转，使得操作可以从小数点后的位数开始计算
                /*将CharSeq对象转换为Stream对象，以实现惰性求值，避免不必要的计算。*/
                .toStream() // Stream is lazy evaluated which ensures the rest is only evaluated until the recurring cycle is found.
                // 生成候选循环节的Stream
                .transform(createCandidateCycles())
                // 删除长度大于字符串长度一半的候选循环节
                .transform(removeCandidatesLongerThanHalfTheFullString(decimalFractionPart))
                // 找到第一个重复出现的循环节
                .transform(findFirstRecurringCycle(decimalFractionPart))
                // 将找到的循环节长度作为结果返回
                .map(String::length)
                .getOrElse(0);
    }

    /**
     * 去除表示十进制小数的字符串中的前两位字符，即去除小数点和可能存在的前导零。
     * 具体来说，该方法返回一个函数，该函数接受一个CharSeq对象，表示十进制小数字符串，然后返回一个新的CharSeq对象，表示去除了前两位字符的十进制小数字符串。
     */
    private static Function1<CharSeq, CharSeq> removeLeadingZeroAndDecimalPoint() {
        /*将字符串的前两个字符删除。drop()方法是CharSeq接口的一个默认方法，用于删除字符串的前缀。在这里，由于小数点和前导零位于字符串的前两位，因此可以使用drop(2)方法直接删除这两个字符。*/
        return seq -> seq.drop(2);
    }

    /**
     * 去除表示十进制小数的字符串中的最后一位，即舍入位。
     * 具体来说，该方法返回一个函数，该函数接受一个CharSeq对象，表示十进制小数字符串，然后返回一个新的CharSeq对象，表示去除了最后一位的十进制小数字符串。
     * <p>
     * 需要注意到该方法并没有进行任何舍入操作，仅仅是删除了表示舍入位的字符。
     * 舍入操作通常需要结合上下文和具体的舍入规则来实现。在实际应用中，可能需要使用其他的方法来完成舍入操作。
     * </p>
     */
    private static Function1<CharSeq, CharSeq> removeRoundingDigit() {
        /*将字符串的最后一个字符删除。dropRight()方法是CharSeq接口的一个默认方法，用于删除字符串的后缀。在这里，由于舍入位位于字符串的最后一位，因此可以使用dropRight(1)方法直接删除最后一位字符。*/
        return seq -> seq.dropRight(1);
    }

    /**
     * 去除十进制小数字符串尾部的零。
     * 具体来说，该方法返回一个函数，该函数接受一个CharSeq对象，表示十进制小数字符串，然后返回一个新的CharSeq对象，表示去除了尾部零的十进制小数字符串。
     */
    private static Function1<CharSeq, CharSeq> removeTrailingZeroes() {
        /*将字符串反转，并使用dropWhile()方法删除所有连续的零，直到遇到第一个非零字符为止。然后，再次使用reverse()方法将字符串反转回来，得到最终的结果。*/
        return seq -> seq
                .reverse()
                .dropWhile(c -> c == '0') // Remove any trailing zeroes
                .reverse();
    }

    /**
     * 生成所有可能的候选循环节。
     * 具体来说，该方法返回一个函数，该函数接受一个字符流reversedDecimalFractionPart，代表表示十进制小数的字符串的小数部分（反转后的字符流），
     * 然后返回一个新的字符串流，其中包含了所有可能的候选循环节。
     */
    private static Function1<Stream<Character>, Stream<String>> createCandidateCycles() {
        return reversedDecimalFractionPart -> reversedDecimalFractionPart
                .map(String::valueOf)// 将字符流中的每个字符转换为一个字符串。
                .scan("", String::concat) // 对这些字符串进行累加，即将它们依次连接起来形成一个字符串流。
                /*在使用scan方法时，需要提供一个初始值，这里使用的是空字符串作为初始值。*/
                .drop(1); // Drop the first empty string created by scan;删除第一个元素，即空字符串，因为它是由scan方法自动添加的，没有实际的意义。
    }

    /**
     * 用于过滤掉长度超过十进制小数一半的候选循环节。
     * <p>
     * 方法接受一个表示十进制小数的字符串decimalFractionPart，并返回一个函数，
     * 该函数接受一个字符串流candidateCycles，代表所有可能的循环节，然后返回一个新的字符串流，其中过滤掉了长度超过一半的候选循环节。
     * </p>
     */
    private static Function1<Stream<String>, Stream<String>> removeCandidatesLongerThanHalfTheFullString(String decimalFractionPart) {
        // 使用filter方法对字符串流中的每个元素进行过滤，如果该元素的长度不超过decimalFractionPart长度的一半，则保留该元素，否则过滤掉该元素。
        return candidateCycles -> candidateCycles.filter(candidate -> decimalFractionPart.length() >= candidate.length() * 2);
    }

    /**
     * 用于找到一个十进制小数的循环节
     * <p>
     * 方法接受一个表示十进制小数的字符串decimalFractionPart，并返回一个函数，
     * 该函数接受一个字符串流reversedCandidateCycles，代表所有可能的循环节（字符串形式的反转），并返回一个Optional类型的对象，表示找到的循环节（如果有的话）。
     * </p>
     */
    private static Function1<Stream<String>, Option<String>> findFirstRecurringCycle(String decimalFractionPart) {
        // 对输入的字符串流进行操作
        return reversedCandidateCycles -> reversedCandidateCycles
                // 使用map方法将字符串流中的每个元素进行反转，并使用mkString方法将它们转换为字符串。
                .map(s -> CharSeq.of(s).reverse().mkString())
                // 使用find方法查找一个满足条件的元素，即该元素等于decimalFractionPart中最后一个循环节的前一部分（即循环节的长度乘以2）到最后一个循环节之前的部分。如果找到了满足条件的元素，则使用Option.some方法将其封装为Optional对象返回，否则返回Option.none。
                .find(candidate -> candidate.equals(decimalFractionPart.substring(decimalFractionPart.length() - (candidate.length() * 2), decimalFractionPart.length() - candidate.length())));
    }
}
