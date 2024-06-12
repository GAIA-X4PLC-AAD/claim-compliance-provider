package com.msg.ccp.compliance;

import java.util.Map;

public interface IComplianceServiceService {

    Map<String, Object> getComplianceCredential(final Map<String, Object> verifiablePresentationWithoutProof);
}
