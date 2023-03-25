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
import io.vavr.collection.Stream;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Euler14Test {

    /**
     * <strong>Problem 14: Longest Collatz sequence</strong>
     * <p>
     * The following iterative sequence is defined for the set of positive integers:
     * <pre>
     * <code>
     * n → n/2 (n is even)
     * n → 3n + 1 (n is odd)
     * </code>
     * </pre>
     * Using the rule above and starting with 13, we generate the following sequence:
     * <pre>
     * <code>
     * 13 → 40 → 20 → 10 → 5 → 16 → 8 → 4 → 2 → 1
     * </code>
     * </pre>
     * It can be seen that this sequence (starting at 13 and finishing at 1) contains 10 terms.
     * Although it has not been proved yet (Collatz Problem), it is thought that all starting numbers finish at 1.
     * <p>
     * Which starting number, under one million, produces the longest chain?
     * <p>
     * NOTE: Once the chain starts the terms are allowed to go above one million.
     * <p>
     * See also <a href="https://projecteuler.net/problem=14">projecteuler.net problem 14</a>.
     */
    @Test
    public void shouldSolveProblem14() {
        // equivalent to from(1L).take(1_000_000)
        Assertions.assertThat(Stream
                // 从 500,000 开始生成一个无限流
                .from(500_000L)
                // 取出前 500,000 个元素
                .take(500_000)
                // 对这些元素使用 collatzSequenceLength 函数计算 Collatz sequence 的长度，并找到其中的最大值
                .maxBy(collatzSequenceLength)
                .get()
                // 确保计算 Collatz sequence 的长度的代码能够正确地找到从 1 到 1,000,000 中任意一个数的最长 Collatz sequence 的长度，并返回对应的数值。
                ).isEqualTo(837799);
    }

    /** 
     *    该函数可以用作Collatz猜想的计算机实现中的一部分，用于计算给定起始数的步数。由于Collatz猜想的性质尚未被证明，因此这个函数在某些情况下可能会进入无限循环
     *    定义了一个静态的、不可变的Function1类型的变量collatzRecursive，它接受一个long类型的参数并返回一个long类型的结果。
     *    该函数实现了Collatz猜想中的递归规则，它计算了从给定的起始数开始，经过一系列转换后得到1所需要的步数。 
     */
    private final static Function1<Long, Long> collatzRecursive = n -> {
        if (n == 1) {
            // 检查起始数n是否等于1，如果是，则返回1。
            return 1L;
        } else {
            if (n % 2 == 0) {
                // 如果n是偶数，则递归地调用它自己，以n/2作为参数，并将返回值加1。
                return Euler14Test.collatzRecursive.apply(n / 2) + 1;
            } else {
                // 如果n是奇数，则递归地调用它自己，以3n+1作为参数，并将返回值加1。
                return Euler14Test.collatzRecursive.apply(3 * n + 1) + 1;
            }
        }
    };

    /* 
        collatzRecursive.memoized() 返回一个 memoized（记忆化）的函数，
        即在函数第一次被调用时，会将输入和输出的键值对缓存起来，下次调用时如果输入相同，直接返回缓存的输出结果，从而提高函数的运行效率。
        将 memoized 的 collatzRecursive 函数赋值给 collatzSequenceLength 变量。
        因为在计算 Collatz sequence 的长度时，需要多次调用 collatzRecursive 函数，而使用 memoized 函数可以避免重复计算，提高计算效率。
        因为 collatzRecursive 是一个 Function1 接口类型的对象，所以可以使用 memoized() 方法将其转换为 memoized 函数。
        在这里，使用 private final static 关键字修饰变量 collatzSequenceLength，使其成为一个静态常量，在整个类中都可以被访问和使用，不需要重复创建多个 memoized 函数对象。
     */
    private final static Function1<Long, Long> collatzSequenceLength = collatzRecursive.memoized();
}
