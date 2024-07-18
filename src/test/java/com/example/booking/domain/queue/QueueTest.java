package com.example.booking.domain.queue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QueueTest {

    @Test
    public void 활성_사용자수가_최대_허용_수보다_적으면_true() {
        int activeUserCount = 50; // 50명의 활성 사용자가 있을 때
        assertTrue(Queue.hasAvailableSlot(activeUserCount));
    }

    @Test
    public void 활성_사용자수가_최대_허용_수와_같으면_false() {
        int activeUserCount = 100; // 100명의 활성 사용자가 있을 때
        assertFalse(Queue.hasAvailableSlot(activeUserCount));
    }

    @Test
    public void 활성_사용자수가_최대_허용_수를_넘으면_false() {
        int activeUserCount = 110; // 110명의 활성 사용자가 있을 때
        assertFalse(Queue.hasAvailableSlot(activeUserCount));
    }

    @Test
    public void 활성_사용자수가_최대_허용_수보다_적으면_남은_자리_수_반환() {
        int activeUserCount = 50; // 50명의 활성 사용자가 있을 때
        int  expectedValue= 100 - activeUserCount; // 기대값: 50
        assertEquals(expectedValue, Queue.countAvailableSlot(activeUserCount));
    }

    @Test
    public void 활성_사용자수가_최대_허용_수와_같으면_남은_자리_수는_0() {
        int activeUserCount = 100; // 100명의 활성 사용자가 있을 때
        int expectedValue = 0; // 기대값: 0
        assertEquals(expectedValue, Queue.countAvailableSlot(activeUserCount));
    }


}
