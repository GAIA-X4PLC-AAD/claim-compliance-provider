package com.msg.ccp.claimcreator;

public record Replacement (String dataResourceId, String expirationDateTime, String instantiatedVirtualResourceId, String legalParticipantId,
                           String obsoleteDateTime, String physicalResourceId, String physicalResourceLegalParticipantId, String serviceAccessPointId,
                           String serviceOfferingId) {
}

