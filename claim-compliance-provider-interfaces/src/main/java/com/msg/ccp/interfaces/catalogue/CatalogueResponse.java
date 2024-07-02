package com.msg.ccp.interfaces.catalogue;

import java.io.Serializable;
import java.util.List;

/**
 * CatalogueResponse class returned by the service implementation.
 * @param verificationTimestamp the verification timestamp.
 * @param lifecycleStatus the lifecycle status.
 * @param issuer the issuer.
 * @param issuedDateTime the issued date time.
 * @param validatorDids the validator DIDs.
 */
public record CatalogueResponse(String verificationTimestamp, String lifecycleStatus, String issuer, String issuedDateTime, List<String> validatorDids) implements Serializable {
}
