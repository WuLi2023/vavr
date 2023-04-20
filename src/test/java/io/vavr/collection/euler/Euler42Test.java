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
import io.vavr.collection.CharSeq;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import static io.vavr.collection.euler.Utils.file;
import static io.vavr.collection.euler.Utils.readLines;
import static org.assertj.core.api.Assertions.assertThat;

public class Euler42Test {

    /**
     * <strong>Problem 42 Coded triangle numbers</strong>
     * <p>
     * The <i>n</i><sup>th</sup> term of the sequence of triangle numbers is
     * given by, <i>t</i><sub>n</sub> = ½<i>n</i>(<i>n</i>+1); so the first ten
     * triangle numbers are:
     * <pre>
     * 1, 3, 6, 10, 15, 21, 28, 36, 45, 55, ...
     * </pre>
     * <p>
     * By converting each letter in a word to a number corresponding to its
     * alphabetical position and adding these values we form a word value. For
     * example, the word value for SKY is 19 + 11 + 25 = 55 = t<sub>10</sub>. If
     * the word value is a triangle number then we shall call the word a
     * triangle word.
     * <p>
     * Using p042_words.txt, a 16K text file containing nearly two-thousand
     * common English words, how many are triangle words?
     * <p>
     * See also <a href="https://projecteuler.net/problem=42">projecteuler.net
     * problem 42</a>.
     */
    @Test
    public void shouldSolveProblem42() {
        assertThat(isTriangleWord("SKY")).isTrue();
        assertThat(sumOfAlphabeticalPositions("SKY")).isEqualTo(55);
        assertThat(alphabeticalPosition('S')).isEqualTo(19);
        assertThat(alphabeticalPosition('K')).isEqualTo(11);
        assertThat(alphabeticalPosition('Y')).isEqualTo(25);
        assertThat(TRIANGLE_NUMBERS.take(10)).containsExactly(1, 3, 6, 10, 15, 21, 28, 36, 45, 55);
        List.rangeClosed(1, 60).forEach(n -> Assertions.assertThat(isTriangleNumberMemoized.apply(n)).isEqualTo(List.of(1, 3, 6, 10, 15, 21, 28, 36, 45, 55).contains(n)));

        assertThat(numberOfTriangleNumbersInFile()).isEqualTo(162);
    }

    /*如果一个单词的单词值是一个三角数，我们就称这个单词是三角单词。*/

    /*  这段代码是一个Java方法，它的作用是读取一个文件中的单词，并计算其中三角形数单词的数量。具体实现步骤如下：

        1. 使用file()方法获取指定文件的路径。
        2. 使用readLines()方法读取该文件中所有行，并返回一个Stream<String>类型的流。
        3. 对每一行进行操作，使用replaceAll()方法将双引号替换为空字符串。
        4. 将每一行以逗号为分隔符拆分成多个单词，并返回一个包含这些单词的List<String>类型列表。
        5. 过滤出所有是三角形数单词（即字母值等于某个三角形数）的单词，使用filter()方法和isTriangleWord静态函数实现过滤功能。
        6. 返回最终结果，即符合条件的三角形数单词数量。

        需要注意：本段代码依赖于Euler42Test类中定义了isTriangleWord静态函数来判断是否为三角形数。 */
    private static int numberOfTriangleNumbersInFile() {
        return readLines(file("p042_words.txt"))
                .map(l -> l.replaceAll("\"", ""))
                .flatMap(l -> List.of(l.split(",")))
                .filter(Euler42Test::isTriangleWord)
                .length();
    }

    private static boolean isTriangleWord(String word) {
        return isTriangleNumber(sumOfAlphabeticalPositions(word));
    }

