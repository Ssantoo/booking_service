package com.example.booking.support.filter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.filter.Filter;

public class CustomLogbackFilter extends Filter<ILoggingEvent> {
    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (event.getMessage().contains("payment")) {
            return FilterReply.DENY;
        } else {
            return FilterReply.ACCEPT;
        }
    }
}
