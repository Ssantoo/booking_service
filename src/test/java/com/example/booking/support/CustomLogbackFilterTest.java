//package com.example.booking.support;
//
//import ch.qos.logback.classic.spi.ILoggingEvent;
//import ch.qos.logback.core.read.ListAppender;
//import com.example.booking.support.filter.CustomLogbackFilter;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class CustomLogbackFilterTest {
//
//    private Logger logger;
//    private ListAppender<ILoggingEvent> listAppender;
//
//    @BeforeEach
//    public void setUp() {
//        logger = LoggerFactory.getLogger(CustomLogbackFilter.class);
//        listAppender = new ListAppender<>();
//        listAppender.setContext((ch.qos.logback.classic.LoggerContext) LoggerFactory.getILoggerFactory());
//
//        CustomLogbackFilter filter = new CustomLogbackFilter();
//        listAppender.addFilter(filter);
//
//        listAppender.start();
//        ((ch.qos.logback.classic.Logger) logger).addAppender(listAppender);
//    }
//
//    @Test
//    public void 커스텀_필터_테스트() {
//        logger.info("payment 로그");
//        logger.info("일반적 로그");
//
//        assertEquals(1, listAppender.list.size());
//        assertEquals("일반적 로그", listAppender.list.get(0).getMessage());
//    }
//
//
//}
