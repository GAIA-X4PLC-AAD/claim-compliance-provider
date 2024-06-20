package com.msg.ccp.controller;

import com.danubetech.verifiablecredentials.VerifiablePresentation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

@ApplicationScoped
@Slf4j
@Path("")
public class ClaimComplianceProviderController {

    private final IClaimComplianceProviderService verifiableCredentialsProcessor;

    @Inject
    public ClaimComplianceProviderController(final IClaimComplianceProviderService verifiableCredentialsProcessor) {
        this.verifiableCredentialsProcessor = verifiableCredentialsProcessor;
    }

    @POST
    @Path("/v1/send-claims")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Initiate VC Processing", description = "This operation initiates the processing of Gaia-X participant credentials and claims. " +
            "The claims will be signed and checked against a Gaia-X compliance service. In addition to that a federated catalogue instance is called " +
            "(for either checking the verifiable presentation or storing it (depending on the implementation). As a result of the processing, a list of " +
            "verifiable presentations is created and returned to the caller. The input payload contains claims and verifiable credentials. The claims are " +
            "the data that the participant wants to sign and check with the service. " +
            "The verifiable credentials are the participant credentials that are sent along the signed claims to the Gaia-X compliance service. " +
            "The result verifiable presentations are signed by the signing implementation and can be used by the participant to prove the claims " +
            "to other services.")
    @RequestBody(description = "Payload containing claims and verifiable credentials", required = true,
            content = @Content(schema = @Schema(implementation = Payload.class), examples = {
                    @ExampleObject(name = "examplePayload", value = """
                            {
                              "claims": [
                                {
                                  "@context": {
                                    "xsd": "http://www.w3.org/2001/XMLSchema#",
                                    "gx": "https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#",
                                    "rdf": "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
                                    "sh": "http://www.w3.org/ns/shacl#"
                                  },
                                  "@id": "https://www.msg.group/dataresource",
                                  "@type": "gx:DataResource",
                                  "gx:copyrightOwnedBy": "https://participant.gxfs.gx4fm.org/.well-known/legalParticipant.json",
                                  "gx:license": "Apache-2.0",
                                  "gx:obsoleteDateTime": {
                                    "@value": "2024-04-26T12:37:03",
                                    "@type": "xsd:dateTime"
                                  },
                                  "gx:exposedThrough": {
                                    "@id": "https://www.msg.group/serviceoffering"
                                  },
                                  "gx:expirationDateTime": {
                                    "@value": "2024-09-19T17:45:10",
                                    "@type": "xsd:dateTime"
                                  },
                                  "gx:containsPII": false,
                                  "gx:policy": {
                                    "@value": "This could be the policy of a DataResource",
                                    "@type": "xsd:string"
                                  },
                                  "gx:producedBy": {
                                    "@id": "https://participant.gxfs.gx4fm.org/.well-known/legalParticipant.json"
                                  },
                                  "gx:name": {
                                    "@value": "Data Resource Name",
                                    "@type": "xsd:string"
                                  },
                                  "gx:description": {
                                    "@value": "This could be the description of a data resource.",
                                    "@type": "xsd:string"
                                  }
                                },
                                {
                                  "@context": {
                                    "gx": "https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#",
                                    "rdf": "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
                                    "sh": "http://www.w3.org/ns/shacl#"
                                  },
                                  "@id": "https://www.msg.group/instantiatedvirtualresource",
                                  "@type": "gx:InstantiatedVirtualResource",
                                  "gx:maintainedBy": {
                                    "@id": "https://participant.gxfs.gx4fm.org/.well-known/legalParticipant.json"
                                  },
                                  "gx:hostedOn": {
                                    "@id": "https://www.msg.group/physicalresource",
                                    "@type": "gx:PhysicalResource",
                                    "gx:maintainedBy": {
                                      "@id": "https://participant.gxfs.gx4fm.org/.well-known/legalParticipant.json"
                                    },
                                    "gx:ownedBy": {
                                      "@id": "https://participant.gxfs.gx4fm.org/.well-known/legalParticipant.json"
                                    },
                                    "gx:manufacturedBy": {
                                      "@id": "https://participant.gxfs.gx4fm.org/.well-known/legalParticipant.json"
                                    },
                                    "gx:location": {
                                      "@value": "DE",
                                      "@type": "xsd:string"
                                    },
                                    "gx:name": {
                                      "@value": "msg PhysicalResource",
                                      "@type": "xsd:string"
                                    },
                                    "gx:description": {
                                      "@value": "This could be used to even further describe the physical resource.",
                                      "@type": "xsd:string"
                                    }
                                  },
                                  "gx:instanceOf": {
                                    "@id": "https://www.msg.group/dataresource",
                                    "@type": "gx:DataResource",
                                    "gx:copyrightOwnedBy": "https://participant.gxfs.gx4fm.org/.well-known/legalParticipant.json",
                                    "gx:license": "Apache-2.0",
                                    "gx:obsoleteDateTime": {
                                      "@value": "2024-04-26T12:37:03",
                                      "@type": "xsd:dateTime"
                                    },
                                    "gx:exposedThrough": {
                                      "@id": "https://www.msg.group/serviceoffering"
                                    },
                                    "gx:expirationDateTime": {
                                      "@value": "2024-09-19T17:45:10",
                                      "@type": "xsd:dateTime"
                                    },
                                    "gx:containsPII": false,
                                    "gx:policy": {
                                      "@value": "This could be a policy for the instantiatedVirtualResource.",
                                      "@type": "xsd:string"
                                    },
                                    "gx:producedBy": {
                                      "@id": "https://participant.gxfs.gx4fm.org/.well-known/legalParticipant.json"
                                    },
                                    "gx:name": {
                                      "@value": "Data Resource Name",
                                      "@type": "xsd:string"
                                    },
                                    "gx:description": {
                                      "@value": "This could be the description of a data resource.",
                                      "@type": "xsd:string"
                                    }
                                  },
                                  "gx:tenantOwnedBy": {
                                    "@id": "https://participant.gxfs.gx4fm.org/.well-known/legalParticipant.json"
                                  },
                                  "gx:serviceAccessPoint": {
                                    "@id": "https://www.msg.group/serviceaccesspoint"
                                  }
                                },
                                {
                                  "@context": {
                                    "xsd": "http://www.w3.org/2001/XMLSchema#",
                                    "gx": "https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#",
                                    "rdf": "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
                                    "sh": "http://www.w3.org/ns/shacl#"
                                  },
                                  "@id": "https://www.msg.group/physicalresource",
                                  "@type": "gx:PhysicalResource",
                                  "gx:maintainedBy": {
                                    "@id": "https://participant.gxfs.gx4fm.org/.well-known/legalParticipant.json"
                                  },
                                  "gx:ownedBy": {
                                    "@id": "https://participant.gxfs.gx4fm.org/.well-known/legalParticipant.json"
                                  },
                                  "gx:manufacturedBy": {
                                    "@id": "https://participant.gxfs.gx4fm.org/.well-known/legalParticipant.json"
                                  },
                                  "gx:location": {
                                    "@value": "DE",
                                    "@type": "xsd:string"
                                  },
                                  "gx:name": {
                                    "@value": "msg PhysicalResource",
                                    "@type": "xsd:string"
                                  },
                                  "gx:description": {
                                    "@value": "This could be used to even further describe the physical resource.",
                                    "@type": "xsd:string"
                                  }
                                },
                                {
                                  "@context": {
                                    "xsd": "http://www.w3.org/2001/XMLSchema#",
                                    "gx": "https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#",
                                    "sh": "http://www.w3.org/ns/shacl#"
                                  },
                                  "@id": "https://www.msg.group/serviceaccesspoint",
                                  "@type": "gx:ServiceAccessPoint",
                                  "gx:name": {
                                    "@value": "msg edc",
                                    "@type": "xsd:string"
                                  },
                                  "gx:host": {
                                    "@value": "msg.group/edc",
                                    "@type": "xsd:string"
                                  },
                                  "gx:protocol": {
                                    "@value": "https",
                                    "@type": "xsd:string"
                                  },
                                  "gx:version": {
                                    "@value": "1.0",
                                    "@type": "xsd:string"
                                  },
                                  "gx:port": {
                                    "@value": "443",
                                    "@type": "xsd:string"
                                  },
                                  "gx:openAPI": {
                                    "@value": "https://www.msg.group/serviceaccesspoint/openapi",
                                    "@type": "xsd:string"
                                  }
                                },
                                {
                                  "@context": {
                                    "xsd": "http://www.w3.org/2001/XMLSchema#",
                                    "gx": "https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#",
                                    "rdf": "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
                                    "sh": "http://www.w3.org/ns/shacl#"
                                  },
                                  "@id": "https://www.msg.group/serviceoffering",
                                  "@type": "gx:ServiceOffering",
                                  "gx:dataAccountExport": {
                                    "@type": "gx:dataAccountExport",
                                    "gx:requestType": "API",
                                    "gx:formatType": {
                                      "@value": "application/json",
                                      "@type": "xsd:string"
                                    },
                                    "gx:accessType": "digital"
                                  },
                                  "gx:termsAndConditions": {
                                    "@type": "gx:SOTermsAndConditions",
                                    "gx:hash": {
                                      "@value": "d056db972da41899ed89cd3f93b8ccd59fc9314e2904af3455ce13d76a2ed99b",
                                      "@type": "xsd:string"
                                    },
                                    "gx:URL": {
                                      "@value": "https://www.msg.group/sotermsandconditions",
                                      "@type": "xsd:string"
                                    }
                                  },
                                  "gx:policy": {
                                    "@value": "This could be a policy for the Service Offering",
                                    "@type": "xsd:string"
                                  },
                                  "gx:providedBy": {
                                    "@id": "https://participant.gxfs.gx4fm.org/.well-known/legalParticipant.json",
                                    "@type": "gx:LegalParticipant",
                                    "gx:legalAddress": {
                                      "@type": "gx:legalAddress",
                                      "gx:countrySubdivisionCode": {
                                        "@value": "DE-BY",
                                        "@type": "xsd:string"
                                      }
                                    },
                                    "gx:legalRegistrationNumber": {
                                      "@type": "gx:legalRegistrationNumber",
                                      "gx:vatID": {
                                        "@value": "DE 129 420 400",
                                        "@type": "xsd:string"
                                      }
                                    },
                                    "gx:headquarterAddress": {
                                      "@type": "gx:legalAddress",
                                      "gx:countrySubdivisionCode": {
                                        "@value": "DE-BY",
                                        "@type": "xsd:string"
                                      }
                                    }
                                  }
                                },
                                {
                                  "@context": {
                                    "xsd": "http://www.w3.org/2001/XMLSchema#",
                                    "skos": "http://www.w3.org/2004/02/skos/core#",
                                    "trusted-cloud": "http://w3id.org/gaia-x/trusted-cloud#",
                                    "gax-core": "https://w3id.org/gaia-x/core#",
                                    "surveyonto": "http://semanticweb.org/metadatasurveyontology/",
                                    "sh": "http://www.w3.org/ns/shacl#"
                                  },
                                  "@id": "surveyonto:surveyResultDataOffering",
                                  "@type": [
                                    "surveyonto:SurveyResultDataOffering"
                                  ],
                                  "surveyonto:edc_endpoint": {
                                    "@value": "https://edcdb-1.gxfs.gx4fm.org/",
                                    "@type": "xsd:string"
                                  },
                                  "surveyonto:contract_id": {
                                    "@value": "contract_zcdkr7kqd47y0w5b4tg91w1etw",
                                    "@type": "xsd:string"
                                  },
                                  "surveyonto:belongs_to": {
                                    "@id": "did:example:survey_service_offering_zcdkr7kqd47y0w5b4tg91w1etw"
                                  },
                                  "surveyonto:survey_close_time": {
                                    "@value": "2024-04-29T12:16:17",
                                    "@type": "xsd:dateTime"
                                  },
                                  "surveyonto:survey_start_time": {
                                    "@value": "2023-03-29T12:16:17",
                                    "@type": "xsd:dateTime"
                                  }
                                }
                              ],
                              "verifiableCredentials": [
                                {
                                  "@context": [
                                    "https://www.w3.org/2018/credentials/v1",
                                    "https://w3id.org/security/suites/jws-2020/v1",
                                    "https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#"
                                  ],
                                  "type": [
                                    "VerifiableCredential"
                                  ],
                                  "id": "https://participant.gxfs.gx4fm.org/.well-known/legalParticipant.json",
                                  "issuer": "did:web:participant.gxfs.gx4fm.org",
                                  "issuanceDate": "2024-04-15T14:50:25.143Z",
                                  "credentialSubject": {
                                    "gx:legalName": "msg systems AG",
                                    "gx:headquarterAddress": {
                                      "gx:countrySubdivisionCode": "DE-BY"
                                    },
                                    "gx:legalRegistrationNumber": {
                                      "id": "https://participant.gxfs.gx4fm.org/.well-known/legalRegistrationNumber.json"
                                    },
                                    "gx:legalAddress": {
                                      "gx:countrySubdivisionCode": "DE-BY"
                                    },
                                    "type": "gx:LegalParticipant",
                                    "gx-terms-and-conditions:gaiaxTermsAndConditions": "70c1d713215f95191a11d38fe2341faed27d19e083917bc8732ca4fea4976700",
                                    "id": "https://participant.gxfs.gx4fm.org/.well-known/legalParticipant.json"
                                  },
                                  "proof": {
                                    "type": "JsonWebSignature2020",
                                    "created": "2024-04-15T14:50:26.184Z",
                                    "proofPurpose": "assertionMethod",
                                    "verificationMethod": "did:web:participant.gxfs.gx4fm.org#JWK2020-RSA",
                                    "jws": "eyJhbGciOiJQUzI1NiIsImI2NCI6ZmFsc2UsImNyaXQiOlsiYjY0Il19..tWypofwz5p8TnhAsc9ZJw6hBnwkk0ifdoO3xjOaFqTO6MNasjC1PUxCqh5FkVlWcu9wHaABoUpzYUXLP2f0RjW8jxpLD_rJmDW8tEjQrMKQKl3GkyxYvAcM73AbjEv2tZ0PVLUPP3zyxx8Ted_smsf5SeAlgQYepfRpdDe3PXtOxMnBPL1D88eofSq4iDWf1zDizpiT_eLONdCQWu-bdypJ31e986UuRyISqcTu6zc5VaJwSzTXZiPuy1owOshXSUtrTf3nwFeEwHrVHdkBYG-p6LQWmXtLYJ9X9lXAbT4KmF9o8zLkUbw5cVdHnxxVO2n3_ZqbapZnwnuSJPybc0fadZgIcXxSBn4etYvKEoOaSU1j9LeNf1P4IyrSBYWyUFFOaRFOA7RPxANhHPgV9adaablm_alpwkV5SQ_y-3-LSmFRSsWibNWTFtcv5AUo1Nvp91rEW7eCTs_WcXCb_UVea5cVjornfcznGKStCz0DG3Wx__dHG23xQubjE8yDEPj7FIZEPR7UpXfYcHoflVf_-U2s--7HvkH1Mobs3mVnVDNwMLZdK8cuUiS9e4RYKS_HxmbMFQuYtE-jR0XpopdRaHFz2QlcIkHNug_aNfKJ_F3j9AXtJbTR6FTF0HQHuvSaXVWpsRvKvfMgIR7L5YCcY6v2kQUbNPrBKg2qmifk"
                                  }
                                },
                                {
                                  "@context": [
                                    "https://www.w3.org/2018/credentials/v1",
                                    "https://w3id.org/security/suites/jws-2020/v1",
                                    "https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#",
                                    "https://schema.org/"
                                  ],
                                  "type": [
                                    "VerifiableCredential"
                                  ],
                                  "id": "https://participant.gxfs.gx4fm.org/.well-known/legalRegistrationNumber.json",
                                  "issuer": "did:web:registrationnumber.notary.lab.gaia-x.eu:main",
                                  "issuanceDate": "2024-04-15T14:48:33.346+00:00",
                                  "credentialSubject": {
                                    "id": "https://participant.gxfs.gx4fm.org/.well-known/legalRegistrationNumber.json",
                                    "type": "gx:legalRegistrationNumber",
                                    "gx:vatID": "DE129420400",
                                    "gx:vatID-countryCode": "DE"
                                  },
                                  "evidence": [
                                    {
                                      "gx:evidenceOf": "VAT_ID",
                                      "gx:evidenceURL": "http://ec.europa.eu/taxation_customs/vies/services/checkVatService",
                                      "gx:executionDate": "2024-04-15T14:48:33.344+00:00"
                                    }
                                  ],
                                  "proof": {
                                    "type": "JsonWebSignature2020",
                                    "created": "2024-04-15T14:48:33.347+00:00",
                                    "proofPurpose": "assertionMethod",
                                    "verificationMethod": "did:web:registrationnumber.notary.lab.gaia-x.eu:main#X509-JWK2020",
                                    "@context": [
                                      "https://www.w3.org/2018/credentials/v1",
                                      "https://w3id.org/security/suites/jws-2020/v1"
                                    ],
                                    "jws": "eyJhbGciOiJQUzI1NiIsImI2NCI6ZmFsc2UsImNyaXQiOlsiYjY0Il19..SPYHZ_Txc7Sf8OvMgfXUJfuGx4eSHyWxTdxLJoQYpH337DZxEMXGKtyKXiKyuYgEV0MDVr5ewrFcTgVSAxJwRwJfZb1mIMswwLEMzkLIpgiDykRRQYLjls38ZlySTw6FI-y7Fja2hWr60an_7y8W1odab9akaIsqOP3bbGY8j8DvbgacKwVOuoXu05LySsU9369R2J9k2nz9Dtz0AB4zaXR3cIkfKWb0NGQFFKvsO_ECetCLCzjzhNkN6EI15YXIHK8VDF03KA7vPM2u8dljKgfxdApFsAdPf-GpCsh88DDSomIH87IiD1FTvip0B60Xi1GNajKtfFXkQ1A8xP7EbQ"
                                  }
                                },
                                {
                                  "@context": [
                                    "https://www.w3.org/2018/credentials/v1",
                                    "https://w3id.org/security/suites/jws-2020/v1",
                                    "https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#"
                                  ],
                                  "type": "VerifiableCredential",
                                  "issuanceDate": "2024-04-15T14:50:25.144Z",
                                  "credentialSubject": {
                                    "gx:termsAndConditions": "The PARTICIPANT signing the Self-Description agrees as follows:\\n- to update its descriptions about any changes, be it technical, organizational, or legal - especially but not limited to contractual in regards to the indicated attributes present in the descriptions.\\n\\nThe keypair used to sign Verifiable Credentials will be revoked where Gaia-X Association becomes aware of any inaccurate statements in regards to the claims which result in a non-compliance with the Trust Framework and policy rules defined in the Policy Rules and Labelling Document (PRLD).",
                                    "type": "gx:GaiaXTermsAndConditions",
                                    "id": "https://participant.gxfs.gx4fm.org/.well-known/termsAndConditions.json"
                                  },
                                  "issuer": "did:web:participant.gxfs.gx4fm.org",
                                  "id": "https://participant.gxfs.gx4fm.org/.well-known/termsAndConditions.json",
                                  "proof": {
                                    "type": "JsonWebSignature2020",
                                    "created": "2024-04-15T14:50:26.217Z",
                                    "proofPurpose": "assertionMethod",
                                    "verificationMethod": "did:web:participant.gxfs.gx4fm.org#JWK2020-RSA",
                                    "jws": "eyJhbGciOiJQUzI1NiIsImI2NCI6ZmFsc2UsImNyaXQiOlsiYjY0Il19..cNmm1Fxnoj8fg61Wm-PIg4w8nFMt0SnzWErpZiZP1hg62Db01lCY7qk8c5G33KH8xeGJwOFjjVMEDzbGXWcZaFVaYDyhRsTkI4u6jhWRCxefrg0Kz2SgnZvBf5EWTKvMXNTFm3pJT7ePLZoHYXs8iCLcUhcJAVJdymMp0gdrAlilqrJtT4SZaUgiAnBM87fo8fq-edRhXKg7S12uR_eiY0Md6tpvf1RFvFswCGlzRNcq-NPVY_sR4-ngo2wNdaM7fRkwMWL3MoGpCItNez9_5foWNQOtVaHNiCfQ9gT6olAksWkYQvXHSZrwe3pq2ieQmL4r2OxuavO4WEo-4Uz-asASV1TRlWw2HyTN_eB2HgUDI-mXixN4weVceRXMUmKlf_Q16MX1TvcPev0mrwqcnIxb4feyftCn9lXNglKrYiOkeazjLVF1uyf36jjuTcLHOOoq37tn8bim66Y90mdGLDEdwH61RKYJg6IjtSOaB9iMcQ4PoEZzm4UwC_R7axOQV8xUJvUEeVfi0_wv--SI4uuuzgiWgtujMF37_R9lLfz8wVFe_DxdnJNSbxxP4XaEC7xcnNXYHFdi1HRQ49d9Zk-1B7Yb_ZURjoyzbaH8yJgO1ndAGrFj1RllA74pH2CTpaJYis40IVbeHBC_gsXa_dQJRMW0XI7pI18M8rkezUQ"
                                  }
                                }
                              ]
                            }
                            """)
            }))
    @APIResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = VerifiablePresentation.class)))
    public VerifiablePresentation initiateVCProcessing(final Payload payload) {
        log.info("Initiating VC processing");
        log.debug("Payload: {}", payload);
        VerifiablePresentation result = verifiableCredentialsProcessor.process(payload.getClaims(), payload.getVerifiableCredentials());
        log.info("VC processing completed");
        return result;
    }
}