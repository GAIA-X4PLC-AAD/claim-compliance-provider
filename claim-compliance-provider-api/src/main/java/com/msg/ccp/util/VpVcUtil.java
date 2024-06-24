package com.msg.ccp.util;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import com.danubetech.verifiablecredentials.VerifiablePresentation;

import java.util.Map;
import java.util.Set;

/**
 * util class for VP and VC.
 * VerifiablePresentation and VerifiableCredential do only know id and type, but not @id and @type (from the json ld spec).
 */
public class VpVcUtil {

    public static String getType(final VerifiablePresentation verifiablePresentation) {
        final Map<String, Object> vpMap = verifiablePresentation.toMap();
        return obtainType(vpMap);
    }

    public static String getType(final VerifiableCredential verifiableCredential) {
        final Map<String, Object> vpMap = verifiableCredential.toMap();
        return obtainType(vpMap);
    }

    public static String getType(final Map<String, Object> map) {
        return obtainType(map);
    }

    public static String getId(final VerifiablePresentation verifiablePresentation) {
        final Map<String, Object> vpMap = verifiablePresentation.toMap();
        return obtainId(vpMap);
    }

    public static String getId(final VerifiableCredential verifiableCredential) {
        final Map<String, Object> vpMap = verifiableCredential.toMap();
        return obtainId(vpMap);
    }

    public static String getId(final Map<String, Object> map) {
        return obtainId(map);
    }

    public static Set<Map<String, Object>> getCredentialSubjects(final Map<String, Object> map) {
        final Object credentialSubjects = map.get("credentialSubject");
        if (credentialSubjects instanceof Map) {
            return Set.of((Map<String, Object>) credentialSubjects);
        } else {
            return (Set<Map<String, Object>>) credentialSubjects;
        }
    }

    private static String obtainType(final Map<String, Object> map) {
        if (map.containsKey("type")) {
            return map.get("type").toString();
        } else if (map.containsKey("@type")) {
            return map.get("@type").toString();
        } else return null;
    }

    private static String obtainId(final Map<String, Object> map) {
        if (map.containsKey("id")) {
            return map.get("id").toString();
        } else if (map.containsKey("@id")) {
            return map.get("@id").toString();
        } else return null;
    }

    private VpVcUtil() {
        super();
    }
}
