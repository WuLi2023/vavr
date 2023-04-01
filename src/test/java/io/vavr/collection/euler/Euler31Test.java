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

import io.vavr.collection.List;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <strong>Problem 31: Coin sums</strong>
 * <p>In England the currency is made up of pound, £, and pence, p, and there are eight coins in general circulation:</p>
 * <blockquote>1p, 2p, 5p, 10p, 20p, 50p, £1 (100p) and £2 (200p).</blockquote>
 * <p>It is possible to make £2 in the following way:</p>
 * <blockquote>1×£1 + 1×50p + 2×20p + 1×5p + 1×2p + 3×1p</blockquote>
 * <p>How many different ways can £2 be made using any number of coins?</p>
 * See also <a href="https://projecteuler.net/problem=31">projecteuler.net problem 31</a>.
 */
public class Euler31Test {

    @Test
    public void shouldSolveProblem31() {
        final List<Integer> coins = List.of(1, 2, 5, 10, 20, 50, 100, 200);
        assertThat(coinSums(200, coins)).isEqualTo(73682);
    }

    /**
     * 递归地计算可以组成n元的硬币组合的数量。
     * 计算使用给定的硬币组成面值为n的钱数的不同方式的数量，并将其作为一个int类型的值返回。
     *
     * @param n     要组成的金额
     * @param coins 可用的硬币列表，该列表表示不同硬币的面值。
     * @return 可以组成n元的硬币组合的数量
     */
    private static int coinSums(int n, List<Integer> coins) {
        // 首先检查n是否为0，如果是，则返回1，表示使用0个硬币组成0元的方式只有一种：即不使用任何硬币。
        return (n == 0) ? 1 :
                // 如果n小于0，或者硬币列表为空，则返回0，表示没有任何硬币可以组成n元。
                (n < 0 || coins.isEmpty()) ? 0 :
                        // 将递归地调用自身两次，一次是不使用coins列表的第一个硬币，另一次是使用coins列表的第一个硬币，然后将这两个结果相加。
                        coinSums(n, coins.tail()) + coinSums(n - coins.head(), coins);
    }

}
