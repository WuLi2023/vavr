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
import io.vavr.collection.List;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

import static io.vavr.API.For;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * <strong>Problem 19:Counting Sundays</strong>
 *
 * <p>You are given the following information, but you may prefer to do some research for yourself.</p>
 * <ul>
 * <li>1 Jan 1900 was a Monday.</li>
 * <li>Thirty days has September,<br />
 * April, June and November.<br />
 * All the rest have thirty-one,<br />
 * Saving February alone,<br />
 * Which has twenty-eight, rain or shine.<br />
 * And on leap years, twenty-nine.</li>
 * <li>A leap year occurs on any year evenly divisible by 4, but not on a century unless it is divisible by 400.</li>
 * </ul>
 * <p>How many Sundays fell on the first of the month during the twentieth century (1 Jan 1901 to 31 Dec 2000)?</p>
 * <p>
 * See also <a href="https://projecteuler.net/problem=19">projecteuler.net problem 19</a>.
 */
public class Euler19Test {

    @Test
    public void shouldSolveProblem19() {
        assertThat(findNumberOfFirstMonthDaysOnSunday(1901, 2000)).isEqualTo(171);
    }

    /* 方法接受两个参数：年份year和月份month，并返回一个布尔值表示该月的第一天是否为星期日。 */
    private static boolean isFirstDayOfMonthSunday(int year, Month month) {
        return LocalDate
                // LocalDate类创建一个日期对象，该日期对象的年份和月份由传入的year和month参数确定，日期为该月的第一天。
                .of(year, month, 1)
                /*然后调用getDayOfWeek()方法获取该日期的星期几，返回值是一个枚举类型DayOfWeek表示星期几。
                最后比较星期几是否为DayOfWeek.SUNDAY，如果是则返回true，否则返回false。*/
                .getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    /*方法接受两个参数：起始年份startYear和结束年份endYear，并返回一个整数表示在该时间段内，第一个月的第一天是星期日的次数。*/
    private static int findNumberOfFirstMonthDaysOnSunday(int startYear, int endYear) {
        return For(
                // 使用List.rangeClosed(startYear, endYear)创建一个包含起始年份和结束年份之间所有年份的列表，
                List.rangeClosed(startYear, endYear),
                // 使用List.of(Month.values())创建一个包含所有月份的列表，并使用For方法将这两个列表进行笛卡尔积，生成一个包含所有年份和月份的元组列表。
                List.of(Month.values()))
                // 使用yield方法将每个元组转换为一个新的元组，其中第一个元素是年份，第二个元素是月份。
                .yield(Tuple::of)
                // 使用filter方法过滤出第一个月的第一天是星期日的元组
                .filter(t -> isFirstDayOfMonthSunday(t._1, t._2))
                // 使用length方法计算满足条件的元组数量，最终返回该数量作为结果。
                .length();
    }

}
