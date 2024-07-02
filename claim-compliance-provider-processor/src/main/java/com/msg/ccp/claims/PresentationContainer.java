package com.msg.ccp.claims;

import com.danubetech.verifiablecredentials.VerifiablePresentation;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public record PresentationContainer (VerifiablePresentation serviceOfferings, VerifiablePresentation resources,
                                     VerifiablePresentation complianceCredentials) {
    public List<VerifiablePresentation> toList() {
        return Stream.of(serviceOfferings, resources, complianceCredentials)
                .filter(Objects::nonNull)
                .toList();
    }
}
