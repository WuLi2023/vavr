package io.vavr.collection.euler;

/**
 * @author Muggle
 * @date 2023年04月25日 20:32
 */
public class Euler50Test {

    /**
     * 查找小于 n 的最大质数，并返回该质数的值
     */
    private static int findPrimeBelow(int n) {
        return Utils.primes()
                .takeWhile(i -> i < n)
                .last();
    }

    /**
     * 查找小于 n 的所有质数中，相邻质数之和最长的一组质数，并返回其中的第一个质数。
     */
    private static int findLongestSumOfConsecutivePrimesBelow(int n) {
        /*具体实现方式是使用 Utils.primes() 函数生成无限流获取所有质数，使用 takeWhile 操作符筛选出小于 n 的所有质数。接着使用 map 操作符将每个质数 i 映射为一个长度为 2 的 int 数组，其中第一个元素是 i，第二个元素是使用 findNumberOfConsecutivePrimesBelow 函数计算出以 i 为起点的相邻质数和的长度。然后使用 maxBy 操作符找到这个流中长度最大的一组相邻质数和，并返回其中的第一个质数。如果没有找到任何相邻质数和，返回 -1。*/
        return Utils.primes()
                .takeWhile(i -> i < n)
                .map(i -> new int[]{i, findNumberOfConsecutivePrimesBelow(n, i)})
                .maxBy(a -> a[1]).get()[0];
    }

    /**
     * 查找小于 n 的所有质数中，以 start 为起点的最长相邻质数之和的长度。
     */
    private static int findNumberOfConsecutivePrimesBelow(int n, int start) {
        /*具体实现方式是使用 Utils.primes() 函数生成无限流获取所有质数，使用 takeWhile 操作符筛选出小于 n 的所有质数。接着使用 scanLeft 操作符生成一个新的流，其中每个元素都是前面所有元素之和，第一个元素为 0。这个步骤相当于把所有前缀和计算出来了。最后再使用 takeWhile 操作符过滤出所有小于 n 的前缀和。最后返回 start 在这个流中的索引，即为 start 作为连续质数和的起点时的长度。如果 start 不是任何连续质数和的起点，返回 -1。*/
        return Utils.primes()
                .takeWhile(i -> i < n)
                .scanLeft(0, Integer::sum)
                .takeWhile(i -> i < n)
                .indexOf(start);
    }
}
