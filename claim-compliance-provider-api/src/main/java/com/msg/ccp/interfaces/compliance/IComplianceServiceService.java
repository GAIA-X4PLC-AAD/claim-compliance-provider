package com.msg.ccp.interfaces.compliance;

import com.msg.ccp.interfaces.config.IServiceConfiguration;

import java.util.Map;

public interface IComplianceServiceService extends IServiceConfiguration {

    Map<String, Object> getComplianceCredential(final Map<String, Object> verifiablePresentationWithoutProof);

}
