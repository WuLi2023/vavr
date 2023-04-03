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
import io.vavr.collection.List;
import org.junit.Test;

import static io.vavr.API.*;
import static org.assertj.core.api.Assertions.assertThat;

public class Euler38Test {

    /*判断一个数字是否为某个数的倍数，需要将该数字除以该数，如果能够整除，则该数字是该数的倍数。
      在这里，我们从左到右遍历了数字的每一个位置，将数字分成两部分：前i位和后n-i位。前i位表示当前的乘数，后n-i位表示当前的被乘数。
      因此，我们将前i位的数字转换为整数，作为当前的乘数，然后将后n-i位的数字作为当前的被乘数，判断它是否能够被当前的乘数整除。
      如果能够整除，则递归调用isPandigitalMultipleRest方法，将被乘数的前一部分作为新的乘数，被乘数的后一部分作为新的被乘数，乘数的值加1作为新的乘数，继续判断是否符合条件。
      如果不符合条件，则返回false。

      因此，在第一次调用isPandigitalMultipleRest方法时，将当前的乘数初始化为2，表示判断当前的数字是否能够被2整除。
      如果能够整除，则继续判断当前的数字除以2的结果是否符合条件；
      如果不符合条件，则返回false。在后续的递归调用中，乘数的值将加1，表示判断当前的数字是否能够被3、4、5等依次递增的数整除。*/

    /**
     * <strong>Problem 38 Pandigital multiples</strong>
     * <p>
     * Take the number 192 and multiply it by each of 1, 2, and 3:
     * <pre>
     * 192 × 1 = 192
     * 192 × 2 = 384
     * 192 × 3 = 576
     * </pre>
     * <p>
     * By concatenating each product we get the 1 to 9 pandigital, 192384576. We
     * will call 192384576 the concatenated product of 192 and (1,2,3)
     * <p>
     * The same can be achieved by starting with 9 and multiplying by 1, 2, 3,
     * 4, and 5, giving the pandigital, 918273645, which is the concatenated
     * product of 9 and (1,2,3,4,5).
     * <p>
     * What is the largest 1 to 9 pandigital 9-digit number that can be formed
     * as the concatenated product of an integer with (1,2, ... , n) where n >
     * 1?
     * <p>
     * See also <a href="https://projecteuler.net/problem=38">projecteuler.net
     * problem 38</a>.
     */
    @Test
    public void shouldSolveProblem38() {
        assertThat(isPandigitalMultiple(CharSeq.of("192384576"))).isTrue();
        assertThat(isPandigitalMultiple(CharSeq.of("918273645"))).isTrue();

        assertThat(largest1To9PandigitalMultiple().mkString()).isEqualTo("932718654");
    }

    /**
     * 查找最大的1到9的全排列中，能够表示为某个数的倍数，并且该数字和该数的乘积所得到的数字是1到9的全排列的功能。
     */
    private static CharSeq largest1To9PandigitalMultiple() {
        return CharSeq.of("87654321")
                // 使用permutations方法生成该CharSeq对象的所有全排列，
                .permutations()
                // 并使用map方法将每个全排列转换为一个数字，
                .map(CharSeq::mkString)
                .map(Integer::valueOf)
                // 接着使用sorted方法将这些数字进行排序，然后使用reverse方法将它们反转，
                .sorted()
                .reverse()
                // 使用map方法在每个数字前面添加一个9，因为已知918273645是符合条件的，所以我们只需要考虑以9开头的数字。
                .map(i -> "9" + i) // Since 918273645 is known we don't have to investigate numbers not starting with a 9.
                .map(CharSeq::of)
                // 使用find方法找到第一个符合条件的数字，并将其转换为CharSeq对象返回
                .find(Euler38Test::isPandigitalMultiple)
                .get();
    }

    /**
     * 判断一个数字是否为某个数的倍数，并且该数字和该数的乘积所得到的数字是1到9的全排列
     * 接受一个CharSeq类型的参数pandigital，表示一个数字的全排列，返回一个boolean类型的结果，表示该数字是否为某个数的倍数，并且该数字和该数的乘积所得到的数字是1到9的全排列。
     */
    private static boolean isPandigitalMultiple(CharSeq pandigital) {
        // 遍历1到n-1之间的整数，判断是否存在某个i，使得pandigital的前i个字符组成的数字能够被2整除，在第一次调用isPandigitalMultipleRest方法时，将当前的乘数初始化为2，表示判断当前的数字是否能够被2整除。
        return List.rangeClosed(1, pandigital.length() - 1)// 生成一个从 1 到字符串长度减 1 的整数列表
                /*对于列表中的每个整数 i，将字符串的前 i 个字符转换为一个整数，作为 `第一个乘数`，
                  调用 isPandigitalMultipleRest() 方法判断 `剩余的字符串` 是否为第一个乘数的倍数，并且这个数和它的倍数构成的数字组成了一个全数位的 pandigital 数字。*/
                // 如果存在这样的i，则返回true，否则返回false。
                .exists(i -> isPandigitalMultipleRest(pandigital.drop(i), Integer.parseInt(pandigital.take(i).mkString()), 2));
    }

    /**
     * 判断一个字符串是否为某个数的倍数，并且这个数和它的倍数构成的数字组成了一个全数位的 pandigital 数字。
     */
    private static boolean isPandigitalMultipleRest(CharSeq pandigitalRest, int multiplicand, int multiplicator) {
        return Match(pandigitalRest.length()).of(
                // 如果长度为 0，则返回 true
                Case($(0), true),
                // 生成一个从 1 到字符串长度的整数列表，然后找到第一个满足条件的值，如果找到了，则返回 true，否则返回 false
                Case($(), length -> List.rangeClosed(1, length)
                        // 对于列表中的每个整数 i，将字符串的前 i 个字符转换为一个整数，判断这个整数是否等于 multiplicand * multiplicator。如果是，则递归判断剩余的字符串是否也满足条件
                        .find(i -> Integer.parseInt(pandigitalRest.take(i).mkString()) == multiplicand * multiplicator)
                        // 将 pandigitalRest.drop(i) 作为新的字符串，multiplicand 不变，multiplicator 加 1。
                        .map(i -> isPandigitalMultipleRest(pandigitalRest.drop(i), multiplicand, multiplicator + 1))
                        // 如果列表中没有任何一个整数满足条件，则返回 false
                        .getOrElse(false)
                )
        );
    }
}
