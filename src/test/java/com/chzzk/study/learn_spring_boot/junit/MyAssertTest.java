package com.chzzk.study.learn_spring_boot.junit;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MyAssertTest {

    List<String> todos = Arrays.asList("AWS", "Azure", "DevOps");

    @Test
    void test() {
        boolean test = todos.contains("AWS"); // Result
        boolean test2 = todos.contains("GCP");

        assertEquals(true, test);
        assertTrue(test);
        assertFalse(test2);
        // assertNull, assertNotNull
        // assertArrayEquals({예상 결과 배열}, {메서드에서 나온 실제 배열})
        assertArrayEquals(new int[] {1, 2}, new int[] {2, 1});

        assertEquals(3, todos.size());
    }
}
