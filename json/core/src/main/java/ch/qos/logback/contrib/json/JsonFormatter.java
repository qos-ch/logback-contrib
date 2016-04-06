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
package ch.qos.logback.contrib.json;

import java.util.Map;

/**
 * A {@code JsonFormatter} formats a data {@link Map Map} into a JSON string.
 *
 * @author Les Hazlewood
 * @since 0.1
 */
public interface JsonFormatter {

    /**
     * Converts the specified map into a JSON string.
     *
     * @param m the map to be converted.
     * @return a JSON String representation of the specified Map instance.
     * @throws Exception if there is a problem converting the map to a String.
     */
    String toJsonString(Map m) throws Exception;

    /**
     * Converts the specified JSON string into a Map.
     * @since 0.1.3  by angus.aqlu@gmail.com
     *
     * @param json the String to be converted
     * @return a MAP instance
     * @throws Exception if there is a problem converting the json to a Map.
     */
    Map parseJsonString(String json) throws Exception;
}
