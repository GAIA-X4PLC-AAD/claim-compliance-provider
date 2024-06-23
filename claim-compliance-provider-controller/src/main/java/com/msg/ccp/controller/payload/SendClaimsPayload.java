package com.msg.ccp.controller.payload;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.Set;

public record SendClaimsPayload(@NotNull Set<Map<String, Object>> claims,
                                @NotNull @NotEmpty Set<VerifiableCredential> verifiableCredentials) {

    /**
     * The value of this can be taken from the build runs. Just search for "Claims: " and copy the value without brackets
     * "[" and "]" in here.
     * It is used both in SendClaimsPayload and GenerateClaimsPayload.
     * !!! Dont forget to update the ID of the domain specific class in SendClaimsPayload.EXAMPLE_PAYLOAD ! IT MUST MATCH THE ID OF THE DataResource !!!
     */
    public static final String LIST_OF_CLAIMS = """
            {
              "@context": {
                "xsd": "http://www.w3.org/2001/XMLSchema#",
                "gx": "https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#",
                "rdf": "http://www.w3.org/1999/02/22-rdf-syntax-ns#"
              },
              "@id": "https://www.gaia-x4plcaad.info/claims/service-offering/daa3d4dc-f170-44b5-bae2-fb3fad07c670",
              "@type": "gx:ServiceOffering",
              "gx:dataAccountExport": {
                "gx:requestType": "API",
                "gx:formatType": {
                  "@value": "application/json",
                  "@type": "xsd:string"
                },
                "gx:accessType": "digital"
              },
              "gx:termsAndConditions": {
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
                "@value": "package access_control default allow = false  allow { input.method == \\"GET\\" input.path == /public } allow { input.method == \\"POST\\"     input.path == \\"/private\\"     input.user.role == \\"admin\\" }",
                "@type": "xsd:string"
              },
              "gx:providedBy": {
                "id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json"
              }
            },
            {
              "@context": {
                "xsd": "http://www.w3.org/2001/XMLSchema#",
                "gx": "https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#",
                "rdf": "http://www.w3.org/1999/02/22-rdf-syntax-ns#"
              },
              "@id": "https://www.gaia-x4plcaad.info/claims/physical-resource/daa3d4dc-f170-44b5-bae2-fb3fad07c670",
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
              }
            },
            {
              "@context": {
                "xsd": "http://www.w3.org/2001/XMLSchema#",
                "gx": "https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#"
              },
              "@id": "https://www.gaia-x4plcaad.info/claims/service-access-point/daa3d4dc-f170-44b5-bae2-fb3fad07c670",
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
            },
            {
              "@context": {
                "xsd": "http://www.w3.org/2001/XMLSchema#",
                "gx": "https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#",
                "rdf": "http://www.w3.org/1999/02/22-rdf-syntax-ns#"
              },
              "@id": "https://www.gaia-x4plcaad.info/claims/data-resource/daa3d4dc-f170-44b5-bae2-fb3fad07c670",
              "@type": "gx:DataResource",
              "gx:copyrightOwnedBy": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json",
              "gx:license": "Apache-2.0",
              "gx:obsoleteDateTime": {
                "@value": "2025-03-23T13:53:38",
                "@type": "xsd:dateTime"
              },
              "gx:exposedThrough": {
                "@id": "https://www.gaia-x4plcaad.info/claims/service-offering/daa3d4dc-f170-44b5-bae2-fb3fad07c670"
              },
              "gx:expirationDateTime": {
                "@value": "2025-03-23T13:53:38",
                "@type": "xsd:dateTime"
              },
              "gx:containsPII": false,
              "gx:policy": {
                "@value": "package access_control  default allow = false  allow {     input.method == \\"GET\\"     input.path == \\"/public\\" }  allow {     input.method == \\"POST\\"     input.path == \\"/private\\"     input.user.role == \\"admin\\" }",
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
                "rdf": "http://www.w3.org/1999/02/22-rdf-syntax-ns#"
              },
              "@id": "https://www.gaia-x4plcaad.info/claims/virtual-resource/daa3d4dc-f170-44b5-bae2-fb3fad07c670",
              "@type": "gx:InstantiatedVirtualResource",
              "gx:maintainedBy": {
                "@id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json"
              },
              "gx:hostedOn": {
                "@id": "https://www.gaia-x4plcaad.info/claims/physical-resource/daa3d4dc-f170-44b5-bae2-fb3fad07c670",
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
                }
              },
              "gx:instanceOf": {
                "@id": "https://www.gaia-x4plcaad.info/claims/data-resource/daa3d4dc-f170-44b5-bae2-fb3fad07c670",
                "@type": "gx:DataResource",
                "gx:copyrightOwnedBy": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json",
                "gx:license": "Apache-2.0",
                "gx:obsoleteDateTime": {
                  "@value": "2025-03-23T13:53:38",
                  "@type": "xsd:dateTime"
                },
                "gx:exposedThrough": {
                  "@id": "https://www.gaia-x4plcaad.info/claims/service-offering/daa3d4dc-f170-44b5-bae2-fb3fad07c670"
                },
                "gx:expirationDateTime": {
                  "@value": "2025-03-23T13:53:38",
                  "@type": "xsd:dateTime"
                },
                "gx:containsPII": false,
                "gx:policy": {
                  "@value": "package access_control  default allow = false  allow {     input.method == \\"GET\\"     input.path == \\"/public\\" }  allow {     input.method == \\"POST\\"     input.path == \\"/private\\"     input.user.role == \\"admin\\" }",
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
                "@id": "https://www.gaia-x4plcaad.info/claims/service-access-point/daa3d4dc-f170-44b5-bae2-fb3fad07c670"
              }
            }
            """;
    public static final String EXAMPLE_PAYLOAD = """
            {
              "claims": [
                """ + LIST_OF_CLAIMS + """
                ,
                {
                  "@context": {
                    "xsd": "http://www.w3.org/2001/XMLSchema#",
                    "skos": "http://www.w3.org/2004/02/skos/core#",
                    "trusted-cloud": "http://w3id.org/gaia-x/trusted-cloud#",
                    "gax-core": "https://w3id.org/gaia-x/core#",
                    "surveyonto": "http://semanticweb.org/metadatasurveyontology/"
                  },
                  "@id": "https://www.gaia-x4plcaad.info/claims/service-offering/daa3d4dc-f170-44b5-bae2-fb3fad07c670",
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
                  "id": "https://wizard.lab.gaia-x.eu/api/credentials/Lg6BMfUw47oH9asdSS8hsj8igGjaaWdzswP5orD3nWVLsjY4raurGUPH9HsM1YYoiBNzQ5E1xLMiZBSJm3ADRPvMgW6NpLKhTrYFWviCK4vte3N4WWDTqFd3eEDSfJ2eifkSSWXLpgDDzsLatJKD7NY1dLqrN22WtS1DBhhJNnG8dyYxC8p5WTfuJ41yVrJV5tXnmho4HYBki9g3qZPQyrceHudWgbhA5usYvHgtnhvSFoS47vmPGHrZJ4g6icgZctqYp397a9smSrtCBzAPeqFzPaeVWsnmkAryqKbxstPjesuApVEnNGiqFZayV9a295wBhyaQGYR3YKHG9u6CCgE4F6JbYmaXwC2satPhi3A7yZG4HkJjj66JDyc1K8PA4YcymdFYWxAc5N32HAGMit3KiZpPPcR5jJC2PFa7BT3xRLtQ127Z12gAbhntBtsDBGBXNgQ5he249rqo17yJBdw7iTd7Y2KAedbLm7vnYNUeewiafwEmii9TrFD77uEqXCvXVeGaeAHsv4Zgnpb1zHPG3NztaVHM2hcbC3DwqtdtnYiYDAoPRHWBw4cpWzvMWHpU6AD5kfY2q6qQrPTFfakLfrUL1aauoX8yL2KQPt62ox9rz4sxkeZt6LFmxoXU8B75FUnnXvU25DZ9xQXh9q756ZRw2H1jpykWsmnbkGpA8QJ9QXZiMn9E9EuCYr135jFPTCzq5N6LeUyNRFdx8VaT6B4MM1dZ5t9fqFw3saVSp5NB2MZfyEaKMYhyakj1PNuMUg1SeZ3iHpfoQfRPt2syojbdm7Sqai3aX2vgtnBUPdopMUbd3xANQ9nn23bPR9nXjXruwRMgH2sis9oXkydasWy58FnuU9WdP6QQpKjjorbbqLCthMFeWdFJpgLYAjUxYfm7iTMA2MqBmEsK1PErm6rion13gHAbnPFQmSGgmWecMc2DiiX5GxsKqXYk3FsWvggfa7aoMLGAi6WWeixp7Po6riwEW9gsB75aNXZUMjymNLnFLqqAfGNYZjhZgjqJhCWQgGK9zfGBzmts2c4ejhZN1KjVt7bYQHgmxz8NyjvxRcCFsWfGGDUW5ktPDGQ?uid=6f348c02-1183-4527-9f50-d5fa67e01c27",
                  "issuer": "did:web:wizard.lab.gaia-x.eu:api:credentials:Lg6BMfUw47oH9asdSS8hsj8igGjaaWdzswP5orD3nWVLsjY4raurGUPH9HsM1YYoiBNzQ5E1xLMiZBSJm3ADRPvMgW6NpLKhTrYFWviCK4vte3N4WWDTqFd3eEDSfJ2eifkSSWXLpgDDzsLatJKD7NY1dLqrN22WtS1DBhhJNnG8dyYxC8p5WTfuJ41yVrJV5tXnmho4HYBki9g3qZPQyrceHudWgbhA5usYvHgtnhvSFoS47vmPGHrZJ4g6icgZctqYp397a9smSrtCBzAPeqFzPaeVWsnmkAryqKbxstPjesuApVEnNGiqFZayV9a295wBhyaQGYR3YKHG9u6CCgE4F6JbYmaXwC2satPhi3A7yZG4HkJjj66JDyc1K8PA4YcymdFYWxAc5N32HAGMit3KiZpPPcR5jJC2PFa7BT3xRLtQ127Z12gAbhntBtsDBGBXNgQ5he249rqo17yJBdw7iTd7Y2KAedbLm7vnYNUeewiafwEmii9TrFD77uEqXCvXVeGaeAHsv4Zgnpb1zHPG3NztaVHM2hcbC3DwqtdtnYiYDAoPRHWBw4cpWzvMWHpU6AD5kfY2q6qQrPTFfakLfrUL1aauoX8yL2KQPt62ox9rz4sxkeZt6LFmxoXU8B75FUnnXvU25DZ9xQXh9q756ZRw2H1jpykWsmnbkGpA8QJ9QXZiMn9E9EuCYr135jFPTCzq5N6LeUyNRFdx8VaT6B4MM1dZ5t9fqFw3saVSp5NB2MZfyEaKMYhyakj1PNuMUg1SeZ3iHpfoQfRPt2syojbdm7Sqai3aX2vgtnBUPdopMUbd3xANQ9nn23bPR9nXjXruwRMgH2sis9oXkydasWy58FnuU9WdP6QQpKjjorbbqLCthMFeWdFJpgLYAjUxYfm7iTMA2MqBmEsK1PErm6rion13gHAbnPFQmSGgmWecMc2DiiX5GxsKqXYk3FsWvggfa7aoMLGAi6WWeixp7Po6riwEW9gsB75aNXZUMjymNLnFLqqAfGNYZjhZgjqJhCWQgGK9zfGBzmts2c4ejhZN1KjVt7bYQHgmxz8NyjvxRcCFsWfGGDUW5ktPDGQ",
                  "issuanceDate": "2024-06-11T12:06:22.588Z",
                  "credentialSubject": {
                    "gx:legalName": "msg systems ag",
                    "gx:headquarterAddress": {
                      "gx:countrySubdivisionCode": "DE-BY"
                    },
                    "gx:legalRegistrationNumber": {
                      "id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalRegistration.json"
                    },
                    "gx:legalAddress": {
                      "gx:countrySubdivisionCode": "DE-BY"
                    },
                    "type": "gx:LegalParticipant",
                    "gx-terms-and-conditions:gaiaxTermsAndConditions": "70c1d713215f95191a11d38fe2341faed27d19e083917bc8732ca4fea4976700",
                    "id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json"
                  },
                  "proof": {
                    "type": "JsonWebSignature2020",
                    "created": "2024-06-21T23:06:30.694+02:00",
                    "proofPurpose": "assertionMethod",
                    "verificationMethod": "did:web:wizard.lab.gaia-x.eu:api:credentials:Lg6BMfUw47oH9asdSS8hsj8igGjaaWdzswP5orD3nWVLsjY4raurGUPH9HsM1YYoiBNzQ5E1xLMiZBSJm3ADRPvMgW6NpLKhTrYFWviCK4vte3N4WWDTqFd3eEDSfJ2eifkSSWXLpgDDzsLatJKD7NY1dLqrN22WtS1DBhhJNnG8dyYxC8p5WTfuJ41yVrJV5tXnmho4HYBki9g3qZPQyrceHudWgbhA5usYvHgtnhvSFoS47vmPGHrZJ4g6icgZctqYp397a9smSrtCBzAPeqFzPaeVWsnmkAryqKbxstPjesuApVEnNGiqFZayV9a295wBhyaQGYR3YKHG9u6CCgE4F6JbYmaXwC2satPhi3A7yZG4HkJjj66JDyc1K8PA4YcymdFYWxAc5N32HAGMit3KiZpPPcR5jJC2PFa7BT3xRLtQ127Z12gAbhntBtsDBGBXNgQ5he249rqo17yJBdw7iTd7Y2KAedbLm7vnYNUeewiafwEmii9TrFD77uEqXCvXVeGaeAHsv4Zgnpb1zHPG3NztaVHM2hcbC3DwqtdtnYiYDAoPRHWBw4cpWzvMWHpU6AD5kfY2q6qQrPTFfakLfrUL1aauoX8yL2KQPt62ox9rz4sxkeZt6LFmxoXU8B75FUnnXvU25DZ9xQXh9q756ZRw2H1jpykWsmnbkGpA8QJ9QXZiMn9E9EuCYr135jFPTCzq5N6LeUyNRFdx8VaT6B4MM1dZ5t9fqFw3saVSp5NB2MZfyEaKMYhyakj1PNuMUg1SeZ3iHpfoQfRPt2syojbdm7Sqai3aX2vgtnBUPdopMUbd3xANQ9nn23bPR9nXjXruwRMgH2sis9oXkydasWy58FnuU9WdP6QQpKjjorbbqLCthMFeWdFJpgLYAjUxYfm7iTMA2MqBmEsK1PErm6rion13gHAbnPFQmSGgmWecMc2DiiX5GxsKqXYk3FsWvggfa7aoMLGAi6WWeixp7Po6riwEW9gsB75aNXZUMjymNLnFLqqAfGNYZjhZgjqJhCWQgGK9zfGBzmts2c4ejhZN1KjVt7bYQHgmxz8NyjvxRcCFsWfGGDUW5ktPDGQ#JWK2020",
                    "jws": "eyJhbGciOiJQUzI1NiIsImI2NCI6ZmFsc2UsImNyaXQiOlsiYjY0Il19..skHciff5ZpmVeQiFSxHiQaHasIs9d64fcO9e-tcovkGInM-1R8RjUlXGxWjC8qz3viWInEa3PSt8H8zqJ2qFnEm88Lh_KjY4Q1j6MQu5nPvWtWoCwbV4fqVdSdEigA-2Vr24bZz90eUkAaY1iiBcnE40LxmUS7m0iORAQT4puG9Eqknw306_bEhEHLSGd5U9usZHE-fHVUFfuFrJwOdcJTof2rOURRGDVq11_-okdduPp7dFgB63FGbSI8pjTfIokMKxxJvdcajHN9wts7vSutB_50q1itFrzqdXZm-6HwYwxDs_dAIxUwGJni5pfnRLeOzifRPDELNliu3zO0jSeO7weofaVLnZ5eGpfeNuW9CbSe1_2kRYOy5Xisa-qaMpR19sVkR3RicFOX3zN7D5L2SJD2zvqa2gtCIz8_CFY0n-gKPLp2PQTDy3CcFsk9gSOBUQZivhIUqOP4Rdao3x2obxj0RRMgdCif5kUrdVBnt3aUlw-eiWRESDT_-A0m_-U3TlJ_Vh0L8w2sYQH2APjmzXTAC4xwCUX_d8SToHjHsj7S7kMW08F7YshVb26nMVwFRN0-w42CycPpUZCZnZbKIm6Wr7yKhJQhS-YTFomGDnKE08CwHwbGS12S3Q9_p0aKEidDS_dZT4vNf3BhylsWyr0pk2xXxX_x7fLrgw3EA"
                  }
                },
                {
                  "@context": [
                    "https://www.w3.org/2018/credentials/v1",
                    "https://w3id.org/security/suites/jws-2020/v1"
                  ],
                  "type": [
                    "VerifiableCredential"
                  ],
                  "id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalRegistration.json",
                  "issuer": "did:web:registration.lab.gaia-x.eu:v1-staging",
                  "issuanceDate": "2024-06-11T10:41:04.368Z",
                  "credentialSubject": {
                    "@context": "https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#",
                    "type": "gx:legalRegistrationNumber",
                    "id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalRegistration.json",
                    "gx:vatID": "DE129420400",
                    "gx:vatID-countryCode": "DE"
                  },
                  "evidence": [
                    {
                      "gx:evidenceURL": "http://ec.europa.eu/taxation_customs/vies/services/checkVatService",
                      "gx:executionDate": "2024-06-11T10:41:04.368Z",
                      "gx:evidenceOf": "gx:vatID"
                    }
                  ],
                  "proof": {
                    "type": "JsonWebSignature2020",
                    "created": "2024-06-11T10:41:04.382Z",
                    "proofPurpose": "assertionMethod",
                    "verificationMethod": "did:web:registration.lab.gaia-x.eu:v1-staging#X509-JWK2020",
                    "jws": "eyJhbGciOiJQUzI1NiIsImI2NCI6ZmFsc2UsImNyaXQiOlsiYjY0Il19..BZLcUJsjZdTeEvgnGjuGXJ2JZCPhdvfbKQjmPgLA0sl93Nf-d79pZkvzg_PHEYRL9REIqysPb2jtEVc_LI17YBc0ZTvHNZw9T9PjfervYFw6pXqID1jXb_lbWabIP1YlxR5AA5uBXoENLuT-GFUyQ7hwMTPakQxvl5pR3jiLJT7brIbtbhoyqX7_nYVpJPxFVcufL9mMkxwlX1x5j9HoVPNCjexlCj0mXq4gDT583SEXlGkbIMUaD9XJlZbp2xyeBIb_luF2LyS2cmbSDjONbppQvrVwnMuyRuexbtc58EBlh7UvmYHemij6JxtYWjBR8axnRBz-4_DviYiaJ3pmJg"
                  }
                },
                {
                  "@context": [
                    "https://www.w3.org/2018/credentials/v1",
                    "https://w3id.org/security/suites/jws-2020/v1",
                    "https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#"
                  ],
                  "type": "VerifiableCredential",
                  "issuanceDate": "2024-06-21T21:15:30.081Z",
                  "credentialSubject": {
                    "gx:termsAndConditions": "The PARTICIPANT signing the Self-Description agrees as follows:\\n- to update its descriptions about any changes, be it technical, organizational, or legal - especially but not limited to contractual in regards to the indicated attributes present in the descriptions.\\n\\nThe keypair used to sign Verifiable Credentials will be revoked where Gaia-X Association becomes aware of any inaccurate statements in regards to the claims which result in a non-compliance with the Trust Framework and policy rules defined in the Policy Rules and Labelling Document (PRLD).",
                    "type": "gx:GaiaXTermsAndConditions",
                    "id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/termsAndConditions.json"
                  },
                  "issuer": "did:web:participant.gxfs.gx4fm.org:msg-systems-ag",
                  "id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/termsAndConditions.json",
                  "proof": {
                    "type": "JsonWebSignature2020",
                    "created": "2024-06-21T23:15:31.464+02:00",
                    "proofPurpose": "assertionMethod",
                    "verificationMethod": "did:web:participant.gxfs.gx4fm.org:msg-systems-ag#JWK2020-RSA",
                    "jws": "eyJhbGciOiJQUzI1NiIsImI2NCI6ZmFsc2UsImNyaXQiOlsiYjY0Il19..MUqvGwsKRwS4SGxVfIS8iMoQwHiMtQlcPeP-Hm2ywiNFHTnI3IY9gon94Mib4jQJ3ExG1YISAjtCH8iidQurlEzlKz5v3frKl1gFS1p-3IWWrIGBChZ_XUZZUdPq0EQ6B-nZdaewwS8vfCtP82IFmXz-jaA0SOAxxuGXxa6xoHt3WbzAEQtEeItqS7my4BNWlSaeMKr-JCWbih-wPSy3ZpXjKwdYOo972yZYj-YP6OVtJe0iTnpazHrGjnkNZmi8tzlTuE691pQH2iM8CG5jQqG_ue38Z7YqPpUghWvpZYgJ6O8yeSqrpeJ8NrmN0NdN5wJRySOuOwzhqSuwsNP4X6yWBEo9bkEJAok0bN-9X7RQm5INUkRxk68_BHZga6lsvvLz2pqgModf2btXRHJhqUYF7Z2BsikcwzhPTnyTAms9q1AovJIx3ypDlNVOUMS24uLqBM713Z17a9qxEUC94WWpXjSXCVLe4FK-26TenYYKdWue3vtaoWDonPSnMN90XmQirvRv0YcLvBQNZ8JNFUe7Et22p8PZUeVcu-YjULRWrGgdk-kWQgCsEQraM13gHNzyKxHUgDG_pco3QIgNsbbOIdEdv8CS4mzm_NApLufRMOUIsjBf7nOe8RewebmgwPeXXqdxLo9xaBb77dwj45Cj6Y2Wjr4O4Cbp9PI9VJ8"
                  }
                }
              ]
            }
            """;

    public static final String EXAMPLE_RESPONSE_409 = """
            {
              "message": "The signature of the document with ID did:web:sd-creator.gxfs.gx4fm.org:id-documents:d3ebe5f7d37e44d2b2d425fb9daaa4cd cannot be validated, please check the document has not been tampered",
              "httpError": "Conflict",
              "exceptionMessage": "The signature of the document with ID did:web:sd-creator.gxfs.gx4fm.org:id-documents:d3ebeaaa4cd cannot be validated, please check the document has not been tampered",
              "statusCode": 409,
              "verifiablePresentationId": "did:web:sd-creator.gxfs.gx4fm.org:id-documents:fb6f1ed484fb48e6b7b25453a0ac39d7"
            }
            """;

    public static final String EXAMPLE_RESPONSE_422 = """
            {
              "message": "Value does not have shape gx:DataAccountExportShape",
              "httpError": "verification_error",
              "exceptionMessage": "Unprocessable Entity",
              "statusCode": 422,
              "verifiablePresentationId": "did:web:sd-creator.gxfs.gx4fm.org:id-documents:fb6f1ed484fb48e6b7b25453a0ac39d7"
            }
            """;

    public static final String EXAMPLE_RESPONSE_500 = """
            {
              "message": "NullPointerException",
              "httpError": "Internal server error",
              "exceptionMessage": "null",
              "statusCode": 500,
              "verifiablePresentationId": "unknown"
            }
            """;
}
