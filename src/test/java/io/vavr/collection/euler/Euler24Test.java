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

import io.vavr.API;
import io.vavr.Function1;
import io.vavr.collection.List;
import io.vavr.collection.Traversable;
import org.junit.Test;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.collection.euler.Utils.factorial;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * <strong>Problem 24: Lexicographic permutations</strong>
 * <p>
 * A permutation is an ordered arrangement of objects. For example, 3124 is one
 * possible permutation of the digits 1, 2, 3 and 4. If all of the permutations
 * are listed numerically or alphabetically, we call it lexicographic order. The
 * lexicographic permutations of 0, 1 and 2 are:
 * <p>
 * 012 021 102 120 201 210
 * <p>
 * What is the millionth lexicographic permutation of the digits 0, 1, 2, 3, 4,
 * 5, 6, 7, 8 and 9?
 * <p>
 * See also <a href="https://projecteuler.net/problem=24">projecteuler.net
 * problem 24</a>.
 */
public class Euler24Test {

    /*一个排列是某些对象的有序组合，例如，3124就是数字1，2，3，4的一种可能排列。
    如果所有排列按数字从小到在与或者字母表从前到后的顺序排列，我们称其为字典排列。
    数字0，1，2的字典排列为：012，021，102，120，201，210。从零到九的所有数字构成的字典排列中，第一百万个数字是多少？*/

    /*分析：为了对简化叙述，我们先看一个简单的情形：求由0,1,2,3构成的字典排列中第十个是多少？显然四个数可以形成的排列有4!=24 种。
    由于字典排列是从小到大排列的，那么先看首位是零的情况，固定首位是零，则剩下三位共有3!=6 种情况，既然要求第十个数，则首位必然是零，又因为首位是一的排列也有六种，所以第十个数的首位必然是一，且应该处于以一为首位的数字的第四位(十除六余四)。
    接下来我们要考虑的是在由0,2,3构成的字典排列中找到第四位，这个数是230，则所求的数应该是1230。
    这里我们可以看到，我们实际上把所求问题分解成了一个小规模的问题，即把求0,1,2,3构成的字典排列第十个数的问题，转化为求0,2,3构成的字典的排列中第四个数的问题。
    因此，这个问题可以使用动态规划的方式求解。*/
    @Test
    public void shouldSolveProblem24() {
        List.of("012", "021", "102", "120", "201", "210")
                // element + index
                .zipWithIndex()
                .forEach(p -> {
                    assertThat(lexicographicPermutationNaive(List.of("1", "0", "2"), p._2 + 1)).isEqualTo(p._1);
                    assertThat(lexicographicPermutation(List.of("1", "0", "2"), p._2 + 1)).isEqualTo(p._1);
                });

        assertThat(lexicographicPermutation(List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9"), 1_000_000)).isEqualTo("2783915460");
    }

    /**
     * Naïve version. Very readable, but not performant enough for calculating
     * "the millionth lexicographic permutation of the digits 0, 1, 2, 3, 4, 5,
     * 6, 7, 8 and 9" (takes about 40 seconds on an average laptop).
     * <p>实现了一个简单的字典序排列算法，用于求解给定字符串列表的第 ordinal 个字典序排列。</p>
     */
    private static String lexicographicPermutationNaive(List<String> stringsToPermutate, int ordinal) {
        return stringsToPermutate
                //  permutations() 方法生成输入字符串列表的所有可能排列
                .permutations()
                // 将每个排列转换为字符串形式，通过 sorted() 方法按字典序排序，最后返回排序后的第 ordinal - 1 个排列作为结果。
                .map(List::mkString)
                .sorted()
                .get(ordinal - 1);
    }

    /**
     * More performant version that uses an algorithm that calculates the number
     * of permutations achievable in each position instead of actually doing the permutations.
     * <p>实现了一个更为高效的字典序排列算法，用于求解给定字符串列表的第 ordinal 个字典序排列。时间复杂度是 O(n^2)</p>
     */
    private static String lexicographicPermutation(List<String> stringsToPermutate, int ordinal) {
        // 使用 Vavr 库中的 Match 方法对输入的字符串列表按长度进行分类，
        return API.Match(stringsToPermutate.sorted()).of(
                // 长度为 1 的列表直接转换为字符串，长度大于 1 的列表则根据字典序排列的规则进行递归处理。
                Case($((List<String> sx) -> sx.length() == 1), Traversable::mkString),
                Case($(), sx -> {
                    // 通过 memoizedFactorial 函数缓存了列表长度的阶乘值，避免了重复计算。
                    final int noOfPossiblePermutationsInTail = memoizedFactorial.apply(sx.length() - 1);
                    // 使用了一些数学技巧，通过计算 `头字符的位置` 和 `剩余排列的序号` 来快速计算出下一个排列的字符串表示。
                    final int headCharPosition = ((ordinal + noOfPossiblePermutationsInTail - 1) / noOfPossiblePermutationsInTail);
                    final int ordinalRest = Integer.max(0, ordinal - ((headCharPosition - 1) * noOfPossiblePermutationsInTail));
                    return List.of(sx.get(headCharPosition - 1)).mkString() + lexicographicPermutation(sx.removeAt(headCharPosition - 1), ordinalRest);
                })
        );
    }

    private static final Function1<Integer, Integer> memoizedFactorial = Function1.of((Integer i) -> factorial(i).intValue()).memoized();
}
