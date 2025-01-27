package com.example.booking.redis;

import com.example.booking.domain.concert.*;
import com.example.booking.infra.concert.entity.ConcertEntity;
import com.example.booking.infra.concert.entity.ScheduleEntity;
import com.example.booking.support.config.RedisConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RedisTest {

    private static final Logger logger = LoggerFactory.getLogger(ConcertServiceTest.class);

    @Autowired
    private ConcertService concertService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;


    @BeforeEach
    public void setUp() {
        redisTemplate.delete("concerts::concertList");
        redisTemplate.delete("concertDates::concertDateList");

        concertRepository.deleteAll();
        scheduleRepository.deleteAll();

        ConcertEntity concertEntity = concertRepository.save(
                new ConcertEntity(null, "Concert 1", "Information 1")
        );
        for (int i = 1; i <= 100000; i++) {
            ScheduleEntity scheduleEntity = new ScheduleEntity(
                    null,
                    LocalDateTime.now().plusDays(i),
                    1000,
                    1000,
                    concertEntity
            );
            scheduleRepository.save(scheduleEntity);
        }
    }

    //콘서트 리스트 비교
    @Test
    public void h2_db_속도_테스트() {
        long startTime = System.currentTimeMillis();
        List<Concert> concerts = concertService.getConcertList();
        long endTime = System.currentTimeMillis();
        logger.info("걸린 시간: {} ms", endTime - startTime);

        List<Concert> cachedList = (List<Concert>) redisTemplate.opsForValue().get("concerts::concertList");
        assertNotNull(cachedList);
        assertEquals(100000, cachedList.size());
    }
    @Test
    public void redis_캐시_속도_테스트() {
        concertService.getConcertList();

        long startTime = System.currentTimeMillis();
        List<Concert> concerts = concertService.getConcertList();
        long endTime = System.currentTimeMillis();
        logger.info("걸린 시간: {} ms", endTime - startTime);

        assertNotNull(concerts);
        assertEquals(100000, concerts.size());
    }

    //콘서트날짜 비교
    @Test
    public void h2_db_콘서트_날짜_속도_테스트() {
        long startTime = System.currentTimeMillis();
        List<Schedule> schedules = concertService.getDates(1L);
        long endTime = System.currentTimeMillis();
        logger.info("걸린 시간: {} ms", endTime - startTime);
        assertNotNull(schedules);
        assertEquals(100000, schedules.size());
    }

    @Test
    public void redis_캐시_콘서트_날짜_속도_테스트() {
        concertService.getDates(1L);
        long startTime = System.currentTimeMillis();
        List<Schedule> schedules = concertService.getDates(1L);
        long endTime = System.currentTimeMillis();
        logger.info("걸린 시간: {} ms", endTime - startTime);
        assertNotNull(schedules);
        assertEquals(100000, schedules.size());
    }



}
