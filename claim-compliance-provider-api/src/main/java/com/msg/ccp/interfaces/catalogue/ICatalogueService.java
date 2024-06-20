package com.msg.ccp.interfaces.catalogue;

import com.danubetech.verifiablecredentials.VerifiablePresentation;

public interface ICatalogueService {

    FederatedCatalogueResponse verify(final VerifiablePresentation verifiablePresentation);
}
