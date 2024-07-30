package com.example.booking.integration;

import com.example.booking.domain.user.User;
import com.example.booking.domain.user.UserRepository;
import com.example.booking.domain.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest
@SqlGroup({
        @Sql(value = "/sql/user-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/reservation-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class TxTest {

    private static final Logger log = LoggerFactory.getLogger(TxTest.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void 비관적락_포인트충전() throws InterruptedException {
        long userId = 1;
        int chargeAmount = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        CountDownLatch latch = new CountDownLatch(3);

        // tx1: 포인트 충전
        executorService.execute(() -> {
            try {
                long tx1StartTime = System.currentTimeMillis();
                System.out.println("시작 tx1 : " + tx1StartTime);

                userService.chargePoint(userId, chargeAmount);

                long tx1EndTime = System.currentTimeMillis();
                System.out.println("tx1 took " + (tx1EndTime - tx1StartTime) + " milliseconds.");
            } finally {
                latch.countDown();
            }
        });

        // tx2: 포인트 충전
        executorService.execute(() -> {
            try {
                Thread.sleep(50);
                long tx2StartTime = System.currentTimeMillis();
                System.out.println("시작 tx2 : " + tx2StartTime);

                userService.chargePoint(userId, chargeAmount);

                long tx2EndTime = System.currentTimeMillis();
                System.out.println("tx2 걸린시간" + (tx2EndTime - tx2StartTime) + " milliseconds.");

                Thread.sleep(5000); // 지연 추가
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        });

        // tx3: 포인트 조회
        executorService.execute(() -> {
            try {
                Thread.sleep(300);
                long tx3StartTime = System.currentTimeMillis();
                System.out.println("시작 tx3 : " + tx3StartTime);

                User getUser = userService.getUserById(userId);
                System.out.println("유저포인트: " + getUser.getPoint());

                long tx3EndTime = System.currentTimeMillis();
                System.out.println("tx3 걸린시간 " + (tx3EndTime - tx3StartTime) + " milliseconds.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        });

        latch.await();
        executorService.shutdown();
    }

    @Test
    public void 낙관적락_포인트충전() throws InterruptedException {
        int numThreads = 100;
        long userId = 1L;
        int chargeAmount = 100;

        CountDownLatch latch = new CountDownLatch(numThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numThreads; i++) {
            executorService.submit(() -> {
                try {
                    userService.chargePoint(userId, chargeAmount);
                    log.info("[Thread ID: {}] 성공적으로 충전 완료", Thread.currentThread().getId());
                } catch (ObjectOptimisticLockingFailureException e) {
                    log.error("[Thread ID: {}] ObjectOptimisticLockingFailureException :: {}", Thread.currentThread().getId(), e.getMessage());
                } catch (Exception ex) {
                    log.error("[Thread ID: {}] Exception :: {}", Thread.currentThread().getId(), ex.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        long endTime = System.currentTimeMillis();
        log.info("소요 시간: {} ms", (endTime - startTime));
    }


}
