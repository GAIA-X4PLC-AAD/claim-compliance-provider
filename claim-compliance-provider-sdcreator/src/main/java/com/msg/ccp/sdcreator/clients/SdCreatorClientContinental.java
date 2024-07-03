package com.msg.ccp.sdcreator.clients;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@ApplicationScoped
@RegisterRestClient
public interface SdCreatorClientContinental extends SdCreatorClient {
}
