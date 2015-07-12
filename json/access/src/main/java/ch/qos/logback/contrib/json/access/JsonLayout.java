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

import ch.qos.logback.access.spi.IAccessEvent;
import ch.qos.logback.contrib.json.JsonLayoutBase;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * A JsonLayout builds its {@link #toJsonMap(ch.qos.logback.access.spi.IAccessEvent) jsonMap} from a
 * source {@link ch.qos.logback.access.spi.IAccessEvent IAccessEvent} with the following keys/value pairs:
 * <p/>
 * <table>
 *     <tr>
 *         <th nowrap="nowrap">Key</th>
 *         <th nowrap="nowrap">Value</th>
 *         <th nowrap="nowrap">Notes</th>
 *         <th nowrap="nowrap">Enabled by default?</th>
 *     </tr>
 *     <tr>
 *         <td nowrap="nowrap">{@code timestamp}</td>
 *         <td nowrap="nowrap">String value of <code>IAccessEvent.{@link ch.qos.logback.access.spi.IAccessEvent#getTimeStamp() getTimeStamp()}</code></td>
 *         <td>By default, the value is not formatted; it is simply {@code String.valueOf(timestamp)}.  To format
 *         the string using a SimpleDateFormat, set the {@link #setTimestampFormat(String) timestampFormat}
 *         property with the corresponding SimpleDateFormat string, for example, {@code yyyy-MM-dd HH:mm:ss.SSS}</td>
 *         <td>true</td>
 *     </tr>
 *     <tr>
 *         <td nowrap="nowrap">{@code remoteAddress}</td>
 *         <td nowrap="nowrap"><code>IAccessEvent.{@link ch.qos.logback.access.spi.IAccessEvent#getRemoteAddr() getRemoteAddr()}</code></td>
 *         <td>Internet Protocol (IP) address of the client or last proxy that sent the request.</td>
 *         <td>true</td>
 *     </tr>
 *     <tr>
 *         <td nowrap="nowrap">{@code remoteUser}</td>
 *         <td nowrap="nowrap"><code>IAccessEvent.{@link ch.qos.logback.access.spi.IAccessEvent#getRemoteUser() getRemoteUser()}</code></td>
 *         <td></td>
 *         <td>true</td>
 *     </tr>
 *     <tr>
 *         <td nowrap="nowrap">{@code requestTime}</td>
 *         <td nowrap="nowrap">String value of <code>IAccessEvent.{@link ch.qos.logback.access.spi.IAccessEvent#getElapsedTime() getElapsedTime()}</code></td>
 *         <td>The time elapsed between receiving the request and logging it. By default, the value is formatted as {@code s.SSS}</td>
 *         <td>true</td>
 *     </tr>
 *     <tr>
 *         <td nowrap="nowrap">{@code uri}</td>
 *         <td nowrap="nowrap"><code>IAccessEvent.{@link ch.qos.logback.access.spi.IAccessEvent#getRequestURI() getRequestURI()}</code></td>
 *         <td></td>
 *         <td>true</td>
 *     </tr>
 *     <tr>
 *         <td nowrap="nowrap">{@code status}</td>
 *         <td nowrap="nowrap">String value of <code>IAccessEvent.{@link ch.qos.logback.access.spi.IAccessEvent#getStatusCode getStatusCode()}</code></td>
 *         <td></td>
 *         <td>true</td>
 *     </tr>
 *     <tr>
 *         <td nowrap="nowrap">{@code method}</td>
 *         <td nowrap="nowrap"><code>IAccessEvent.{@link ch.qos.logback.access.spi.IAccessEvent#getMethod() getMethod()}</code></td>
 *         <td></td>
 *         <td>true</td>
 *     </tr>
 *     <tr>
 *         <td nowrap="nowrap">{@code protocol}</td>
 *         <td nowrap="nowrap"><code>IAccessEvent.{@link ch.qos.logback.access.spi.IAccessEvent#getProtocol() getProtocol()}</code></td>
 *         <td></td>
 *         <td>true</td>
 *     </tr>
 *     <tr>
 *         <td nowrap="nowrap">{@code contentLength}</td>
 *         <td nowrap="nowrap">String value of <code>IAccessEvent.{@link ch.qos.logback.access.spi.IAccessEvent#getContentLength() getContentLength()}</code></td>
 *         <td></td>
 *         <td>true</td>
 *     </tr>
 *     <tr>
 *         <td nowrap="nowrap">{@code url}</td>
 *         <td nowrap="nowrap"><code>IAccessEvent.{@link ch.qos.logback.access.spi.IAccessEvent#getRequestURL() getRequestURL()}</code></td>
 *         <td></td>
 *         <td>false</td>
 *     </tr>
 *     <tr>
 *         <td nowrap="nowrap">{@code remoteHost}</td>
 *         <td nowrap="nowrap"><code>IAccessEvent.{@link ch.qos.logback.access.spi.IAccessEvent#getRemoteHost() getRemoteHost()}</code></td>
 *         <td>Fully qualified name of the client or the last proxy that sent the request.</td>
 *         <td>true</td>
 *     </tr>
 *     <tr>
 *         <td nowrap="nowrap">{@code serverName}</td>
 *         <td nowrap="nowrap"><code>IAccessEvent.{@link ch.qos.logback.access.spi.IAccessEvent#getServerName() getServerName()}</code></td>
 *         <td>Name of the server to which the request was sent.</td>
 *         <td>true</td>
 *     </tr>
 *     <tr>
 *         <td nowrap="nowrap">{@code headers}</td>
 *         <td nowrap="nowrap"><code>IAccessEvent.{@link ch.qos.logback.access.spi.IAccessEvent#getRequestHeaderMap() getRequestHeaderMap()}</code></td>
 *         <td>This value is a {@code Map&lt;String,String&gt;}.</td>
 *         <td>true</td>
 *     </tr>
 *     <tr>
 *         <td nowrap="nowrap">{@code params}</td>
 *         <td nowrap="nowrap"><code>IAccessEvent.{@link ch.qos.logback.access.spi.IAccessEvent#getRequestParameterMap() getRequestParameterMap()}</code></td>
 *         <td>This value is a {@code Map&lt;String,String&gt;}.</td>
 *         <td>true</td>
 *     </tr>
 *     <tr>
 *         <td nowrap="nowrap">{@code port}</td>
 *         <td nowrap="nowrap">String value of <code>IAccessEvent.{@link ch.qos.logback.access.spi.IAccessEvent#getLocalPort() getLocalPort()}</code></td>
 *         <td></td>
 *         <td>false</td>
 *     </tr>
 *     <tr>
 *         <td nowrap="nowrap">{@code requestContent}</td>
 *         <td nowrap="nowrap"><code>IAccessEvent.{@link ch.qos.logback.access.spi.IAccessEvent#getRequestContent() getRequestContent()}</code></td>
 *         <td></td>
 *         <td>false</td>
 *     </tr>
 *     <tr>
 *         <td nowrap="nowrap">{@code responseContent}</td>
 *         <td nowrap="nowrap"><code>IAccessEvent.{@link ch.qos.logback.access.spi.IAccessEvent#getResponseContent() getResponseContent()}</code></td>
 *         <td></td>
 *         <td>false</td>
 *     </tr>
 * </table>
 * <p/>
 *
 * @author Espen A. Fossen
 */
public class JsonLayout extends JsonLayoutBase<IAccessEvent> {

    public static final String TIMESTAMP_ATTR_NAME = "timestamp";
    public static final String REMOTEADDR_ATTR_NAME = "remoteAddress";
    public static final String REMOTEUSER_ATTR_NAME = "remoteUser";
    public static final String REQUESTTIME_ATTR_NAME = "requestTime";
    public static final String REQUESTURI_ATTR_NAME = "uri";
    public static final String STATUSCODE_ATTR_NAME = "status";
    public static final String METHOD_ATTR_NAME = "method";
    public static final String PROTOCOL_ATTR_NAME = "protocol";
    public static final String CONTENTLENGTH_ATTR_NAME = "contentLength";
    public static final String REQUESTURL_ATTR_NAME = "url";
    public static final String REMOTEHOST_ATTR_NAME = "remoteHost";
    public static final String SERVERNAME_ATTR_NAME = "serverName";
    public static final String REQUESTHEADER_ATTR_NAME = "headers";
    public static final String REQUESTPARAMETER_ATTR_NAME = "params";
    public static final String LOCALPORT_ATTR_NAME = "port";
    public static final String REQUESTCONTENT_ATTR_NAME = "requestContent";
    public static final String RESPONSECONTENT_ATTR_NAME = "responseContent";

    protected boolean includeRemoteAddr;
    protected boolean includeRemoteUser;
    protected boolean includeRequestTime;
    protected boolean includeRequestURI;
    protected boolean includeStatusCode;
    protected boolean includeMethod;
    protected boolean includeProtocol;
    protected boolean includeContentLength;
    protected boolean includeRequestURL;
    protected boolean includeRemoteHost;
    protected boolean includeServerName;
    protected boolean includeRequestHeader;
    protected boolean includeRequestParameter;
    protected boolean includeLocalPort;
    protected boolean includeRequestContent;
    protected boolean includeResponseContent;


    public JsonLayout() {
        super();
        this.includeRemoteAddr = true;
        this.includeRemoteUser = true;
        this.includeRequestTime = true;
        this.includeRequestURI = true;
        this.includeStatusCode = true;
        this.includeMethod = true;
        this.includeProtocol = true;
        this.includeRequestURL = false;
        this.includeContentLength = false;
        this.includeRemoteHost = true;
        this.includeServerName = true;
        this.includeRequestHeader = true;
        this.includeRequestParameter = true;
        this.includeLocalPort = false;
        this.includeRequestContent = false;
        this.includeResponseContent = false;
    }

    @Override
    protected Map toJsonMap(IAccessEvent event) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();

        if (this.includeTimestamp) {
            long timestamp = event.getTimeStamp();
            String formatted = formatTimestamp(timestamp);
            if (formatted != null) {
                map.put(TIMESTAMP_ATTR_NAME, formatted);
            }
        }

        if (this.includeRemoteAddr) {
            String remoteAddr = event.getRemoteAddr();
            if (remoteAddr == null) {
                remoteAddr = event.getRemoteAddr();
            }


            if (remoteAddr != null) {
                map.put(REMOTEADDR_ATTR_NAME, remoteAddr);
            }
        }

        if (this.includeRemoteUser) {
            String remoteUser = event.getRemoteUser();
            if (remoteUser != null) {
                map.put(REMOTEUSER_ATTR_NAME, remoteUser);
            }
        }

        if (this.includeRequestTime) {
            long elapsedTime = event.getElapsedTime();
            if (elapsedTime > 0) {
                final long sec = TimeUnit.MILLISECONDS.toSeconds(event.getElapsedTime());
                final long ms = TimeUnit.MILLISECONDS.toMillis(event.getElapsedTime() - TimeUnit.SECONDS.toMillis(sec));
                String time = String.format("%01d.%03d", sec, ms);
                if (time != null) {
                    map.put(REQUESTTIME_ATTR_NAME, time);
                }
            }
        }

        if (this.includeStatusCode) {
            String statusCode = String.valueOf(event.getStatusCode());
            if (statusCode != null) {
                map.put(STATUSCODE_ATTR_NAME, statusCode);
            }
        }

        if (this.includeMethod) {
            String method = event.getMethod();
            if (method != null) {
                map.put(METHOD_ATTR_NAME, method);
            }
        }

        if (this.includeRequestURI) {
            String requestURI = event.getRequestURI();
            if (requestURI != null) {
                map.put(REQUESTURI_ATTR_NAME, requestURI);
            }
        }

        if (this.includeProtocol) {
            String protocol = event.getProtocol();
            if (protocol != null) {
                map.put(PROTOCOL_ATTR_NAME, protocol);
            }
        }

        if (this.includeContentLength) {
            String contentLength = Long.toString(event.getContentLength());
            if (contentLength != null) {
                map.put(CONTENTLENGTH_ATTR_NAME, contentLength);
            }
        }

        if (this.includeRequestURL) {
            String requestURL = event.getRequestURL();
            if (requestURL != null) {
                map.put(REQUESTURL_ATTR_NAME, requestURL);
            }
        }

        if (this.includeRemoteHost) {
            String remoteHost = event.getRemoteHost();
            if (remoteHost != null) {
                map.put(REMOTEHOST_ATTR_NAME, remoteHost);
            }
        }

        if (this.includeServerName) {
            String serverName = event.getServerName();
            if (serverName != null) {
                map.put(SERVERNAME_ATTR_NAME, serverName);
            }
        }

        if (this.includeRequestHeader) {
            Map<String, String> requestHeader = event.getRequestHeaderMap();
            if ((requestHeader != null) && !requestHeader.isEmpty()) {
                map.put(REQUESTHEADER_ATTR_NAME, requestHeader);
            }
        }

        if (this.includeRequestParameter) {
            Map<String, String[]> requestParameter = event.getRequestParameterMap();
            if ((requestParameter != null) && !requestParameter.isEmpty()) {
                map.put(REQUESTPARAMETER_ATTR_NAME, requestParameter);
            }
        }

        if (this.includeLocalPort) {
            String localPort = String.valueOf(event.getLocalPort());
            if (localPort != null) {
                map.put(LOCALPORT_ATTR_NAME, localPort);
            }
        }

        if (this.includeRequestContent) {
            String requestContent = event.getRequestContent();
            if (requestContent != null) {
                map.put(REQUESTCONTENT_ATTR_NAME, requestContent);
            }
        }

        if (this.includeResponseContent) {
            String responseContent = event.getResponseContent();
            if (responseContent != null) {
                map.put(RESPONSECONTENT_ATTR_NAME, responseContent);
            }
        }

        return map;

    }

    public boolean isIncludeRemoteAddr() {
        return includeRemoteAddr;
    }

    public void setIncludeRemoteAddr(boolean includeRemoteAddr) {
        this.includeRemoteAddr = includeRemoteAddr;
    }

    public boolean isIncludeRemoteUser() {
        return includeRemoteUser;
    }

    public void setIncludeRemoteUser(boolean includeRemoteUser) {
        this.includeRemoteUser = includeRemoteUser;
    }

    public boolean isIncludeRequestTime() {
        return includeRequestTime;
    }

    public void setIncludeRequestTime(boolean includeRequestTime) {
        this.includeRequestTime = includeRequestTime;
    }

    public boolean isIncludeRequestURI() {
        return includeRequestURI;
    }

    public void setIncludeRequestURI(boolean includeRequestURI) {
        this.includeRequestURI = includeRequestURI;
    }

    public boolean isIncludeStatusCode() {
        return includeStatusCode;
    }

    public void setIncludeStatusCode(boolean includeStatusCode) {
        this.includeStatusCode = includeStatusCode;
    }

    public boolean isIncludeMethod() {
        return includeMethod;
    }

    public void setIncludeMethod(boolean includeMethod) {
        this.includeMethod = includeMethod;
    }

    public boolean isIncludeProtocol() {
        return includeProtocol;
    }

    public void setIncludeProtocol(boolean includeProtocol) {
        this.includeProtocol = includeProtocol;
    }

    public boolean isIncludeContentLength() {
        return includeContentLength;
    }

    public void setIncludeContentLength(boolean includeContentLength) {
        this.includeContentLength = includeContentLength;
    }

    public boolean isIncludeRequestURL() {
        return includeRequestURL;
    }

    public void setIncludeRequestURL(boolean includeRequestURL) {
        this.includeRequestURL = includeRequestURL;
    }

    public boolean isIncludeRemoteHost() {
        return includeRemoteHost;
    }

    public void setIncludeRemoteHost(boolean includeRemoteHost) {
        this.includeRemoteHost = includeRemoteHost;
    }

    public boolean isIncludeServerName() {
        return includeServerName;
    }

    public void setIncludeServerName(boolean includeServerName) {
        this.includeServerName = includeServerName;
    }

    public boolean isIncludeRequestHeader() {
        return includeRequestHeader;
    }

    public void setIncludeRequestHeader(boolean includeRequestHeader) {
        this.includeRequestHeader = includeRequestHeader;
    }

    public boolean isIncludeRequestParameter() {
        return includeRequestParameter;
    }

    public void setIncludeRequestParameter(boolean includeRequestParameter) {
        this.includeRequestParameter = includeRequestParameter;
    }

    public boolean isIncludeLocalPort() {
        return includeLocalPort;
    }

    public void setIncludeLocalPort(boolean includeLocalPort) {
        this.includeLocalPort = includeLocalPort;
    }

    public boolean isIncludeRequestContent() {
        return includeRequestContent;
    }

    public void setIncludeRequestContent(boolean includeRequestContent) {
        this.includeRequestContent = includeRequestContent;
    }

    public boolean isIncludeResponseContent() {
        return includeResponseContent;
    }

    public void setIncludeResponseContent(boolean includeResponseContent) {
        this.includeResponseContent = includeResponseContent;
    }
}