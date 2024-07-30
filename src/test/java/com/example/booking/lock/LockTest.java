package com.example.booking.lock;

import com.example.booking.domain.concert.Reservation;
import com.example.booking.domain.concert.ReservationRepository;
import com.example.booking.domain.user.User;
import com.example.booking.domain.user.UserRepository;
import com.example.booking.domain.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class LockTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    @Transactional
    public void 비관적락_동시에_28번_동시에_충전과_2번의_조회_시도() throws InterruptedException {
        // Given
        long userId = 1L;
        User initialUser = userRepository.findById(userId);
        int initialAmount = initialUser.getPoint();
        int chargeAmount = 100;

        int threadCount = 30;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicBoolean[] chargeResults = new AtomicBoolean[28];
        for (int i = 0; i < 28; i++) {
            chargeResults[i] = new AtomicBoolean(true);
        }
        AtomicBoolean[] checkResults = new AtomicBoolean[2];
        for (int i = 0; i < 2; i++) {
            checkResults[i] = new AtomicBoolean(true);
        }

        long[] checkStartTimes = new long[2];
        long[] checkEndTimes = new long[2];

        // 28번 : 포인트 충전 시도
        for (int i = 0; i < 28; i++) {
            int index = i;
            executorService.execute(() -> {
                try {
                    System.out.println((index + 1) + "번 " + ": 포인트 충전");
                    userService.chargePoint(userId, chargeAmount);
                } catch (Exception e) {
                    chargeResults[index].set(false);
                    System.err.println((index + 1) + " 실패: " + e.getMessage());
                }
                latch.countDown();
            });
        }

        // 2번 : 포인트 조회 시도
        for (int i = 0; i < 2; i++) {
            int index = i;
            executorService.execute(() -> {
                try {
                    long startTime = System.currentTimeMillis();
                    System.out.println((29 + index + 1) + "번" + ": 포인트 조회 시도");
                    User user = userService.getUserById(userId);
                    long endTime = System.currentTimeMillis();
                    long waitTime = endTime - startTime;
                    System.out.println("현재포인트: " + user.getPoint());
                    System.out.println("시간체크 : " + waitTime);
                    checkStartTimes[index] = startTime;
                    checkEndTimes[index] = endTime;
                } catch (Exception e) {
                    checkResults[index].set(false);
                    System.err.println((29 + index + 1) + " 실패 : " + e.getMessage());
                }
                latch.countDown();
            });
        }

        latch.await();

        User finalUser = userService.getUserById(userId);
        System.out.println("최종 포인트 " + finalUser.getPoint());

        for (AtomicBoolean result : chargeResults) {
            assertThat(result.get()).isTrue();
        }
        for (AtomicBoolean result : checkResults) {
            assertThat(result.get()).isTrue();
        }

        int expectedFinalAmount = initialAmount + chargeAmount * 28;
        assertThat(finalUser.getPoint()).isEqualTo(expectedFinalAmount);
    }

    @Test
    @Transactional
    public void 비관적락_동시에_28번_동시에_포인트_사용_시도와_2번의_조회_시도() throws InterruptedException {
        // Given
        long userId = 1L;
        User initUser = userRepository.findById(userId);
        int initialPoints = initUser.getPoint();
        int reservationCost = 100;

        User initialUser = userRepository.findById(userId);
        initialUser = initialUser.charge(initialPoints);
        userRepository.save(initialUser);

        long reservationId = 1L;
        Reservation reservation = reservationRepository.findById(reservationId);

        int threadCount = 30;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicBoolean[] useResults = new AtomicBoolean[28];
        for (int i = 0; i < 28; i++) {
            useResults[i] = new AtomicBoolean(true);
        }
        AtomicBoolean[] checkResults = new AtomicBoolean[2];
        for (int i = 0; i < 2; i++) {
            checkResults[i] = new AtomicBoolean(true);
        }

        long[] checkStartTimes = new long[2];
        long[] checkEndTimes = new long[2];

        // 28번 : 포인트 사용 시도
        for (int i = 0; i < 28; i++) {
            int index = i;
            executorService.execute(() -> {
                try {
                    System.out.println((index + 1) + "번 " + ": 포인트 사용");
                    userService.usePoint(userId, reservation);
                } catch (Exception e) {
                    useResults[index].set(false);
                    System.err.println((index + 1) + " 실패: " + e.getMessage());
                }
                latch.countDown();
            });
        }

        // 2번 : 포인트 조회 시도
        for (int i = 0; i < 2; i++) {
            int index = i;
            executorService.execute(() -> {
                try {
                    long startTime = System.currentTimeMillis();
                    System.out.println((29 + index + 1) + "번" + ": 포인트 조회 시도");
                    User user = userService.getUserById(userId);
                    long endTime = System.currentTimeMillis();
                    long waitTime = endTime - startTime;
                    System.out.println("현재포인트: " + user.getPoint());
                    System.out.println("시간체크 : " + waitTime);
                    checkStartTimes[index] = startTime;
                    checkEndTimes[index] = endTime;
                } catch (Exception e) {
                    checkResults[index].set(false);
                    System.err.println((29 + index + 1) + " 실패 : " + e.getMessage());
                }
                latch.countDown();
            });
        }

        latch.await();

        User finalUser = userService.getUserById(userId);
        System.out.println("최종 포인트 " + finalUser.getPoint());

        for (AtomicBoolean result : useResults) {
            assertThat(result.get()).isTrue();
        }
        for (AtomicBoolean result : checkResults) {
            assertThat(result.get()).isTrue();
        }

        int expectedFinalAmount = initialPoints - reservationCost * 28;
        assertThat(finalUser.getPoint()).isEqualTo(expectedFinalAmount);
    }


}
