package io.vavr.collection.euler;

import io.vavr.collection.Stream;

/**
 * @author Muggle
 * @date 2023年04月24日 21:37
 */
public class Euler46Test {

    /*奇合数是指一个大于 1 的奇数，它可以被表示为两个素数之和的形式，其中素数是指只能被 1 和它本身整除的正整数。*/
    /*如果一个数是质数，那么它本身就可以被表示为一个质数和一个平方数的形式（即本身是一个素数，平方数为 0），所以我们只需要考虑不能被表示为质数和平方数之和的数值。因此，我们需要从奇合数中排除质数，以便找到满足条件的数值。*/
    private static int findSmallestOddCompositeThatCannotBeWrittenAsSumOfPrimeAndTwiceASquare() {
        return Stream.from(35, 2)
                // 使用 filter 方法对奇合数列表进行过滤，只保留其中不是质数的数值
                .filter(n -> !Utils.isPrime(n))
                // 使用 find 方法查找第一个不能被写成一个质数和一个平方数的两倍之和的奇合数，如果找不到，置 -1。
                .find(n -> !canBeWrittenAsSumOfPrimeAndTwiceASquare(n))
                .getOrElse(-1);
    }

    /**
     * 判断一个数能否表示为一个素数和两倍完全平方数的和
     *
     * @param n 待判断的整数
     * @return 若该数能够被表示为一个素数和两倍完全平方数的和，返回true；否则返回false。
     */
    private static boolean canBeWrittenAsSumOfPrimeAndTwiceASquare(int n) {
        // 创建从1到sqrt(n/2)的整数序列，并对其中每个元素执行以下操作：
        // 计算n-2*i*i的结果，其中i为当前元素。然后将这些结果作为Stream流并行处理，查找第1个结果是素数的元素。
        // 这个计算出来的结果流要么为空（find方法返回Option.None），表示没有满足条件的解；
        // 要么包含一个值（find方法返回Option.Some），表示找到了一个符合要求的解。
        return Stream.rangeClosed(1, (int) Math.sqrt((double) n / 2))
                .map(i -> n - 2 * i * i)
                .find(Utils::isPrime)
                .isDefined();
    }
}