    /*  函数名：isTriangleNumber
        输入参数：
        n：一个整数，表示要检查它是否为三角形数的数字。
        输出：
        布尔值；如果输入数字是三角形数，则为true，否则为false。
        描述：
        该函数接受一个整数值作为输入，并检查它是否是三角形数。
        三角形数是一种可以用点的三角网格表示的数字，在这个网格中第一行包含一个点，每个后续行都比前面多一个点。
        例如，1、3、6、10、15是前五个三角形数。
        该函数使用称为TRIANGLE_NUMBERS的懒惰序列来生成三角形数字。
        在此序列上调用takeWhile方法会返回一个新序列，其中包括原始序列中满足lambda表达式指定条件的元素。
        在本例中，lambda表达式检查当前三角形数字是否小于或等于输入数字。
        exists方法返回true如果结果序列中有任何元素满足lambda表达式指定条件。
        在本例中，lambda表达式检查当前三角形数字是否等于输入数字。
        如果存在这样的三角形数量，则函数返回true，表示输入数量是一个三角性数量。
        否则，它将返回false. */
    private static boolean isTriangleNumber(int n) {
        return TRIANGLE_NUMBERS
                .takeWhile(t -> t <= n)
                .exists(t -> t == n);
    }

    /*  这段代码定义了一个私有的静态常量 `isTriangleNumberMemoized`，它是一个函数类型为 `Function1<Integer, Boolean>` 的对象。
        这个函数接受一个整数作为参数，并返回一个布尔值。
        该函数使用了 memoization 技术，即缓存计算结果以避免重复计算。
        具体来说，它调用了名为 `isTriangleNumber` 的方法，并将其作为参数传递给 `Function1.of()` 方法进行包装和转换成函数类型。
        然后再调用 `.memoized()` 方法对其进行 memoization 处理。
        在此之后，可以通过调用 `isTriangleNumberMemoized.apply(n)` 来获取 n 是否是三角形数的布尔值结果（其中 n 是整数）。 */
    private static final Function1<Integer, Boolean> isTriangleNumberMemoized = Function1.of(Euler42Test::isTriangleNumber).memoized();

    /*  这段代码定义了一个私有的静态常量 `TRIANGLE_NUMBERS`，它是一个整数流（Stream<Integer>）。
        这个流包含从1开始的所有三角形数。
        三角形数是连续正整数的和，从1开始。用于计算第n个三角形数字的公式是n(n+1)/2。
        在初始化时，使用 `Stream.from(1)` 创建了一个无限流，并通过 `.map(n -> 0.5 * n * (n + 1))` 将每个元素转换为对应的三角形数公式计算结果。
        最后，`.map(Double::intValue)` 将每个浮点型结果转换为整型并返回该值作为新的整数流中的元素。 */
    private static final Stream<Integer> TRIANGLE_NUMBERS = Stream.from(1).map(n -> 0.5 * n * (n + 1)).map(Double::intValue);

    /*  这段代码定义了一个名为"sumOfAlphabeticalPositions"的静态方法，它接受一个名为"word"的字符串参数。
        该方法通过使用CharSeq对象的map函数将每个字符应用于“alphabeticalPosition”方法来计算给定字符串中每个字符的字母位置之和。
        计算出的总和以整数值返回。  */
    private static int sumOfAlphabeticalPositions(String word) {
        return CharSeq.of(word)
                .map(Euler42Test::alphabeticalPosition)
                .sum().intValue();
    }

    /*  这段Java代码定义了一个名为“alphabeticalPosition”的私有静态方法，该方法接受一个char类型的参数。
        该方法返回一个整数值，表示输入字符在英文字母表中的字母位置。
        实现使用ASCII码来计算位置。它从输入字符的ASCII值中减去'A'的ASCII值，并加1。
        这将导致字符在字母表中的位置，其中'A'位于第1个位置，'B'位于第2个位置等等。
        需要注意的是，此实现假定输入字符是大写字母。如果输入字符不是大写字母，则结果可能不可预测或不正确。 */
    private static int alphabeticalPosition(char c) {
        return c - 'A' + 1;
    }
}
