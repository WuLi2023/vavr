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

import org.junit.Test;

import java.math.BigInteger;

import static io.vavr.collection.euler.Utils.file;
import static io.vavr.collection.euler.Utils.readLines;
import static org.assertj.core.api.Assertions.assertThat;

public class Euler13Test {

    /**
     * <strong>Problem 13: Large sum</strong>
     * <p>
     * Work out the first ten digits of the sum of the following one-hundred 50-digit numbers.
     * <p>
     * See also <a href="https://projecteuler.net/problem=13">projecteuler.net problem 13</a>.
     */
    @Test
    public void shouldSolveProblem13() {
        assertThat(solve()).isEqualTo("5537376230");
    }

    /** 计算文件中所有数字的总和，并返回前10个数字。它使用BigInteger类来处理大数字，以避免精度丢失。 */
    private static String solve() {
        // 将文件中的每行数字读入到一个Stream中。
        return readLines(file("p013_numbers.txt"))
                // 使用map方法将每个字符串转换为一个BigInteger对象
                .map(BigInteger::new)
                // 使用fold方法将这些BigInteger对象加起来，得到一个总和。
                .fold(BigInteger.ZERO, BigInteger::add)
                // 将这个总和转换为一个字符串，并返回前10个数字作为结果。
                .toString().substring(0, 10);
    }
}
