/**
 * Copyright (C) 2015, The logback-contrib developers. All rights reserved.
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
 *         <td nowrap="nowrap">{@code url}</td>
 *         <td nowrap="nowrap"><code>IAccessEvent.{@link ch.qos.logback.access.spi.IAccessEvent#getRequestURL() getRequestURL()}</code></td>
 *         <td></td>
 *         <td>true</td>
 *     </tr>
 *     <tr>
 *         <td nowrap="nowrap">{@code uri}</td>
 *         <td nowrap="nowrap"><code>IAccessEvent.{@link ch.qos.logback.access.spi.IAccessEvent#getRequestURI() getRequestURI()}</code></td>
 *         <td></td>
 *         <td>true</td>
 *     </tr>
 *     <tr>
 *         <td nowrap="nowrap">{@code remoteHost}</td>
 *         <td nowrap="nowrap"><code>IAccessEvent.{@link ch.qos.logback.access.spi.IAccessEvent#getRemoteHost() getRemoteHost()}</code></td>
 *         <td></td>
 *         <td>true</td>
 *     </tr>
 *     <tr>
 *         <td nowrap="nowrap">{@code remoteUser}</td>
 *         <td nowrap="nowrap"><code>IAccessEvent.{@link ch.qos.logback.access.spi.IAccessEvent#getRemoteUser() getRemoteUser()}</code></td>
 *         <td></td>
 *         <td>true</td>
 *     </tr>
 *     <tr>
 *         <td nowrap="nowrap">{@code remoteAddress}</td>
 *         <td nowrap="nowrap"><code>IAccessEvent.{@link ch.qos.logback.access.spi.IAccessEvent#getRemoteAddr() getRemoteAddr()}</code></td>
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
 *         <td nowrap="nowrap">{@code headers}</td>
 *         <td nowrap="nowrap"><code>IAccessEvent.{@link ch.qos.logback.access.spi.IAccessEvent#getRequestHeaderMap() getRequestHeaderMap()}</code></td>
 *         <td>This value is a {@code Map&lt;String,String&gt;}.</td>
 *         <td>true</td>
 *     </tr>
 *     <tr>
 *         <td nowrap="nowrap">{@code protocol}</td>
 *         <td nowrap="nowrap"><code>IAccessEvent.{@link ch.qos.logback.access.spi.IAccessEvent#getProtocol() getProtocol()}</code></td>
 *         <td></td>
 *         <td>true</td>
 *     </tr>
 *     <tr>
 *         <td nowrap="nowrap">{@code servername}</td>
 *         <td nowrap="nowrap"><code>IAccessEvent.{@link ch.qos.logback.access.spi.IAccessEvent#getServerName() getServerName()}</code></td>
 *         <td></td>
 *         <td>true</td>
 *     </tr>
 *     <tr>
 *         <td nowrap="nowrap">{@code params}</td>
 *         <td nowrap="nowrap"><code>IAccessEvent.{@link ch.qos.logback.access.spi.IAccessEvent#getRequestParameterMap() getRequestParameterMap()}</code></td>
 *         <td>This value is a {@code Map&lt;String,String&gt;}.</td>
 *         <td>true</td>
 *     </tr>
 *     <tr>
 *         <td nowrap="nowrap">{@code status}</td>
 *         <td nowrap="nowrap">String value of <code>IAccessEvent.{@link ch.qos.logback.access.spi.IAccessEvent#getStatusCode getStatusCode()}</code></td>
 *         <td></td>
 *         <td>true</td>
 *     </tr>
 *     <tr>
 *         <td nowrap="nowrap">{@code port}</td>
 *         <td nowrap="nowrap">String value of <code>IAccessEvent.{@link ch.qos.logback.access.spi.IAccessEvent#getLocalPort() getLocalPort()}</code></td>
 *         <td></td>
 *         <td>true</td>
 *     </tr>
 * </table>
 * <p/>
 *
 * @author Espen A. Fossen
 */
public class JsonLayout extends JsonLayoutBase<IAccessEvent> {

    public static final String TIMESTAMP_ATTR_NAME = "timestamp";
    public static final String REQUESTURI_ATTR_NAME = "uri";
    public static final String REQUESTURL_ATTR_NAME = "url";
    public static final String REQUESTHEADER_ATTR_NAME = "headers";
    public static final String REMOTEHOST_ATTR_NAME = "remoteHost";
    public static final String REMOTEUSER_ATTR_NAME = "remoteUser";
    public static final String REMOTEADDR_ATTR_NAME = "remoteAddress";
    public static final String METHOD_ATTR_NAME = "method";
    public static final String PROTOCOL_ATTR_NAME = "protocol";
    public static final String SERVERNAME_ATTR_NAME = "servername";
    public static final String REQUESTPARAMETER_ATTR_NAME = "params";
    public static final String STATUSCODE_ATTR_NAME = "status";
    public static final String LOCALPORT_ATTR_NAME = "port";

