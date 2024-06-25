package com.msg.ccp.interfaces.catalogue;

import java.io.Serializable;
import java.util.List;

public record FederatedCatalogueResponse(String verificationTimestamp, String lifecycleStatus, String issuer, String issuedDateTime, List<String> validatorDids) implements Serializable {
}
