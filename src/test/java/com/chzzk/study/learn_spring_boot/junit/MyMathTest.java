package com.chzzk.study.learn_spring_boot.junit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MyMathTest {

    private MyMath math = new MyMath();

    @Test
    void CalculateSum_ThreeMemberArray() {
        assertEquals(6, math.calculateSum(new int[]{1, 2, 3}));
    }

    @Test
    void CalculateSum_ZeroMemberArray() {
        assertEquals(0, math.calculateSum(new int[]{}));
    }
}
