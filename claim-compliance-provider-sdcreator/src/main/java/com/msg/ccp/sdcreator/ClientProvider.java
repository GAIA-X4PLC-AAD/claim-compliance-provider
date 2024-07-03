package com.msg.ccp.sdcreator;

import com.msg.ccp.sdcreator.clients.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 * This class provides the correct client for the given issuer.
 * !!! THIS CLASS IS JUST TO SAVE RESOURCES IN OUR INFRASTRUCTURE !!!
 * Normally there would only be ONE dedicated client per claims-compliance-provider.
 * Otherwise, we would have to spin up several CCPs for each client.
 */
@ApplicationScoped
public class ClientProvider {
    private final SdCreatorClientDefault sdCreatorClientDefault;
    private final SdCreatorClient3dMapping sdCreatorClient3dMapping;
    private final SdCreatorClientAscs sdCreatorClientAscs;
    private final SdCreatorClientBmw sdCreatorClientBmw;
    private final SdCreatorClientContinental sdCreatorClientContinental;
    private final SdCreatorClientDlr sdCreatorClientDlr;
    private final SdCreatorClientGx4fmPlcaad sdCreatorClientGx4fmPlcaad;
    private final SdCreatorClientInfineon sdCreatorClientInfineon;
    private final SdCreatorClientIviFraunhofer sdCreatorClientIviFraunhofer;
    private final SdCreatorClientMsgSystemsAg sdCreatorClientMsgSystemsAg;
    private final SdCreatorClientSetlabs sdCreatorClientSetlabs;
    private final SdCreatorClientTracetronic sdCreatorClientTracetronic;
    private final SdCreatorClientTriangraphics sdCreatorClientTriangraphics;
    private final SdCreatorClientTuBerlin sdCreatorClientTuBerlin;
    private final SdCreatorClientTuMuenchen sdCreatorClientTuMuenchen;

    @Inject
    public ClientProvider(@RestClient final SdCreatorClientDefault sdCreatorClientDefault, @RestClient final SdCreatorClient3dMapping sdCreatorClient3dMapping,
                          @RestClient final SdCreatorClientAscs sdCreatorClientAscs, @RestClient final SdCreatorClientBmw sdCreatorClientBmw,
                          @RestClient final SdCreatorClientContinental sdCreatorClientContinental, @RestClient final SdCreatorClientDlr sdCreatorClientDlr,
                          @RestClient final SdCreatorClientGx4fmPlcaad sdCreatorClientGx4fmPlcaad, @RestClient final SdCreatorClientInfineon sdCreatorClientInfineon,
                          @RestClient final SdCreatorClientIviFraunhofer sdCreatorClientIviFraunhofer, @RestClient final SdCreatorClientMsgSystemsAg sdCreatorClientMsgSystemsAg,
                          @RestClient final SdCreatorClientSetlabs sdCreatorClientSetlabs, @RestClient final SdCreatorClientTracetronic sdCreatorClientTracetronic,
                          @RestClient final SdCreatorClientTriangraphics sdCreatorClientTriangraphics, @RestClient final SdCreatorClientTuBerlin sdCreatorClientTuBerlin,
                          @RestClient final SdCreatorClientTuMuenchen sdCreatorClientTuMuenchen) {
        this.sdCreatorClientDefault = sdCreatorClientDefault;
        this.sdCreatorClient3dMapping = sdCreatorClient3dMapping;
        this.sdCreatorClientAscs = sdCreatorClientAscs;
        this.sdCreatorClientBmw = sdCreatorClientBmw;
        this.sdCreatorClientContinental = sdCreatorClientContinental;
        this.sdCreatorClientDlr = sdCreatorClientDlr;
        this.sdCreatorClientGx4fmPlcaad = sdCreatorClientGx4fmPlcaad;
        this.sdCreatorClientInfineon = sdCreatorClientInfineon;
        this.sdCreatorClientIviFraunhofer = sdCreatorClientIviFraunhofer;
        this.sdCreatorClientMsgSystemsAg = sdCreatorClientMsgSystemsAg;
        this.sdCreatorClientSetlabs = sdCreatorClientSetlabs;
        this.sdCreatorClientTracetronic = sdCreatorClientTracetronic;
        this.sdCreatorClientTriangraphics = sdCreatorClientTriangraphics;
        this.sdCreatorClientTuBerlin = sdCreatorClientTuBerlin;
        this.sdCreatorClientTuMuenchen = sdCreatorClientTuMuenchen;
    }

    public SdCreatorClient getClient(final String issuer) {
        return switch (issuer) {
            case "did:web:participant.gxfs.gx4fm.org:3dmapping" -> sdCreatorClient3dMapping;
            case "did:web:participant.gxfs.gx4fm.org:ascs" -> sdCreatorClientAscs;
            case "did:web:participant.gxfs.gx4fm.org:bmw" -> sdCreatorClientBmw;
            case "did:web:participant.gxfs.gx4fm.org:continental" -> sdCreatorClientContinental;
            case "did:web:participant.gxfs.gx4fm.org:dlr" -> sdCreatorClientDlr;
            case "did:web:participant.gxfs.gx4fm.org:gx4fm-plcaad" -> sdCreatorClientGx4fmPlcaad;
            case "did:web:participant.gxfs.gx4fm.org:infineon" -> sdCreatorClientInfineon;
            case "did:web:participant.gxfs.gx4fm.org:ivi-fraunhofer" -> sdCreatorClientIviFraunhofer;
            case "did:web:participant.gxfs.gx4fm.org:msg-systems-ag" -> sdCreatorClientMsgSystemsAg;
            case "did:web:participant.gxfs.gx4fm.org:setlabs" -> sdCreatorClientSetlabs;
            case "did:web:participant.gxfs.gx4fm.org:tracetronic" -> sdCreatorClientTracetronic;
            case "did:web:participant.gxfs.gx4fm.org:triangraphics" -> sdCreatorClientTriangraphics;
            case "did:web:participant.gxfs.gx4fm.org:tu-berlin" -> sdCreatorClientTuBerlin;
            case "did:web:participant.gxfs.gx4fm.org:tu-muenchen" -> sdCreatorClientTuMuenchen;
            default -> sdCreatorClientDefault;
        };
    }
}
