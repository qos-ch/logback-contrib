/**
 * Copyright (C) 2014, The logback-contrib developers. All rights reserved.
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
package ch.qos.logback.contrib.json.access;

import ch.qos.logback.access.TeztConstants;
import ch.qos.logback.access.dummy.DummyAccessEventBuilder;
import ch.qos.logback.access.joran.JoranConfigurator;
import ch.qos.logback.access.spi.AccessContext;
import ch.qos.logback.access.spi.IAccessEvent;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.read.ListAppender;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonLayoutTest {

    AccessContext context = new AccessContext();

    void configure(String file) throws JoranException {
        JoranConfigurator jc = new JoranConfigurator();
        jc.setContext(context);
        jc.doConfigure(file);
    }

    @Test
    public void jsonLayout() throws Exception {
        configure(TeztConstants.TEST_DIR_PREFIX + "input/json/jsonLayout.xml");
        ListAppender<IAccessEvent> listAppender = (ListAppender<IAccessEvent>) context.getAppender("STR_LIST");
        IAccessEvent event = DummyAccessEventBuilder.buildNewAccessEvent();
        listAppender.doAppend(event);

        assertEquals(1, listAppender.list.size());
        IAccessEvent iAccessEvent = listAppender.list.get(0);

        JsonLayout jsonLayout = new JsonLayout();
        jsonLayout.setContext(context);
        String log = jsonLayout.doLayout(iAccessEvent);

        assertTrue(log.contains("remoteHost=testHost"));
        assertTrue(log.contains("remoteUser=testUser"));
        assertTrue(log.contains("remoteAddress=testRemoteAddress"));
        assertTrue(log.contains("method=testMethod"));
        assertTrue(log.contains("headers={headerName1=headerValue1, headerName2=headerValue2}"));
        assertTrue(log.contains("protocol=testProtocol"));
        assertTrue(log.contains("serverName=testServerName"));
        assertTrue(log.contains("status=200"));
    }

    @Test
    public void jsonLayoutIncludeDefaultOff() throws Exception {
        configure(TeztConstants.TEST_DIR_PREFIX + "input/json/jsonLayout.xml");
        ListAppender<IAccessEvent> listAppender = (ListAppender<IAccessEvent>) context.getAppender("STR_LIST");
        IAccessEvent event = DummyAccessEventBuilder.buildNewAccessEvent();
        listAppender.doAppend(event);

        assertEquals(1, listAppender.list.size());
        IAccessEvent iAccessEvent = listAppender.list.get(0);

        JsonLayout jsonLayout = new JsonLayout();
        jsonLayout.setContext(context);
        jsonLayout.includeRequestURL = true;
        jsonLayout.includeContentLength = true;
        jsonLayout.includeLocalPort = true;
        jsonLayout.includeRequestContent = true;
        jsonLayout.includeResponseContent = true;
        String log = jsonLayout.doLayout(iAccessEvent);

        assertTrue(log.contains("remoteHost=testHost"));
        assertTrue(log.contains("remoteUser=testUser"));
        assertTrue(log.contains("remoteAddress=testRemoteAddress"));
        assertTrue(log.contains("method=testMethod"));
        assertTrue(log.contains("headers={headerName1=headerValue1, headerName2=headerValue2}"));
        assertTrue(log.contains("protocol=testProtocol"));
        assertTrue(log.contains("contentLength=1000"));
        assertTrue(log.contains("url=testMethod null testProtocol"));
        assertTrue(log.contains("serverName=testServerName"));
        assertTrue(log.contains("status=200"));
        assertTrue(log.contains("port=11"));
        assertTrue(log.contains("requestContent=request contents"));
        assertTrue(log.contains("responseContent=response contents"));
    }
}