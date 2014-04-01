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
package ch.qos.logback.contrib.mongodb;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.integration.junit4.JMockit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.qos.logback.access.spi.IAccessEvent;
import ch.qos.logback.contrib.mongodb.MongoDBAccessEventAppender;

import com.mongodb.BasicDBObject;

/**
 * Tests for {@link MongoDBAccessEventAppender}.
 * 
 * @author Tomasz Nurkiewicz
 * @author Christian Trutz
 * @since 0.1
 */
@RunWith(JMockit.class)
public class MongoDBAccessEventAppenderTest {

    // to be tested
    private MongoDBAccessEventAppender appender = null;

    @Test
    public void testTimeStamp() {
        // given
        new NonStrictExpectations() {
            {
                event.getTimeStamp();
                result = 1000L;
            }
        };
        // when
        final BasicDBObject dbObject = appender.toMongoDocument(event);
        // then
        assertEquals(new Date(1000L), dbObject.get("timeStamp"));
    }

    @Test
    public void testServerName() {
        // given
        new NonStrictExpectations() {
            {
                event.getServerName();
                result = "servername";
            }
        };
        // when
        final BasicDBObject dbObject = appender.toMongoDocument(event);
        // then
        assertEquals("servername", dbObject.get("serverName"));
    }

    @Test
    public void testRemote() {
        // given
        new NonStrictExpectations() {
            {
                event.getRemoteHost();
                result = "host";
                event.getRemoteUser();
                result = "user";
                event.getRemoteAddr();
                result = "addr";
            }
        };
        // when
        final BasicDBObject dbObject = appender.toMongoDocument(event);
        // then
        BasicDBObject remoteDBObject = (BasicDBObject) dbObject.get("remote");
        assertEquals("host", remoteDBObject.getString("host"));
        assertEquals("user", remoteDBObject.getString("user"));
        assertEquals("addr", remoteDBObject.getString("addr"));
    }

    @Test
    public void testRequest() {
        // given
        new NonStrictExpectations() {
            {
                event.getRequestURI();
                result = "uri";
                event.getProtocol();
                result = "protocol";
                event.getMethod();
                result = "method";
                event.getRequestContent();
                result = "postContent";
                event.getCookie("JSESSIONID");
                result = "sessionId";
                event.getRequestHeader("User-Agent");
                result = "userAgent";
                event.getRequestHeader("Referer");
                result = "referer";
            }
        };
        // when
        final BasicDBObject dbObject = appender.toMongoDocument(event);
        // then
        BasicDBObject requestDBObject = (BasicDBObject) dbObject.get("request");
        assertEquals("uri", requestDBObject.getString("uri"));
        assertEquals("protocol", requestDBObject.getString("protocol"));
        assertEquals("method", requestDBObject.getString("method"));
        assertEquals("postContent", requestDBObject.getString("postContent"));
        assertEquals("sessionId", requestDBObject.getString("sessionId"));
        assertEquals("userAgent", requestDBObject.getString("userAgent"));
        assertEquals("referer", requestDBObject.getString("referer"));
    }

    //
    //
    // MOCKING
    //

    @Mocked
    private IAccessEvent event;

    @Before
    public void before() {
        appender = new MongoDBAccessEventAppender();
    }

}
