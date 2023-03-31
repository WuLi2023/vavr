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
import io.vavr.collection.Stream;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <strong>Problem 28: Number spiral diagonals</strong>
 * <p>
 * Starting with the number 1 and moving to the right in a clockwise direction a
 * 5 by 5 spiral is formed as follows:
 * <pre>
 *             21 22 23 24 25
 *             20  7  8  9 10
 *             19  6  1  2 11
 *             18  5  4  3 12
 *             17 16 15 14 13
 * </pre>
 * <p>
 * It can be verified that the sum of the numbers on the diagonals is 101.
 * <p>
 * What is the sum of the numbers on the diagonals in a 1001 by 1001 spiral
 * formed in the same way?
 * <p>
 * See also
 * <a href="https://projecteuler.net/problem=28">projecteuler.net problem 28
 * </a>.
 */
public class Euler28Test {

    @Test
    public void shouldSolveProblem28() {
        assertThat(sumOfDiagonalInSpiralWithSide(5)).isEqualTo(101);
        assertThat(sumOfDiagonalInSpiralWithSide(1001)).isEqualTo(669_171_001);
    }

    /**
     * 返回以螺旋形排列的正方形的对角线数字的和。
     */
    private static long sumOfDiagonalInSpiralWithSide(long maxSideLength) {
        // 获取以螺旋形排列的正方形的对角线数字
        return diagonalNumbersInSpiralWithSide(maxSideLength)
                // 使用Stream.sum方法计算对角线数字的和。
                .sum().longValue();
    }

    /**
     * 返回一个流，该流包含正方形的对角线数字（以螺旋形排列的正方形的对角线数字）。
     */
    private static Stream<Long> diagonalNumbersInSpiralWithSide(long maxSideLength) {
        /*使用Stream.iterate方法生成一个无限流，
          该流从一个元组(1, center())开始，
          每次调用nextSideLength和nextRoundOfCorners方法来计算下一个元组，
          其中第一个元素是当前正方形的边长，
          第二个元素是当前正方形的对角线数字。*/
        return Stream.iterate(Tuple.of(1, center()), t -> Tuple.of(nextSideLength(t._1), nextRoundOfCorners(t._2.last(), nextSideLength(t._1))))
                // 使用Stream.takeWhile方法取前面的元组，直到当前正方形的边长大于maxSideLength。
                .takeWhile(t -> t._1 <= maxSideLength)
                // 使用Stream.flatMap方法将每个元组的第二个元素（即当前正方形的对角线数字）作为一个流返回。
                .flatMap(t -> t._2);
    }

    /**
     * 返回一个流，该流包含正方形的中心。
     */
    private static Stream<Long> center() {
        return Stream.of(1L);
    }

    /**
     * 如果当前正方形的边长为n，则该方法将返回n+2，即下一个正方形的边长。
     */
    private static int nextSideLength(int currentSideLength) {
        return currentSideLength + 2;
    }

    /**
     * 计算了当前正方形的四个角落的下一个正方形的角落的值，并将它们作为一个流返回。
     */
    private static Stream<Long> nextRoundOfCorners(long previousCorner, int currentSideLength) {
        // 使用Stream.iterate方法生成一个无限流，该流从previousCorner开始，每次增加currentSideLength-1。
        return Stream.iterate(previousCorner, n -> n + currentSideLength - 1)
                // 从第二个元素开始，取前4个元素。使用Stream.drop方法跳过第一个元素（即previousCorner）
                .drop(1)
                // 使用Stream.take方法取前4个元素，即表示下一轮四个角落的值。
                .take(4);
    }
}
