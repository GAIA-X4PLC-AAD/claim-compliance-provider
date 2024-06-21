package com.msg.ccp.claimcreator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Replacement {
    private String countrySubdivisionCode;
    private String dataResourceId;
    private String expirationDateTime;
    private String instantiatedVirtualResourceId;
    private String legalParticipantId;
    private String legalRegistrationNumber;
    private String obsoleteDateTime;
    private String physicalResourceId;
    private String physicalResourceLegalParticipantId;
    private String serviceAccessPointId;
    private String serviceOfferingId;
}

