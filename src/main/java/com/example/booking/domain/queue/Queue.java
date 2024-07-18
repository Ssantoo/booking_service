package com.example.booking.domain.queue;


public class Queue {

    private static final int MAX_ACTIVE_USERS = 100;

    //현재 활성 상태의 토큰 수를 확인하여, 최대 허용 가능한 활성 토큰 수보다 적은지 여부를 반환
    public static boolean hasAvailableSlot(int activeUserCount) {
        return activeUserCount < MAX_ACTIVE_USERS;
    }

    //현재 활성 상태의 토큰 수를 기반으로 활성화할 수 있는 여유 자리 수를 계산하여 반환
    public static int countAvailableSlot(int activeUserCount) {
        return MAX_ACTIVE_USERS - activeUserCount;
    }

}
