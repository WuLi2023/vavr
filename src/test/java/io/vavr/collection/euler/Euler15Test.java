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

import static io.vavr.collection.euler.Utils.factorial;
import static org.assertj.core.api.Assertions.assertThat;

public class Euler15Test {

    /**
     * <strong>Problem 15: Lattice paths</strong>
     * <p>
     * Starting in the top left corner of a 2x2 grid, and only being able to move to
     * the right and down,
     * there are exactly 6 routes to the bottom right corner.
     * <p>
     * How many such routes are there through a 20×20 grid?
     * <p>
     * See also <a href="https://projecteuler.net/problem=15">projecteuler.net
     * problem 15</a>.
     */
    @Test
    public void shouldSolveProblem15() {
        // (2n)! / n!*(2n-n)!
        assertThat(solve(2)).isEqualTo(6);
        assertThat(solve(20)).isEqualTo(137_846_528_820L);
    }

    /**
     * 总的可选路径共有 C(2n,n)
     * C(2n,n) 表示从 2n 个元素中选取 n 个元素的组合数，也可以表示将 2n 个元素分成大小相等的两个集合的方案数。
     */
    private static long solve(int n) {
        // n! -> 调用 factorial(n) 方法计算 n! 的值，将结果保存在变量 f 中
        final BigInteger f = factorial(n);
        return
        // 计算 (2n)! 的值
        factorial(2 * n)
                // 将计算出来的阶乘值除以 n! -> 将 (2n)! 除以 f，得到 C(2n, n) 的值
                .divide(f).divide(f)
                .longValue();
    }
}
