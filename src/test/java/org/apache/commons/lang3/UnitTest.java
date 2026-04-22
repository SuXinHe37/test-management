import org.apache.commons.lang3.ArrayUtils;

public class UnitTest {
    private static int totalTests = 0;
    private static int passedTests = 0;
    private static int failedTests = 0;

    public static void main(String[] args) {
        System.out.println("========== 手动测试 ArrayUtils ==========\n");

        testIsEmpty();
        testIsNotEmpty();
        testContains();

        printSummary();
    }

    /**
     * 测试 isEmpty 方法（注入缺陷的方法）
     */
    private static void testIsEmpty() {
        System.out.println("--- 测试 ArrayUtils.isEmpty ---");

        // 测试用例1: null
        boolean r1 = ArrayUtils.isEmpty((boolean[]) null);
        assertTest("isEmpty(null)", true, r1);

        // 测试用例2: 空数组
        String[] emptyArr = new String[]{};
        boolean r2 = ArrayUtils.isEmpty(emptyArr);
        assertTest("isEmpty(空数组)", true, r2);

        // 测试用例3: 长度为1的数组（会触发缺陷）
        String[] singleArr = new String[]{"A"};
        boolean r3 = ArrayUtils.isEmpty(singleArr);
        assertTest("isEmpty(长度为1的数组)", false, r3);  // 期望false，实际会返回true

        // 测试用例4: 长度为2的数组
        String[] twoArr = new String[]{"A", "B"};
        boolean r4 = ArrayUtils.isEmpty(twoArr);
        assertTest("isEmpty(长度为2的数组)", false, r4);

        System.out.println();
    }

    /**
     * 测试 isNotEmpty 方法（依赖isEmpty）
     */
    private static void testIsNotEmpty() {
        System.out.println("--- 测试 ArrayUtils.isNotEmpty ---");

        boolean r1 = ArrayUtils.isNotEmpty((boolean[]) null);
        assertTest("isNotEmpty(null)", false, r1);

        boolean r2 = ArrayUtils.isNotEmpty(new String[]{});
        assertTest("isNotEmpty(空数组)", false, r2);

        // 由于依赖isEmpty，这个测试也会失败
        boolean r3 = ArrayUtils.isNotEmpty(new String[]{"A"});
        assertTest("isNotEmpty(长度为1的数组)", true, r3);

        boolean r4 = ArrayUtils.isNotEmpty(new String[]{"A", "B"});
        assertTest("isNotEmpty(长度为2的数组)", true, r4);

        System.out.println();
    }

    /**
     * 测试 contains 方法（正常方法，用于对比）
     */
    private static void testContains() {
        System.out.println("--- 测试 ArrayUtils.contains ---");

        String[] arr = {"apple", "banana", "orange"};

        boolean r1 = ArrayUtils.contains(arr, "banana");
        assertTest("contains(存在)", true, r1);

        boolean r2 = ArrayUtils.contains(arr, "grape");
        assertTest("contains(不存在)", false, r2);

        boolean r3 = ArrayUtils.contains(null, "apple");
        assertTest("contains(null)", false, r3);

        System.out.println();
    }



    /**
     * 断言工具方法
     */
    private static void assertTest(String testName, Object expected, Object actual) {
        totalTests++;
        boolean isEqual;

        if (expected == actual) {
            isEqual = true;
        } else if (expected != null && expected.equals(actual)) {
            isEqual = true;
        } else {
            isEqual = false;
        }

        if (isEqual) {
            System.out.println("✓ " + testName + " 通过");
            passedTests++;
        } else {
            System.err.println("✗ " + testName + " 失败");
            System.err.println("  期望: " + expected);
            System.err.println("  实际: " + actual);
            failedTests++;
        }
    }

    /**
     * 打印测试总结
     */
    private static void printSummary() {
        System.out.println("\n========== 测试总结 ==========");
        System.out.println("总测试数: " + totalTests);
        System.out.println("通过: " + passedTests);
        System.out.println("失败: " + failedTests);
        System.out.println("通过率: " + (passedTests * 100.0 / totalTests) + "%");

        if (failedTests > 0) {
            System.err.println("\n⚠️ 发现缺陷：长度为1的数组被错误地认为为空！");
        }
    }
}
