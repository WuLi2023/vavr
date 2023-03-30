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

import static io.vavr.collection.euler.Utils.fibonacci;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * <strong>Problem 25: 1000-digit Fibonacci number</strong>
 * <p>
 * The Fibonacci sequence is defined by the recurrence relation:
 * <p>
 * Fn = Fn−1 + Fn−2, where F1 = 1 and F2 = 1. Hence the first 12 terms will be:
 * <pre>
 * F1 = 1
 * F2 = 1
 * F3 = 2
 * F4 = 3
 * F5 = 5
 * F6 = 8
 * F7 = 13
 * F8 = 21
 * F9 = 34
 * F10 = 55
 * F11 = 89
 * F12 = 144
 * </pre>
 * <p>
 * The 12th term, F12, is the first term to contain three digits.
 * <p>
 * What is the first term in the Fibonacci sequence to contain 1000 digits?
 * <p>
 * See also <a href="https://projecteuler.net/problem=25">projecteuler.net
 * problem 25</a>.
 */
public class Euler25Test {

    @Test
    public void shouldSolveProblem25() {
        assertThat(firstFibonacciTermContaining(3)).isEqualTo(12);
        assertThat(firstFibonacciTermContaining(1000)).isEqualTo(4782);
    }

    /**
     * 实现了一个查找第一个包含指定数位数的斐波那契数列项的方法。
     * <p>时间复杂度是 O(n)，其中 n 是第一个满足条件的斐波那契数列项的索引值。由于斐波那契数列的增长速度非常快，因此在实际使用中可能会出现性能问题。</p>
     */
    private static int firstFibonacciTermContaining(int digits) {
        // 使用 fibonacci() 方法生成一个斐波那契数列的流
        return fibonacci()
                // 使用 zipWithIndex() 方法将该流中的每个元素和它的索引值进行配对，生成一个新的流。
                .zipWithIndex()
                // 使用 find() 方法在该流中查找第一个满足条件的元素，条件为该元素的字符串表示的长度等于指定的 digits 参数。
                .find(t -> t._1.toString().length() == digits)
                // 获取该元素在原始流中的索引值，作为结果返回。
                .get()._2;
    }
}
