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
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Vector;
import org.junit.Test;

import static io.vavr.API.*;
import static io.vavr.collection.Stream.rangeClosed;
import static org.assertj.core.api.Assertions.assertThat;

public class Euler17Test {
    /**
     * <strong>Problem 17: Number letter counts</strong>
     * <p>
     * If the numbers 1 to 5 are written out in words: one, two, three, four,
     * five, then there are 3 + 3 + 5 + 4 + 4 = 19 letters used in total.
     * <p>
     * If all the numbers from 1 to 1000 (one thousand) inclusive were written
     * out in words, how many letters would be used?
     * <p>
     * NOTE: Do not count spaces or hyphens. For example, 342 (three hundred and
     * forty-two) contains 23 letters and 115 (one hundred and fifteen) contains
     * 20 letters. The use of "and" when writing out numbers is in compliance
     * with British usage.
     */
    @Test
    public void shouldSolveProblem17() {
        runTestsFor(new SolutionA());
        runTestsFor(new SolutionB());

        // Additional capability, only for more general solution B
        assertThat(new SolutionB().letterCount(Integer.MAX_VALUE)).isEqualTo("twobilliononehundredandfortysevenmillionfourhundredandeightythreethousandsixhundredandfortyseven".length());
    }

    private static void runTestsFor(SolutionProblem17 solution) {
        List.of("one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen", "twenty", "twentyone")
                .zipWithIndex()
                .forEach(t -> {
                    final int number = t._2 + 1;
                    final String numberAsString = t._1;
                    assertThat(numberAsString).hasSize(solution.letterCount(number));
                });

        assertThat(solution.letterCount(200)).isEqualTo("twohundred".length());
        assertThat(solution.letterCount(201)).isEqualTo("twohundredandone".length());

        assertThat(solution.letterCount(342)).isEqualTo(23);
        assertThat(solution.letterCount(115)).isEqualTo(20);

        assertThat(solution.letterCount(rangeClosed(1, 5))).isEqualTo(19);
        assertThat(solution.letterCount(rangeClosed(1, 1000))).isEqualTo(21124);
    }

    private interface SolutionProblem17 {
        int letterCount(int num);

        /**
         * 计算给定整数序列（Seq<Integer> range）中所有整数的字母数之和
         *
         * @param range 整数序列
         * @return int 和
         */
        default int letterCount(Seq<Integer> range) {
            return range
                    // 调用了字母计数器（letterCount）方法，该方法接受一个整数参数并返回该整数的数字名称的长度。
                    .map(this::letterCount)
                    // 将结果相加返回
                    .sum().intValue();
        }
    }

    static final String CONJUNCTION = "and";
    static final Map<Integer, String> LENGTHS = List.of(
                    1, "one",
                    2, "two",
                    3, "three",
                    4, "four",
                    5, "five",
                    6, "six",
                    7, "seven",
                    8, "eight",
                    9, "nine",
                    10, "ten",
                    11, "eleven",
                    12, "twelve",
                    13, "thirteen",
                    14, "fourteen",
                    15, "fifteen",
                    16, "sixteen",
                    17, "seventeen",
                    18, "eighteen",
                    19, "nineteen",
                    20, "twenty",
                    30, "thirty",
                    40, "forty",
                    50, "fifty",
                    60, "sixty",
                    70, "seventy",
                    80, "eighty",
                    90, "ninety",
                    100, "hundred",
                    1_000, "thousand",
                    1_000_000, "million",
                    1_000_000_000, "billion"
            )
            // 分组
            .grouped(2)
            // 转换
            .toSortedMap(pair -> Tuple.of((Integer) pair.get(0), (String) pair.get(1)));

