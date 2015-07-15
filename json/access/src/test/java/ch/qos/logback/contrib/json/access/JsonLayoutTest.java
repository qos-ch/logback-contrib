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

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

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

        assertTrue(log.contains(String.format("%s=%s", JsonLayout.REMOTEHOST_ATTR_NAME, event.getRemoteHost())));
        assertTrue(log.contains(String.format("%s=%s", JsonLayout.REMOTEUSER_ATTR_NAME, event.getRemoteUser())));
        assertTrue(log.contains(String.format("%s=%s", JsonLayout.REMOTEADDR_ATTR_NAME, event.getRemoteAddr())));
        assertTrue(log.contains(String.format("%s=%s", JsonLayout.METHOD_ATTR_NAME, event.getMethod())));
        if(event.getRequestHeaderMap().size() == 2){
            Iterator<Map.Entry<String, String>> iterator = event.getRequestHeaderMap().entrySet().iterator();
            Map.Entry<String, String> firstInMap = iterator.next();
            Map.Entry<String, String> secondInMap = iterator.next();
            assertTrue(log.contains(String.format("%s={%s=%s, %s=%s}", JsonLayout.REQUESTHEADER_ATTR_NAME, firstInMap.getKey(), firstInMap.getValue(), secondInMap.getKey(), secondInMap.getValue())));
        }
        assertTrue(log.contains(String.format("%s=%s", JsonLayout.PROTOCOL_ATTR_NAME, event.getProtocol())));
        assertTrue(log.contains(String.format("%s=%s", JsonLayout.SERVERNAME_ATTR_NAME, event.getServerName())));
        assertTrue(log.contains(String.format("%s=%s", JsonLayout.STATUSCODE_ATTR_NAME, event.getStatusCode())));
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

        assertTrue(log.contains(String.format("%s=%s", JsonLayout.CONTENTLENGTH_ATTR_NAME, event.getContentLength())));
        assertTrue(log.contains(String.format("%s=%s", JsonLayout.REQUESTURL_ATTR_NAME, event.getMethod())));
        assertTrue(log.contains(String.format("%s=%s", JsonLayout.LOCALPORT_ATTR_NAME, event.getLocalPort())));
        assertTrue(log.contains(String.format("%s=%s", JsonLayout.RESPONSECONTENT_ATTR_NAME, event.getResponseContent())));
        assertTrue(log.contains(String.format("%s=%s", JsonLayout.REQUESTCONTENT_ATTR_NAME, event.getRequestContent())));
    }
}