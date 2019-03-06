/**
 * Copyright (C) 2016, The logback-contrib developers. All rights reserved.
 *
 * This program and the accompanying materials are dual-licensed under
 * either the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation
 *
 *   or (per the licensee's choosing)
 *
 * under the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation.
 */
package ch.qos.logback.contrib.json.classic;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.core.joran.spi.JoranException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;
import static org.slf4j.Logger.ROOT_LOGGER_NAME;

/**
 * @author <a href="mailto:matejonnet@gmail.com">Matej Lazar</a>
 */
public class CustomKeysJsonLayoutTest {
    private LoggerContext context = new LoggerContext();

    @Test
    public void shouldProduceJsonWithCustomKey() throws IOException, JoranException {
        ContextUtil.reconfigure(context, "src/test/input/json/customKeysJsonLayout.xml");
        String message = "Firestarter.";
        Logger logger = context.getLogger(ROOT_LOGGER_NAME);
        ILoggingEvent logEvent = new LoggingEvent("my.class.name", logger, Level.INFO, message, null, null);

        ConsoleAppender<ILoggingEvent> appender = (ConsoleAppender<ILoggingEvent>) logger.getAppender("STR_LIST");
        LayoutWrappingEncoder<ILoggingEvent> encoder = (LayoutWrappingEncoder<ILoggingEvent>) appender.getEncoder();
        Layout<ILoggingEvent> customKeysJsonLayout = encoder.getLayout();

        String log = customKeysJsonLayout.doLayout(logEvent);

        assertThat(log, containsString(String.format("%s=%s", "myLoggerName", logger.getName())));
        assertThat(log, containsString(String.format("%s=%s", JsonLayout.LEVEL_ATTR_NAME, Level.INFO)));
        assertThat(log, containsString(String.format("%s=%s", JsonLayout.FORMATTED_MESSAGE_ATTR_NAME, message)));
    }
}