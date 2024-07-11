package com.example.booking.domain.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class User {
    private final Long id;
    private final String name;
    private final int point;

    public User(Long id, String name, int point) {
        this.id = id;
        this.name = name;
        this.point = point;
    }

    //포인트 충전
    public User charge(int amount) {
        checkNegative(amount);
        return new User(id, name,point+amount);
    }

    //충전 포인트 음수 체크?
    public void checkNegative(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("포인트는 음수가 될 수 없습니다.");
        }
    }

    //사용?
    public User use(int amount) {
        if( point < amount) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }
        checkNegative(amount);
        return new User(id, name, point-amount);
    }


}
