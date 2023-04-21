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
import io.vavr.collection.Seq;
import io.vavr.collection.List;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Euler43Test {

    /**
     * <strong>Problem 43 Sub-string divisibility</strong>
     * <p>
     * The number, 1406357289, is a 0 to 9 pandigital number because it is made
     * up of each of the digits 0 to 9 in some order, but it also has a rather
     * interesting sub-string divisibility property.</p>
     * <p>
     * Let <i>d</i><sub>1</sub> be the 1<sup>st</sup> digit,
     * <i>d</i><sub>2</sub> be the 2<sup>nd</sup> digit, and so on. In this way,
     * we note the following:</p>
     * <pre>
     * <ul>
     * <li><i>d</i><sub>2</sub><i>d</i><sub>3</sub><i>d</i><sub>4</sub>=406 is divisible by 2</li>
     * <li><i>d</i><sub>3</sub><i>d</i><sub>4</sub><i>d</i><sub>5</sub>=063 is divisible by 3</li>
     * <li><i>d</i><sub>4</sub><i>d</i><sub>5</sub><i>d</i><sub>6</sub>=635 is divisible by 5</li>
     * <li><i>d</i><sub>5</sub><i>d</i><sub>6</sub><i>d</i><sub>7</sub>=357 is divisible by 7</li>
     * <li><i>d</i><sub>6</sub><i>d</i><sub>7</sub><i>d</i><sub>8</sub>=572 is divisible by 11</li>
     * <li><i>d</i><sub>7</sub><i>d</i><sub>8</sub><i>d</i><sub>9</sub>=728 is divisible by 13</li>
     * <li><i>d</i><sub>8</sub><i>d</i><sub>9</sub><i>d</i><sub>10</sub>=289 is divisible by 17</li>
     * </ul>
     * </pre>
     * <p>
     * Find the sum of all 0 to 9 pandigital numbers with this property.
     * <p>
     * See also <a href="https://projecteuler.net/problem=43">projecteuler.net
     * problem 43</a>.
     */
    @Test
    public void shouldSolveProblem43() {
        final Seq<Long> result = tenDigitPandigitalsWithProperty();
        Assertions.assertThat(result).contains(1406357289L);

        assertThat(result.sum().longValue()).isEqualTo(16695334890L);
    }

    /*  分析：这道题有两种解法，第一种解法利用题中所列素数的整除性质，并遍历所有0至9全数字的组合寻找满足要求数字并累加求和。
        第二种方法则需要在整除性质的基础上，对各个数位的出现数字的可能性加以分析，迭代解出满足要求的数字，使用这种方法可以直接笔算出相应结果，不需要编程。
        相对来说，第一种方法更好理解，但效率较低；第二种方法效率更高，但理解起来要麻烦一些。*/

    /*  这段代码是一个 Java 方法，返回类型为 Seq<Long>。
        它生成了所有由数字 0-9 组成的十位数，并且满足以下条件：该十位数的后 9 个数字组成的三位数分别能够被给定列表 DIVISORS 中的每个整数整除。
        方法中定义了两个常量 ALL_DIGITS 和 DIVISORS，ALL_DIGITS 是包含数字 "0123456789" 的 CharSeq 对象，DIVISORS 是包含整数 2,3,5,7,11,13和17 的 List<Integer> 对象。
        方法使用 ALL_DIGITS.combinations(2) 来生成所有可能的前两个数字组合，并使用 flatMap(CharSeq::permutations) 将这些组合转换为所有可能排列。
        然后对于每一种排列，将其作为第一次迭代时累加器中唯一元素（即 firstTwoDigits），并在 DIVISORS 列表上进行折叠操作。
        在每次迭代过程中，从 digitsSoFar 中删除已经出现过的字符，并添加下一个未出现过的字符 nextDigit。
        最终筛选出符合要求（能够被相应除数整除）的尾部九位数字 tailDigitsWithProperty 并将其与首字母拼接起来形成完整十位数。
        最后通过 map(CharSeq::parseLong) 将结果转换为 Long 类型并返回。
        总之，此代码实现了寻找特定性质下所有可能情况所组成的数字列表。 */

    private static Seq<Long> tenDigitPandigitalsWithProperty() {
        final CharSeq ALL_DIGITS = CharSeq.of("0123456789"); // 所有数字字符组成的字符序列常量
        final List<Integer> DIVISORS = List.of(2, 3, 5, 7, 11, 13, 17); // 需要验证的因子列表

        // 将所有数字字符放入一个字符序列中，然后：
        return ALL_DIGITS
                // 生成所有可能的两个数字字符的组合
                .combinations(2)
                // 对每个组合进行排列，得到新的字符序列。这样生成了所有可能的两位数字前缀，即我们要生成的数字的前两位。
                .flatMap(CharSeq::permutations)
                .flatMap(firstTwoDigits -> DIVISORS
                        // 对于每个两位数字前缀，该方法使用 foldLeft 迭代 DIVISORS 列表，并构建满足特定条件的数字序列。
                        .foldLeft(List.of(firstTwoDigits), (accumulator, divisor) -> accumulator // 累加器的初始值是一个包含两位数字前缀的列表。
                                // 对于每个除数，使用 flatMap 生成可以附加到当前数字序列的所有可能下一个数字字符。
                                .flatMap(digitsSoFar -> ALL_DIGITS // 在已有字符序列上添加下一个数字字符
                                        .removeAll(digitsSoFar) // 剩下未使用的数字字符
                                        .map(nextDigit -> digitsSoFar.append(nextDigit)) // 拼接为新的字符序列
                                )
                                .filter(digitsToTest -> digitsToTest.takeRight(3).parseInt() % divisor == 0) // 验证最后三位是否可以被因子整除
                        )
                )
                .map(tailDigitsWithProperty -> tailDigitsWithProperty
                        .prepend(ALL_DIGITS // 最后一位还未使用的数字字符中取一个加在最前面
                                .removeAll(tailDigitsWithProperty)
                                .head()
                        )
                )
                // 将每个字符序列转换为长整型数值
                .map(CharSeq::parseLong);
    }
}