    protected boolean includeRequestURI;
    protected boolean includeRequestURL;
    protected boolean includeRequestHeader;
    protected boolean includeRemoteHost;
    protected boolean includeRemoteUser;
    protected boolean includeRemoteAddr;
    protected boolean includeMethod;
    protected boolean includeProtocol;
    protected boolean includeServerName;
    protected boolean includeRequestParameter;
    protected boolean includeStatusCode;
    protected boolean includeLocalPort;


    public JsonLayout() {
        super();
        this.includeRequestURI = true;
        this.includeRequestURL = true;
        this.includeRequestHeader = true;
        this.includeRemoteHost = true;
        this.includeRemoteUser = true;
        this.includeRemoteAddr = true;
        this.includeMethod = true;
        this.includeProtocol = true;
        this.includeServerName = true;
        this.includeStatusCode = true;
        this.includeRequestParameter = true;
        this.includeLocalPort = true;
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

        if (this.includeRequestURI) {
            String requestURI = event.getRequestURI();
            if (requestURI != null) {
                map.put(REQUESTURI_ATTR_NAME, requestURI);
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

        if (this.includeRemoteUser) {
            String remoteUser = event.getRemoteUser();
            if (remoteUser != null) {
                map.put(REMOTEUSER_ATTR_NAME, remoteUser);
            }
        }

        if (this.includeRemoteAddr) {
            String remoteAddr = event.getRemoteAddr();
            if (remoteAddr != null) {
                map.put(REMOTEADDR_ATTR_NAME, remoteAddr);
            }
        }

        if (this.includeMethod) {
            String method = event.getMethod();
            if (method != null) {
                map.put(METHOD_ATTR_NAME, method);
            }
        }

        if (this.includeRequestHeader) {
            Map<String, String> requestHeader = event.getRequestHeaderMap();
            if ((requestHeader != null) && !requestHeader.isEmpty()) {
                map.put(REQUESTHEADER_ATTR_NAME, requestHeader);
            }
        }

        if (this.includeProtocol) {
            String protocol = event.getProtocol();
            if (protocol != null) {
                map.put(PROTOCOL_ATTR_NAME, protocol);
            }
        }

        if (this.includeServerName) {
            String serverName = event.getServerName();
            if (serverName != null) {
                map.put(SERVERNAME_ATTR_NAME, serverName);
            }
        }

        if (this.includeRequestParameter) {
            Map<String, String[]> requestParameter = event.getRequestParameterMap();
            if ((requestParameter != null) && !requestParameter.isEmpty()) {
                map.put(REQUESTPARAMETER_ATTR_NAME, requestParameter);
            }
        }

        if (this.includeStatusCode) {
            String statusCode = String.valueOf(event.getStatusCode());
            if (statusCode != null) {
                map.put(STATUSCODE_ATTR_NAME, statusCode);
            }
        }

        if (this.includeLocalPort) {
            String localPort = String.valueOf(event.getLocalPort());
            if (localPort != null) {
                map.put(LOCALPORT_ATTR_NAME, localPort);
            }
        }

        return map;

    }

    public boolean isIncludeRequestURI() {
        return includeRequestURI;
    }

    public void setIncludeRequestURI(boolean includeRequestURI) {
        this.includeRequestURI = includeRequestURI;
    }

    public boolean isIncludeRequestURL() {
        return includeRequestURL;
    }

    public void setIncludeRequestURL(boolean includeRequestURL) {
        this.includeRequestURL = includeRequestURL;
    }

    public boolean isIncludeRequestHeader() {
        return includeRequestHeader;
    }

    public void setIncludeRequestHeader(boolean includeRequestHeader) {
        this.includeRequestHeader = includeRequestHeader;
    }

    public boolean isIncludeRemoteHost() {
        return includeRemoteHost;
    }

    public void setIncludeRemoteHost(boolean includeRemoteHost) {
        this.includeRemoteHost = includeRemoteHost;
    }

    public boolean isIncludeRemoteUser() {
        return includeRemoteUser;
    }

    public void setIncludeRemoteUser(boolean includeRemoteUser) {
        this.includeRemoteUser = includeRemoteUser;
    }

    public boolean isIncludeRemoteAddr() {
        return includeRemoteAddr;
    }

    public void setIncludeRemoteAddr(boolean includeRemoteAddr) {
        this.includeRemoteAddr = includeRemoteAddr;
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

    public boolean isIncludeServerName() {
        return includeServerName;
    }

    public void setIncludeServerName(boolean includeServerName) {
        this.includeServerName = includeServerName;
    }

    public boolean isIncludeRequestParameter() {
        return includeRequestParameter;
    }

    public void setIncludeRequestParameter(boolean includeRequestParameter) {
        this.includeRequestParameter = includeRequestParameter;
    }

    public boolean isIncludeStatusCode() {
        return includeStatusCode;
    }

    public void setIncludeStatusCode(boolean includeStatusCode) {
        this.includeStatusCode = includeStatusCode;
    }

    public boolean isIncludeLocalPort() {
        return includeLocalPort;
    }

    public void setIncludeLocalPort(boolean includeLocalPort) {
        this.includeLocalPort = includeLocalPort;
    }

}