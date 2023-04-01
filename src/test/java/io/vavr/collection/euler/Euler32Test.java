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
import io.vavr.Tuple3;
import io.vavr.collection.List;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Euler32Test {

    /**
     * <strong>Problem 23 Pandigital products</strong>
     * <p>
     * We shall say that an n-digit number is pandigital if it makes use of all
     * the digits 1 to n exactly once; for example, the 5-digit number, 15234,
     * is 1 through 5 pandigital.
     * <p>
     * The product 7254 is unusual, as the identity, 39 × 186 = 7254, containing
     * multiplicand, multiplier, and product is 1 through 9 pandigital.
     * <p>
     * Find the sum of all products whose multiplicand/multiplier/product
     * identity can be written as a 1 through 9 pandigital.
     * <p>
     * HINT: Some products can be obtained in more than one way so be sure to
     * only include it once in your sum.
     * <p>
     * See also <a href="https://projecteuler.net/problem=32">projecteuler.net
     * problem 32</a>.
     */
    @Test
    public void shouldSolveProblem32() {
        assertThat(isPandigital(1, 5, "15234")).isTrue();
        assertThat(isPandigital(1, 9, "39" + "186" + "7254")).isTrue();
        assertThat(isPandigital(1, 5, "55555")).isFalse();
        assertThat(isPandigital(1, 5, "12340")).isFalse();
        assertThat(sumOfAllProductsPandigital1Through9()).isEqualTo(45228);
    }

    /**
     * 判断一个字符串是否为一个范围内的数字的全排列（即1到N的一个排列）。
     * from和to分别表示数字范围的开始和结束
     * num为传入的待检查的字符串
     */
    private static boolean isPandigital(int from, int to, String num) {
        // 1. 判断数字字符序列的长度是否等于from到to的长度
        return num.length() == to - from + 1 &&
                // 2. 判断数字字符序列是否包含from到to的所有数字
                List.rangeClosed(from, to).forAll(i -> num.contains(Integer.toString(i)));
        /*生成从from到to范围内所有数字的不可变列表。
          forAll()方法接受一个函数，并对列表中的每个元素都应用这个函数，只有当所有元素都返回true时它才返回true。*/
    }

    /**
     * 表示数字字符序列"123456789"。
     * CharSeq是Vavr库中的一个不可变字符序列（字符串）类型，可以像普通Java字符串一样处理。
     * 它支持各种字符串操作，例如子串、拼接、替换等。
     */
    private static final CharSeq DIGITS_1_9 = CharSeq.of("123456789");

    private static long sumOfAllProductsPandigital1Through9() {
        // 表示待计算数字的位数。在这个问题中，要求乘积的两个数字分别有一个和两个数字。
        return List.of(1, 2)
                /*使用crossProduct()方法来计算数字的排列组合。
                  对于每个i（1或2），使用DIGITS_1_9.crossProduct(i)得到数字序列的排列组合，
                  然后使用.flatMap()方法展平结果，并使用Tuple.of()方法将排列组合的两个数字转换为元组。*/
                .flatMap(i -> DIGITS_1_9.crossProduct(i)// 生成位数为i的数字（从1到9）的所有排列组合，每个组合都是一个CharSeq类型的列表
                        /*对于crossProduct返回的每个组合，使用flatMap()方法将其展平，并生成位数为5-i的数字的所有排列组合。
                          multiplicand表示即将满足条件的待检查数字中已知的一个因数，该因数循环变量来自于之前生成的数字排列组合。
                          使用 DIGITS_1_9.removeAll(multiplicand) 方法，从数字 1 到 9 中移除 multiplicand 中出现的数字，保证在后续过程中不能再被使用，得到一个新的 CharSeq 对象。
                          接着，使用 crossProduct(5 - i) 方法，生成所有可能的 (5 - i) 位被乘数，每个被乘数是一个 CharSeq 对象，存放在一个 List 中。*/
                        .flatMap(multiplicand -> DIGITS_1_9.removeAll(multiplicand).crossProduct(5 - i)
                                // 对每个被乘数执行一个函数，返回一个 Tuple 对象。这个函数的参数是 multiplier，表示一个被乘数。将每个被乘数与因数组合成一个元组
                                .map(multiplier -> Tuple.of(multiplicand.mkString(), multiplier.mkString()))
                        )
                )
                .map(t -> Tuple.of(t._1, t._2, Long.parseLong(t._1) * Long.valueOf(t._2)))
                // 筛选满足某些条件的元组。使用isPandigital()方法来判断元组中的数字是否满足乘积的全排列条件。如果满足条件，则将元组保留下来。
                .filter(t -> isPandigital(1, 9, t._1 + t._2 + Long.toString(t._3)))
                // 将元组中的数字转换为乘积
                .map(Tuple3::_3)
                // 确保唯一性
                .distinct()
                .sum().longValue();
    }

}
