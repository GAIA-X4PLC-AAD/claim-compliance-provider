package com.msg.ccp.util;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import com.danubetech.verifiablecredentials.VerifiablePresentation;

import java.util.*;

/**
 * Util class for VP and VC.
 * VerifiablePresentation and VerifiableCredential do only know id and type, but not @id and @type (from the json ld spec).
 * This util class abstracts from this and provides methods to get the id and types from a VP or VC or credentialSubject.
 */
public class VpVcUtil {

    private static final String TYPE_FIELD = "type";
    private static final String TYPE_FIELD_WITH_AT = "@type";


    private static final String ID_FIELD = "id";
    private static final String ID_FIELD_WITH_AT = "@id";

    /**
     * Get the types from a map.
     * @param map the map to get the types from.
     * @return the types.
     */
    public static List<String> getTypes(final Map<String, Object> map) {
        return obtainTypes(map);
    }

    /**
     * Get the id from a VerifiablePresentation.
     * @param verifiablePresentation the VerifiablePresentation to get the id from.
     * @return the id.
     */
    public static String getId(final VerifiablePresentation verifiablePresentation) {
        final Map<String, Object> vpMap = verifiablePresentation.toMap();
        return obtainId(vpMap);
    }

    /**
     * Get the id from a VerifiableCredential.
     * @param verifiableCredential the VerifiableCredential to get the id from.
     * @return the id.
     */
    public static String getId(final VerifiableCredential verifiableCredential) {
        final Map<String, Object> vpMap = verifiableCredential.toMap();
        return obtainId(vpMap);
    }

    /**
     * Get the id from a map.
     * @param map the map to get the id from.
     * @return the id.
     */
    public static String getId(final Map<String, Object> map) {
        return obtainId(map);
    }

    /**
     * Get the credentialSubjects from a map.
     * @param map the map to get the credentialSubjects from.
     * @return the credentialSubjects.
     */
    @SuppressWarnings("unchecked")
    public static Set<Map<String, Object>> getCredentialSubjects(final Map<String, Object> map) {
        final Object credentialSubjects = map.get("credentialSubject");
        if (credentialSubjects instanceof Map) {
            return Set.of((Map<String, Object>) credentialSubjects);
        } else {
            return (Set<Map<String, Object>>) credentialSubjects;
        }
    }

    /**
     * Get the types from a map.
     * @param map the map to get the types from.
     * @return the types.
     */
    private static List<String> obtainTypes(final Map<String, Object> map) {
        final List<String> result = new ArrayList<>();
        final Object typeValue = map.get(TYPE_FIELD);
        final Object atTypeValue = map.get(TYPE_FIELD_WITH_AT);

        if (typeValue instanceof Collection) {
            ((Collection<?>) typeValue).stream().map(Object::toString).forEach(result::add);
        } else if (typeValue != null) {
            result.add(typeValue.toString());
        }

        if (atTypeValue instanceof Collection) {
            ((Collection<?>) atTypeValue).stream().map(Object::toString).forEach(result::add);
        } else if (atTypeValue != null) {
            result.add(atTypeValue.toString());
        }
        return result;
    }

    /**
     * Get the id from a map.
     * @param map the map to get the id from.
     * @return the id.
     */
    private static String obtainId(final Map<String, Object> map) {
        if (map.containsKey(ID_FIELD)) {
            return map.get(ID_FIELD).toString();
        } else if (map.containsKey(ID_FIELD_WITH_AT)) {
            return map.get(ID_FIELD_WITH_AT).toString();
        } else return null;
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private VpVcUtil() {
        super();
    }
}
