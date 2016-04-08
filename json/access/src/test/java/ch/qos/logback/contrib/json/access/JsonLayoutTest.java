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
package ch.qos.logback.contrib.json.access;

import ch.qos.logback.access.TeztConstants;
import ch.qos.logback.access.dummy.DummyAccessEventBuilder;
import ch.qos.logback.access.joran.JoranConfigurator;
import ch.qos.logback.access.spi.AccessContext;
import ch.qos.logback.access.spi.IAccessEvent;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.read.ListAppender;
import org.junit.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static ch.qos.logback.contrib.json.access.JsonLayout.REQUESTTIME_ATTR_NAME;
import static org.junit.Assert.*;

public class JsonLayoutTest {

    private AccessContext context = new AccessContext();

    private void configure(String file) throws JoranException {
        JoranConfigurator jc = new JoranConfigurator();
        jc.setContext(context);
        jc.doConfigure(file);
    }

    @Test
    public void addToJsonMap() throws Exception {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        JsonLayout jsonLayout = new JsonLayout();
        jsonLayout.add("key1", true, "value1", map);
        jsonLayout.add("key2", false, "value2", map);
        jsonLayout.add("key3", true, null, map);

        assertTrue(map.size() == 1);
        assertTrue(map.containsKey("key1"));
        assertEquals(map.get("key1"), "value1");
        assertTrue(!map.containsKey("key2"));
        assertTrue(!map.containsKey("key3"));
    }

    @Test
    public void addIntToJsonMap() throws Exception {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        JsonLayout jsonLayout = new JsonLayout();
        jsonLayout.addInt("key1", true, 1, map);
        jsonLayout.addInt("key2", false, 1, map);
        jsonLayout.addInt("key3", true, -1, map);

        assertTrue(map.size() == 2);
        assertTrue(map.containsKey("key1"));
        assertEquals(map.get("key1"), "1");
        assertTrue(!map.containsKey("key2"));
        assertTrue(map.containsKey("key3"));
        assertEquals(map.get("key3"), "-1");
    }

    @Test
    public void addTimestampToJsonMap() throws Exception {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        JsonLayout jsonLayout = new JsonLayout();
        jsonLayout.setTimestampFormat("yyyy-MM-dd HH:mm:ss.SSS");
        jsonLayout.addTimestamp("key1", true, 1, map);
        jsonLayout.addTimestamp("key2", false, 1, map);
        jsonLayout.addTimestamp("key3", true, -1, map);

        assertTrue(map.size() == 2);
        assertTrue(map.containsKey("key1"));
        assertEquals("1970-01-01 01:00:00.001", map.get("key1"));
        assertTrue(!map.containsKey("key2"));
        assertTrue(map.containsKey("key3"));
        assertEquals("-1", map.get("key3"));
    }

    @Test
    public void addRequestTimeToJsonMap() throws Exception {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        JsonLayout jsonLayout = new JsonLayout();
        jsonLayout.addRequestTime(10001, map);

        assertTrue(map.size() == 1);
        assertTrue(map.containsKey(REQUESTTIME_ATTR_NAME));
        assertEquals("10.001", map.get(REQUESTTIME_ATTR_NAME));

        Map<String, Object> map2 = new LinkedHashMap<String, Object>();
        JsonLayout jsonLayout2 = new JsonLayout();
        jsonLayout2.addRequestTime(-1, map2);

        assertTrue(map2.size() == 0);
    }

    @Test
    public void addMapToJsonMap() throws Exception {
        Map<String, Object> map = new LinkedHashMap<String, Object>();

        Map<String, String> emptyMap = new HashMap<String, String>();
        Map<String, String> mapWithData = new HashMap<String, String>();
        mapWithData.put("mapKey1", "mapValue1");
        Map<String, String[]> mapWithArrayValue = new HashMap<String, String[]>();
        mapWithArrayValue.put("mapKey1", new String[]{"mapValue1","mapValue2","mapValue3"});

        JsonLayout jsonLayout = new JsonLayout();
        jsonLayout.addMap("key1", true, emptyMap, map);
        jsonLayout.addMap("key2", true, mapWithData, map);
        jsonLayout.addMap("key3", true, mapWithArrayValue, map);
        jsonLayout.addMap("key4", false, mapWithArrayValue, map);

        assertTrue(map.size() == 2);
        assertTrue(!map.containsKey("key1"));
        assertEquals(mapWithData, map.get("key2"));
        assertEquals(mapWithArrayValue, map.get("key3"));
        assertTrue(!map.containsKey("key4"));
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