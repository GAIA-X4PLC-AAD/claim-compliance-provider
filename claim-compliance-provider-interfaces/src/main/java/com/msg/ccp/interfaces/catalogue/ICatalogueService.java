package com.msg.ccp.interfaces.catalogue;

import com.danubetech.verifiablecredentials.VerifiablePresentation;
import com.msg.ccp.interfaces.config.IServiceConfiguration;

/**
 * Interface for the catalogue service.
 */
public interface ICatalogueService extends IServiceConfiguration {
    /**
     * Invoke the catalogue service. This can be any call from the catalogue service.
     * @param verifiablePresentation the verifiable presentation to be passed.
     * @return the response from the catalogue service.
     */
    CatalogueResponse invoke(final VerifiablePresentation verifiablePresentation);
}