    /**
     * Solution using Vavr Pattern Matching.
     * 实现了SolutionProblem17接口，并提供了letterCount方法，该方法接受一个整数参数num，并返回num的数字名称的字母数
     */
    private static class SolutionA implements SolutionProblem17 {
        @Override
        public int letterCount(int num) {
            // 使用Match.of方法对num进行模式匹配，根据num的大小选择不同的计算方式。
            return Match(num).of( /*@formatter:off*/
                    /* 当num大于等于1000时，将num分为千位和百位以下部分，分别计算千位的数字名称长度、"thousand"的长度和`百位`以下部分的数字名称字母数，并将它们相加返回。*/
                    Case($(n -> n >= 1000), n -> length(n / 1000) + length(1000) + letterCount(n % 1000)),
                    /* 当num大于等于100但小于1000时，根据百位以下部分是否为0选择不同的计算方式，并将它们相加返回。 */
                    Case($(n -> n >= 100), n -> Match(n).of(
                            Case($(n1 -> (n1 % 100) > 0), n1 -> length(n1 / 100) + length(100) + CONJUNCTION.length() + letterCount(n1 % 100)),
                            // 百位一下部分为 0
                            Case($(), length(n / 100) + length(100)))),
                    /* 当num大于等于20但小于100时，根据个位和十位的数字名称长度计算总的数字名称字母数。 */
                    Case($(n -> n >= 20), n -> length(n - (n % 10)) + letterCount(n % 10)),
                    // 当num等于0时，返回0。
                    Case($(0), 0),
                    // 对于其他情况，直接返回数字名称的长度。
                    Case($(), n -> length(n))
            ); /*@formatter:on*/
        }

        private static int length(int number) {
            return LENGTHS.get(number).map(String::length).get();
        }
    }

    /**
     * A more general solution using functionality of the Vavr Collections.
     * 实现了SolutionProblem17接口，并提供了letterCount方法，该方法接受一个整数参数number，并返回number的数字名称的字母数。
     */
    private static class SolutionB implements SolutionProblem17 {
        @Override
        public int letterCount(int number) {
            // 用asText方法将number转换为数字名称，并获取该名称的长度返回。
            return asText(number).length();
        }

        private static String asText(int number) {
            /*  使用foldRight方法遍历LENGTHS Map对象中的每一个数字和它们的名称，并将它们转换为数字名称的文本表示。
                将初始值设为一个空Vector和number，表示当前剩余的数字是number。
                在每次迭代中，获取`当前数字`和它的`文本表示`、当前已经转换的数字名称的Vector以及当前剩余的数字。
            */
            return LENGTHS.foldRight(
                    Tuple.of(Vector.<String>empty(), number),
                    (magnitudeAndText, lengthsAndRemainder) -> {
                        final int magnitude = magnitudeAndText._1;
                        final int remainder = lengthsAndRemainder._2;
                /*  如果剩余的数字大于等于当前数字的大小且不为0，就将当前数字的名称添加到文本表示中，并将当前剩余的数字更新为当前数字的余数。
                    然后返回更新的向量和数字。
                    如果剩余的数字小于当前数字的大小或者为0，则直接返回更新的向量和数字，不再继续迭代。
                    为了支持百位以下的数字名称，asText方法还调用了asText(magnitude, text, chunks, remainder)方法，
                    该方法接受当前数字的大小、当前数字的名称、当前已经转换的数字名称的Vector以及当前剩余的数字作为参数，
                    并根据剩余数字的大小和当前数字的大小计算出百位以下的数字名称的文本表示。
                */
                        return ((remainder >= magnitude) && (remainder > 0)) ? asText(magnitude, magnitudeAndText._2, lengthsAndRemainder._1, remainder)
                                : lengthsAndRemainder;
                    }
            )._1.mkString();
        }

        /**
         * 根据剩余数字的大小和当前数字的大小计算出百位以下的数字名称的文本表示
         *
         * @param magnitude 当前数字的大小
         * @param text      当前数字的名称
         * @param chunks    当前已经转换的数字名称的Vector
         * @param remainder 当前剩余的数字
         * @return io.vavr.Tuple2<io.vavr.collection.Vector < java.lang.String>,java.lang.Integer>
         */
        private static Tuple2<Vector<String>, Integer> asText(int magnitude, String text, Vector<String> chunks, int remainder) {
            /* 如果剩余数字大于等于100 */
            if (remainder >= 100) {
                // 就将剩余数字除以当前数字大小，将商转换为数字名称的文本表示，并将它添加到当前数字名称的文本表示中。
                text = asText(remainder / magnitude) + text;
                // 如果当前数字的大小小于1000且剩余数字的余数不为0，
                if ((remainder < 1000) && ((remainder % magnitude) != 0)) {
                    // 则在当前数字名称的文本表示中添加一个连接词。
                    text += CONJUNCTION;
                }
            }
            // 最后返回更新的向量和数字。
            return Tuple.of(chunks.append(text), remainder % magnitude);
        }
    }
}
