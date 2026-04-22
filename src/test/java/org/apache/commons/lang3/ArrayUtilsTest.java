package org.apache.commons.lang3;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ArrayUtils 单元测试")
public class ArrayUtilsTest {

    @BeforeAll
    static void setUpAll() {
        System.out.println("开始执行 ArrayUtils 测试套件");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("ArrayUtils 测试套件执行完毕");
    }

    // ========== isEmpty 方法测试 ==========

    @Test
    @DisplayName("测试 isEmpty - 边界值测试")
    void testIsEmptyBoundary() {
        // 这些应该通过
        assertTrue(ArrayUtils.isEmpty((Object[]) null), "null应该返回true");
        assertTrue(ArrayUtils.isEmpty(new String[]{}), "空数组应该返回true");
        assertFalse(ArrayUtils.isEmpty(new String[]{"A", "B"}), "长度>=2的数组应该返回false");

        // 这个断言会失败，因为注入的缺陷让长度为1的数组返回true
        assertFalse(ArrayUtils.isEmpty(new String[]{"A"}), "长度为1的数组应该返回false（缺陷）");
    }

    @ParameterizedTest
    @DisplayName("测试 isEmpty - 参数化测试")
    @ValueSource(strings = {"X", "Y", "Z"})
    void testIsEmptyParameterized(String element) {
        // 这个断言会失败，暴露缺陷
        assertFalse(ArrayUtils.isEmpty(new String[]{element}),
                "长度为1的数组[" + element + "]不应该被认为是空的");
    }

    // ========== isNotEmpty 方法测试（依赖isEmpty） ==========

    @Test
    @DisplayName("测试 isNotEmpty")
    void testIsNotEmpty() {
        assertFalse(ArrayUtils.isNotEmpty((Object[]) null), "null应该返回false");
        assertFalse(ArrayUtils.isNotEmpty(new String[]{}), "空数组应该返回false");

        // 由于依赖isEmpty，这个断言也会失败
        assertTrue(ArrayUtils.isNotEmpty(new String[]{"A"}), "长度为1的数组应该返回true");

        assertTrue(ArrayUtils.isNotEmpty(new String[]{"A", "B"}), "长度>=2的数组应该返回true");
    }

    // ========== contains 方法测试（正常方法，无缺陷） ==========

    @Test
    @DisplayName("测试 contains - 正常情况")
    void testContainsNormal() {
        String[] fruits = {"apple", "banana", "orange"};

        assertTrue(ArrayUtils.contains(fruits, "banana"), "应该包含banana");
        assertFalse(ArrayUtils.contains(fruits, "grape"), "不应该包含grape");
    }

    @Test
    @DisplayName("测试 contains - null安全")
    void testContainsNullSafe() {
        assertFalse(ArrayUtils.contains(null, "anything"), "null数组应该返回false");
        assertFalse(ArrayUtils.contains(new String[]{}, "anything"), "空数组应该返回false");
    }

    // ========== add 方法测试（正常方法） ==========

    @Test
    @DisplayName("测试 add - 向数组添加元素")
    void testAdd() {
        String[] original = {"A", "B"};
        String[] result = ArrayUtils.add(original, "C");

        assertEquals(3, result.length, "添加后长度应为3");
        assertEquals("C", result[2], "新元素应在最后");
        assertNotSame(original, result, "应返回新数组，不修改原数组");
    }

    @Test
    @DisplayName("测试 add - null数组")
    void testAddToNull() {
        String[] result = ArrayUtils.add(null, "A");

        assertEquals(1, result.length, "null数组添加后应返回长度为1的数组");
        assertEquals("A", result[0]);
    }

    // ========== reverse 方法测试 ==========

    @Test
    @DisplayName("测试 reverse - 数组反转")
    void testReverse() {
        String[] input = {"A", "B", "C", "D"};
        ArrayUtils.reverse(input);

        assertArrayEquals(new String[]{"D", "C", "B", "A"}, input, "数组应被反转");
    }

    @Test
    @DisplayName("测试 reverse - 空数组和null")
    void testReverseEdgeCases() {
        String[] empty = {};
        ArrayUtils.reverse(empty);
        assertArrayEquals(new String[]{}, empty, "空数组反转后仍为空");

        assertDoesNotThrow(() -> ArrayUtils.reverse((Object[]) null), "null数组不应抛异常");
    }

    // ========== subarray 方法测试 ==========

    @Test
    @DisplayName("测试 subarray - 获取子数组")
    void testSubarray() {
        String[] input = {"A", "B", "C", "D", "E"};
        String[] result = ArrayUtils.subarray(input, 1, 4);

        assertArrayEquals(new String[]{"B", "C", "D"}, result);
    }
}
