package com.msg.ccp.interfaces.config;

import java.util.Map;
import java.util.Set;

/**
 * Interface for the service configuration.
 */
public interface IServiceConfiguration {
    String KEY_PROPERTY = "key";
    String VALUE_PROPERTY = "value";

    /**
     * Gets the components properties each containing its key and value.
     * @return components properties
     */
    Set<Map<String,Object>> getConfig();
}
