package com.msg.ccp.processor;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import com.danubetech.verifiablecredentials.VerifiablePresentation;
import com.msg.ccp.claims.ClaimsCredentialsService;
import com.msg.ccp.exception.CcpException;
import com.msg.ccp.interfaces.catalogue.CatalogueResponse;
import com.msg.ccp.interfaces.catalogue.ICatalogueService;
import com.msg.ccp.interfaces.compliance.IComplianceServiceService;
import com.msg.ccp.interfaces.signer.ISignerService;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class VCProcessorTest {
    @Mock
    private IComplianceServiceService complianceServiceServiceMock;
    @Mock
    private ISignerService signerServiceMock;
    @Mock
    private ICatalogueService catalogueService;
    @Mock
    private ClaimsCredentialsService claimsCredentialsService;

    @InjectMocks
    private VCProcessor vcProcessor;

    @BeforeEach
    public void setup() throws Exception {
        try (final AutoCloseable closeable =  MockitoAnnotations.openMocks(this)) {
            when(signerServiceMock.createVCsFromClaims(any())).thenReturn(Collections.emptySet());
            when(signerServiceMock.createVPfromVCs(any())).thenReturn(new VerifiablePresentation());
            when(complianceServiceServiceMock.getComplianceCredential(any())).thenReturn(new VerifiableCredential());
            when(catalogueService.invoke(any())).thenReturn(new CatalogueResponse("now",
                    "active", "issuer", "now", new ArrayList<>()));
            when(claimsCredentialsService.separateDomainSpecificCredentials(any())).thenCallRealMethod();
            when(claimsCredentialsService.findServiceOfferings(any())).thenCallRealMethod();
            when(claimsCredentialsService.findResourceOfferings(any())).thenCallRealMethod();
        }
    }

    @Test
    @DisplayName("IF neither ServiceOffering nor Resources were passed THEN an exception is expected.")
    void exceptionInCaseOfNoValidClaims() {
        // prepare
        final Set<Map<String, Object>> claims = new HashSet<>();
        final Set<VerifiableCredential> credentials = new HashSet<>();

        // action & verify
        assertThatThrownBy(() -> vcProcessor.process(claims, credentials))
                .isInstanceOf(CcpException.class)
                .hasMessage("Neither ServiceOffering nor ResourceOffering found");
    }
}
