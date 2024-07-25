package com.example.booking.infra.user.entity;


import com.example.booking.domain.concert.Seat;
import com.example.booking.domain.user.User;
import com.example.booking.infra.concert.entity.SeatEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int point;

    @Version
    private int version;

    public User toModel() {
        return User.builder()
                .id(id)
                .name(name)
                .point(point)
                .version(version)
                .build();
    }

    public static UserEntity from(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .name(user.getName())
                .point(user.getPoint())
                .version(user.getVersion())
                .build();
    }
}
