package com.msg.ccp.catalogue;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FederatedCatalogueResponse {
        private String verificationTimestamp;
        private String lifecycleStatus;
        private String issuer;
        private String issuedDateTime;
        List<String> validatorDids = new ArrayList<>();
}
