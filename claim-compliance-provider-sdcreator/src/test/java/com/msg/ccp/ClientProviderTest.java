package com.msg.ccp;

import com.msg.ccp.sdcreator.ClientProvider;
import com.msg.ccp.sdcreator.clients.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

class ClientProviderTest {
    @Mock
    private SdCreatorClientDefault sdCreatorClientDefault;
    @Mock
    private SdCreatorClient3dMapping sdCreatorClient3dMapping;
    @Mock
    private SdCreatorClientAscs sdCreatorClientAscs;
    @Mock
    private SdCreatorClientBmw sdCreatorClientBmw;
    @Mock
    private SdCreatorClientContinental sdCreatorClientContinental;
    @Mock
    private SdCreatorClientDlr sdCreatorClientDlr;
    @Mock
    private SdCreatorClientGx4fmPlcaad sdCreatorClientGx4fmPlcaad;
    @Mock
    private SdCreatorClientInfineon sdCreatorClientInfineon;
    @Mock
    private SdCreatorClientIviFraunhofer sdCreatorClientIviFraunhofer;
    @Mock
    private SdCreatorClientMsgSystemsAg sdCreatorClientMsgSystemsAg;
    @Mock
    private SdCreatorClientSetlabs sdCreatorClientSetlabs;
    @Mock
    private SdCreatorClientTracetronic sdCreatorClientTracetronic;
    @Mock
    private SdCreatorClientTriangraphics sdCreatorClientTriangraphics;
    @Mock
    private SdCreatorClientTuBerlin sdCreatorClientTuBerlin;
    @Mock
    private SdCreatorClientTuMuenchen sdCreatorClientTuMuenchen;


    @InjectMocks
    private ClientProvider clientProvider;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("IF known issuer is passed THEN the correct client is returned.")
    void testGetClientReturnsCorrectClientForKnownIssuer() {
        assertThat(clientProvider.getClient("did:web:participant.gxfs.gx4fm.org:3dmapping")).isSameAs(sdCreatorClient3dMapping);
        assertThat(clientProvider.getClient("did:web:participant.gxfs.gx4fm.org:ascs")).isSameAs(sdCreatorClientAscs);
        assertThat(clientProvider.getClient("did:web:participant.gxfs.gx4fm.org:bmw")).isSameAs(sdCreatorClientBmw);
        assertThat(clientProvider.getClient("did:web:participant.gxfs.gx4fm.org:continental")).isSameAs(sdCreatorClientContinental);
        assertThat(clientProvider.getClient("did:web:participant.gxfs.gx4fm.org:dlr")).isSameAs(sdCreatorClientDlr);
        assertThat(clientProvider.getClient("did:web:participant.gxfs.gx4fm.org:gx4fm-plcaad")).isSameAs(sdCreatorClientGx4fmPlcaad);
        assertThat(clientProvider.getClient("did:web:participant.gxfs.gx4fm.org:infineon")).isSameAs(sdCreatorClientInfineon);
        assertThat(clientProvider.getClient("did:web:participant.gxfs.gx4fm.org:ivi-fraunhofer")).isSameAs(sdCreatorClientIviFraunhofer);
        assertThat(clientProvider.getClient("did:web:participant.gxfs.gx4fm.org:msg-systems-ag")).isSameAs(sdCreatorClientMsgSystemsAg);
        assertThat(clientProvider.getClient("did:web:participant.gxfs.gx4fm.org:setlabs")).isSameAs(sdCreatorClientSetlabs);
        assertThat(clientProvider.getClient("did:web:participant.gxfs.gx4fm.org:tracetronic")).isSameAs(sdCreatorClientTracetronic);
        assertThat(clientProvider.getClient("did:web:participant.gxfs.gx4fm.org:triangraphics")).isSameAs(sdCreatorClientTriangraphics);
        assertThat(clientProvider.getClient("did:web:participant.gxfs.gx4fm.org:tu-berlin")).isSameAs(sdCreatorClientTuBerlin);
        assertThat(clientProvider.getClient("did:web:participant.gxfs.gx4fm.org:tu-muenchen")).isSameAs(sdCreatorClientTuMuenchen);
    }

    @Test
    @DisplayName("IF unknown issuer is passed THEN the default client is returned.")
    void testGetClientReturnsDefaultClientForUnknownIssuer() {
        // Example for an unknown issuer
        assertThat(clientProvider.getClient("did:web:participant.gxfs.gx4fm.org")).isSameAs(sdCreatorClientDefault);
        assertThat(clientProvider.getClient("did:web:msg-systems-ag")).isSameAs(sdCreatorClientDefault);

    }
}
