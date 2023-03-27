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

import io.vavr.Function3;
import io.vavr.collection.Vector;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Euler18Test {

    /**
     * <strong>Problem 18: Maximum path sum I</strong>
     * <p>
     * By starting at the top of the triangle below and moving to adjacent numbers on the row below, the maximum total from top to bottom is 23.
     * <pre>
     *        3
     *       7 4
     *      2 4 6
     *     8 5 9 3
     * </pre>
     * That is, 3 + 7 + 4 + 9 = 23.
     * <p>
     * Find the maximum total from top to bottom in p018_triangle.txt.
     * <p>
     * <strong>NOTE</strong>: As there are only 16384 routes, it is possible to solve this problem by trying every route.
     * However, Problem 67, is the same challenge with a triangle containing one-hundred rows;
     * it cannot be solved by brute force, and requires a clever method! ;o).
     * <p>
     * See also <a href="https://projecteuler.net/problem=18">projecteuler.net problem 18</a>.
     */
    @Test
    public void shouldSolveProblem18() {
        assertThat(solve("small_triangle.txt")).isEqualTo(23);
        assertThat(solve("p018_triangle.txt")).isEqualTo(1074);
    }

    private static int solve(String fileName) {
        return naive.apply(Euler67Test.loadTriangle(fileName), 0, 0);
    }

    /**
     * 计算给定二维向量中从(row,col)位置开始到底部的最大路径和，并返回该路径和的值
     * 实现了一个朴素的递归算法，可能会在计算较大的输入时导致性能问题。在实际应用中，可能需要使用更高效的算法来解决这个问题。
     */
    private final static Function3<Vector<Vector<Integer>>, Integer, Integer, Integer> naive = Function3.of(
            /* 一个整数类型的二维向量tr，以及两个整数类型的参数row和col。 */
            (Vector<Vector<Integer>> tr, Integer row, Integer col) -> {
                // 首先获取二维向量中(row,col) 位置的值并将其赋给变量value。
                int value = tr.get(row).get(col);
                // 然后，如果当前位置是向量的最后一行，则返回当前位置的值。
                if (row == tr.length() - 1) {
                    return tr.get(row).get(col);
                } else {
                    // 否则，函数对象递归地调用自身，并将下一行的相邻两个位置的较大值与当前位置的值相加，返回结果作为当前位置的最大路径和。
                    return value + Math.max(Euler18Test.naive.apply(tr, row + 1, col), Euler18Test.naive.apply(tr, row + 1, col + 1));
                }
            }
    );
}
