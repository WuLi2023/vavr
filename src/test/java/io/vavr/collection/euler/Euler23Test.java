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
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import static io.vavr.API.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * <strong>Problem 23: Non-abundant sums</strong>
 * <p>
 * A perfect number is a number for which the sum of its proper divisors is
 * exactly equal to the number. For example, the sum of the proper divisors of
 * 28 would be 1 + 2 + 4 + 7 + 14 = 28, which means that 28 is a perfect number.
 * <p>
 * A number n is called deficient if the sum of its proper divisors is less than
 * n and it is called abundant if this sum exceeds n.
 * <p>
 * As 12 is the smallest abundant number, 1 + 2 + 3 + 4 + 6 = 16, the smallest
 * number that can be written as the sum of two abundant numbers is 24. By
 * mathematical analysis, it can be shown that all integers greater than 28123
 * can be written as the sum of two abundant numbers. However, this upper limit
 * cannot be reduced any further by analysis even though it is known that the
 * greatest number that cannot be expressed as the sum of two abundant numbers
 * is less than this limit.
 * <p>
 * Find the sum of all the positive integers which cannot be written as the sum
 * of two abundant numbers.
 * <p>
 * See also <a href="https://projecteuler.net/problem=23">projecteuler.net
 * problem 23</a>.
 */
public class Euler23Test {

    /**
     * 最小的过剩数，其值为 12
     */
    private static final long SMALLEST_ABUNDANT_NUMBER = 12;
    /**
     * 最小的可以被写成两个过剩数之和的数，其值为 24。
     */
    private static final long SMALLEST_NUMBER_WRITTEN_AS_THE_SUM_OF_TO_ABUNDANT_NUMBERS = 2 * SMALLEST_ABUNDANT_NUMBER;
    /**
     * 根据数学分析得出的，所有可以被写成两个过剩数之和的数的下限，其值为 28123。
     */
    private static final long LOWER_LIMIT_FOUND_BY_MATHEMATICAL_ANALYSIS_FOR_NUMBERS_THAT_CAN_BE_WRITTEN_AS_THE_SUM_OF_TO_ABUNDANT_NUMBERS = 28123;

    @Test
    public void shouldSolveProblem23() {
        List.range(1, SMALLEST_ABUNDANT_NUMBER).forEach(n -> Assertions.assertThat(isAbundant.apply(n)).isFalse());
        Assertions.assertThat(isAbundant.apply(SMALLEST_ABUNDANT_NUMBER)).isTrue();
        Assertions.assertThat(isAbundant.apply(28L)).isFalse();
        assertThat(canBeWrittenAsTheSumOfTwoAbundantNumbers(SMALLEST_NUMBER_WRITTEN_AS_THE_SUM_OF_TO_ABUNDANT_NUMBERS - 1)).isFalse();
        assertThat(canBeWrittenAsTheSumOfTwoAbundantNumbers(SMALLEST_NUMBER_WRITTEN_AS_THE_SUM_OF_TO_ABUNDANT_NUMBERS)).isTrue();
        assertThat(canBeWrittenAsTheSumOfTwoAbundantNumbers(LOWER_LIMIT_FOUND_BY_MATHEMATICAL_ANALYSIS_FOR_NUMBERS_THAT_CAN_BE_WRITTEN_AS_THE_SUM_OF_TO_ABUNDANT_NUMBERS + 1)).isTrue();
        assertThat(sumOfAllPositiveIntegersThatCannotBeWrittenAsTheSumOfTwoAbundantNumbers()).isEqualTo(4179871L);
    }

    /**
     * 计算所有不能被两个过剩数之和表示的正整数的总和
     */
    private static long sumOfAllPositiveIntegersThatCannotBeWrittenAsTheSumOfTwoAbundantNumbers() {
        return Stream.rangeClosed(1, LOWER_LIMIT_FOUND_BY_MATHEMATICAL_ANALYSIS_FOR_NUMBERS_THAT_CAN_BE_WRITTEN_AS_THE_SUM_OF_TO_ABUNDANT_NUMBERS)
                // 筛选出不能被两个过剩数之和表示的数字，并使用 sum() 方法计算这些数字的总和
                .filter(l -> !canBeWrittenAsTheSumOfTwoAbundantNumbers(l))
                .sum().longValue();
    }

    /**
     * 接受一个长整型数作为参数，判断该数是否可以被两个过剩数之和表示。
     */
    private static boolean canBeWrittenAsTheSumOfTwoAbundantNumbers(long l) {
        // 使用 Match.of() 方法对输入的数进行模式匹配。
        return Match(l).of(
                // 小于最小的可以被写成两个过剩数之和的数（即 24），则直接返回 false。如果该数等于最小的这样的数，即 24，则返回 true。
                Case($(n -> n < SMALLEST_NUMBER_WRITTEN_AS_THE_SUM_OF_TO_ABUNDANT_NUMBERS), false),
                Case($(SMALLEST_NUMBER_WRITTEN_AS_THE_SUM_OF_TO_ABUNDANT_NUMBERS), true),
                // 生成从 SMALLEST_ABUNDANT_NUMBER 到 l/2 的所有数字的 Stream，然后使用 exists() 方法查找两个过剩数之和等于该数的情况，具体做法是对于每个数字 a，判断 isAbundant.apply(a) 和 isAbundant.apply(l - a) 是否都为 true，如果是，则返回 true，否则继续查找。如果没有找到这样的情况，则返回 false。
                Case($(), () -> Stream.rangeClosed(SMALLEST_ABUNDANT_NUMBER, l / 2).exists(a -> isAbundant.apply(a) && isAbundant.apply(l - a)))
        );
    }

    /**
     * 定义了一个名为 isAbundant 的 Function1<Long, Boolean> 常量，用于判断一个长整型数是否为过剩数（abundant number）。
     */
    private static final Function1<Long, Boolean> isAbundant = Function1.of((Long l) -> Utils.divisors(l)
                    // 获取该数的所有因子，并使用 sum() 方法计算这些因子的总和。如果总和大于该数本身，说明该数为过剩数，返回 true，否则返回 false。
                    .sum().longValue() > l)
            // 使用 memoized() 方法进行了优化，以避免重复计算。
            .memoized();
}
