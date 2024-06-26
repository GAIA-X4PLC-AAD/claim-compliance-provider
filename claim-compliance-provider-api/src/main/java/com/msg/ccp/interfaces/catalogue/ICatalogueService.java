package com.msg.ccp.interfaces.catalogue;

import com.danubetech.verifiablecredentials.VerifiablePresentation;
import com.msg.ccp.interfaces.config.IServiceConfiguration;

public interface ICatalogueService extends IServiceConfiguration {

    FederatedCatalogueResponse verify(final VerifiablePresentation verifiablePresentation);
}
