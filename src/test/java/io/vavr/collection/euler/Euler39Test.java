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
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import org.junit.Test;

import static io.vavr.control.Option.some;
import static java.lang.Math.floor;
import static java.lang.Math.hypot;
import static org.assertj.core.api.Assertions.assertThat;

public class Euler39Test {

    /**
     * <strong>Problem 39 Integer right triangles</strong>
     * <p>
     * If <i>p</i> is the perimeter of a right angle triangle with integral
     * length sides, {<i>a,b,c</i>}, there are exactly three solutions for
     * <i>p</i> = 120.
     * <p>
     * {20,48,52}, {24,45,51}, {30,40,50}
     * <p>
     * For which value of <i>p</i> ≤ 1000, is the number of solutions maximised?
     * <p>
     * See also <a href="https://projecteuler.net/problem=39">projecteuler.net
     * problem 39</a>.
     */
    @Test
    public void shouldSolveProblem39() {
        assertThat(SOLUTIONS_FOR_PERIMETERS_UP_TO_1000.get(120)).isEqualTo(some(List.of(Tuple.of(20, 48, 52), Tuple.of(24, 45, 51), Tuple.of(30, 40, 50))));

        assertThat(perimeterUpTo1000WithMaximisedNumberOfSolutions()).isEqualTo(840);
    }

    /**
     * 将预先计算的映射SOLUTIONS_FOR_PERIMETERS_UP_TO_1000中的每个条目映射到一个元组，该元组包含周长值和其关联解列表的长度。
     * 然后使用maxBy方法查找第二个元素（即最大数量解元组）最大的元组。
     * 最后，从结果元组中检索并返回周长值。
     */
    private static int perimeterUpTo1000WithMaximisedNumberOfSolutions() {
        return SOLUTIONS_FOR_PERIMETERS_UP_TO_1000
                .map((perimeter, listOfSolutions) -> Tuple.of(perimeter, listOfSolutions.length()))
                .maxBy(Tuple2::_2)
                .get()._1;
    }

    /*Instead of iterating up to 500 for both 'a' and 'b', the code could iterate up to the square root of 1000 minus 'a' and only consider values of 'b' greater than or equal to 'a'.
      This is because the maximum value of 'c' (the hypotenuse) for a given 'a' and 'b' occurs when they are both equal, and in this case, c = sqrt(2) * a.
      Therefore, we only need to consider values of 'a' up to the square root of 1000/2 = 22.4.
      The optimized code would look like this:
      List.rangeClosed(1, 22).flatMap(a -> List.rangeClosed(a, (int) Math.sqrt(1000-a*a)).map(b -> Tuple.of(a, b, hypot(a, b))))...*/

    /**
     * 生成一个包含所有周长不超过1000的勾股三元组的映射。勾股三元组是一组满足a^2 + b^2 = c^2的三个整数(a, b, c)
     */
    private static final Map<Integer, List<Tuple3<Integer, Integer, Integer>>> SOLUTIONS_FOR_PERIMETERS_UP_TO_1000
            = List.rangeClosed(1, 500)
            // 生成所有可能的整数对 (a, b)，其中 a 和 b 是从1到500（包括1和500）的整数
            .flatMap(a -> List.rangeClosed(a, 500)
                    .map(b -> Tuple.of(a, b, hypot(a, b))))
            // 过滤掉所有不是整数的直角三角形的整数对 (a, b, c)
            .filter(t -> floor(t._3) == t._3)
            // 将每个元组的第三个值从 Double 转换为 Integer
            .map(t -> t.map3(Double::intValue))
            // 将这些元组按照它们的(a + b + c)之和分组
            .groupBy(t -> t.apply((a, b, c) -> a + b + c))
            // 过滤掉键（即 a + b + c 的总和）大于1000 的情况
            .filterKeys(d -> d <= 1_000);
}
