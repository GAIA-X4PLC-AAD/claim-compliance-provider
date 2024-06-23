package com.msg.ccp.controller.payload;

import jakarta.validation.constraints.NotBlank;

public record GenerateClaimsPayload(@NotBlank String identifierPrefix, @NotBlank String legalParticipantId,
                                    @NotBlank String physicalResourceLegalParticipantId) {

    public static final String EXAMPLE_PAYLOAD = """
            {
              "identifierPrefix": "https://www.gaia-x4plcaad.info/claims",
              "legalParticipantId": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json",
              "physicalResourceLegalParticipantId": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json"
            }
            """;

    public static final String EXAMPLE_RESPONSE = """
            [
                {
                    "@context": {
                        "xsd": "http://www.w3.org/2001/XMLSchema#",
                        "gx": "https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#",
                        "rdf": "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
                        "sh": "http://www.w3.org/ns/shacl#"
                    },
                    "@id": "https://www.gaia-x4plcaad.info/claims/physical-resource/894d9abe-a83c-4f8f-b690-9c86ea515674",
                    "@type": "gx:PhysicalResource",
                    "gx:maintainedBy": {
                        "@id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json"
                    },
                    "gx:ownedBy": {
                        "@id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json"
                    },
                    "gx:manufacturedBy": {
                        "@id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json"
                    },
                    "gx:location": {
                        "@value": "DE",
                        "@type": "xsd:string"
                    },
                    "gx:name": {
                        "@value": "Generated PhysicalResource",
                        "@type": "xsd:string"
                    },
                    "gx:description": {
                        "@value": "Generated PhysicalResource Description.",
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
                    "@id": "https://www.gaia-x4plcaad.info/claims/service-offering/894d9abe-a83c-4f8f-b690-9c86ea515674",
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
                        "@value": "package access_control default allow = false  allow { input.method == GET input.path == /public } allow { input.method == \\\\\\"POST\\\\\\"     input.path == \\\\\\"/private\\\\\\"     input.user.role == \\\\\\"admin\\\\\\" }",
                        "@type": "xsd:string"
                    },
                    "gx:providedBy": {
                        "@id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json",
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
                                "@value": "DE129420400",
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
                        "gx": "https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#",
                        "rdf": "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
                        "sh": "http://www.w3.org/ns/shacl#"
                    },
                    "@id": "https://www.gaia-x4plcaad.info/claims/data-resource/894d9abe-a83c-4f8f-b690-9c86ea515674",
                    "@type": "gx:DataResource",
                    "gx:copyrightOwnedBy": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json",
                    "gx:license": "Apache-2.0",
                    "gx:obsoleteDateTime": {
                        "@value": "2025-03-21T19:22:39.3797256Z",
                        "@type": "xsd:dateTime"
                    },
                    "gx:exposedThrough": {
                        "@id": "https://www.gaia-x4plcaad.info/claims/service-offering/894d9abe-a83c-4f8f-b690-9c86ea515674"
                    },
                    "gx:expirationDateTime": {
                        "@value": "{expirationDateTime",
                        "@type": "xsd:dateTime"
                    },
                    "gx:containsPII": false,
                    "gx:policy": {
                        "@value": "package access_control  default allow = false  allow {     input.method == \\\\\\"GET\\\\\\"     input.path == \\\\\\"/public\\\\\\" }  allow {     input.method == \\\\\\"POST\\\\\\"     input.path == \\\\\\"/private\\\\\\"     input.user.role == \\\\\\"admin\\\\\\" }",
                        "@type": "xsd:string"
                    },
                    "gx:producedBy": {
                        "@id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json"
                    },
                    "gx:name": {
                        "@value": "Generated Data Resource",
                        "@type": "xsd:string"
                    },
                    "gx:description": {
                        "@value": "Generated description.",
                        "@type": "xsd:string"
                    }
                },
                {
                    "@context": {
                        "gx": "https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#",
                        "rdf": "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
                        "sh": "http://www.w3.org/ns/shacl#"
                    },
                    "@id": "https://www.gaia-x4plcaad.info/claims/virtual-resource/894d9abe-a83c-4f8f-b690-9c86ea515674",
                    "@type": "gx:InstantiatedVirtualResource",
                    "gx:maintainedBy": {
                        "@id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json"
                    },
                    "gx:hostedOn": {
                        "@id": "https://www.gaia-x4plcaad.info/claims/physical-resource/894d9abe-a83c-4f8f-b690-9c86ea515674",
                        "@type": "gx:PhysicalResource",
                        "gx:maintainedBy": {
                            "@id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json"
                        },
                        "gx:ownedBy": {
                            "@id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json"
                        },
                        "gx:manufacturedBy": {
                            "@id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json"
                        },
                        "gx:location": {
                            "@value": "DE",
                            "@type": "xsd:string"
                        },
                        "gx:name": {
                            "@value": "Generated PhysicalResource",
                            "@type": "xsd:string"
                        },
                        "gx:description": {
                            "@value": "Generated PhysicalResource Description",
                            "@type": "xsd:string"
                        }
                    },
                    "gx:instanceOf": {
                        "@id": "https://www.gaia-x4plcaad.info/claims/data-resource/894d9abe-a83c-4f8f-b690-9c86ea515674",
                        "@type": "gx:DataResource",
                        "gx:copyrightOwnedBy": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json",
                        "gx:license": "Apache-2.0",
                        "gx:obsoleteDateTime": {
                            "@value": "2025-03-21T19:22:39.3797256Z",
                            "@type": "xsd:dateTime"
                        },
                        "gx:exposedThrough": {
                            "@id": "{serviceOfferingId"
                        },
                        "gx:expirationDateTime": {
                            "@value": "2025-03-21T19:22:39.3797256Z",
                            "@type": "xsd:dateTime"
                        },
                        "gx:containsPII": false,
                        "gx:policy": {
                            "@value": "package access_control  default allow = false  allow {     input.method == \\\\\\"GET\\\\\\"     input.path == \\\\\\"/public\\\\\\" }  allow {     input.method == \\\\\\"POST\\\\\\"     input.path == \\\\\\"/private\\\\\\"     input.user.role == \\\\\\"admin\\\\\\" }",
                            "@type": "xsd:string"
                        },
                        "gx:producedBy": {
                            "@id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json"
                        },
                        "gx:name": {
                            "@value": "Generated Data Resource Name",
                            "@type": "xsd:string"
                        },
                        "gx:description": {
                            "@value": "Generated Data Resource Description.",
                            "@type": "xsd:string"
                        }
                    },
                    "gx:tenantOwnedBy": {
                        "@id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json"
                    },
                    "gx:serviceAccessPoint": {
                        "@id": "https://www.gaia-x4plcaad.info/claims/service-access-point/894d9abe-a83c-4f8f-b690-9c86ea515674"
                    }
                },
                {
                    "@context": {
                        "xsd": "http://www.w3.org/2001/XMLSchema#",
                        "gx": "https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#",
                        "sh": "http://www.w3.org/ns/shacl#"
                    },
                    "@id": "https://www.gaia-x4plcaad.info/claims/service-access-point/894d9abe-a83c-4f8f-b690-9c86ea515674",
                    "@type": "gx:ServiceAccessPoint",
                    "gx:name": {
                        "@value": "Provider EDC",
                        "@type": "xsd:string"
                    },
                    "gx:host": {
                        "@value": "edcdb-pr.gxfs.gx4fm.org/",
                        "@type": "xsd:string"
                    },
                    "gx:protocol": {
                        "@value": "https",
                        "@type": "xsd:string"
                    },
                    "gx:version": {
                        "@value": "0.2.1",
                        "@type": "xsd:string"
                    },
                    "gx:port": {
                        "@value": "443",
                        "@type": "xsd:string"
                    },
                    "gx:openAPI": {
                        "@value": "https://app.swaggerhub.com/apis/eclipse-edc-bot/management-api/0.2.1",
                        "@type": "xsd:string"
                    }
                }
            ]
            """;
}