package com.msg.ccp.controller.payload;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.Set;

public record SendClaimsPayload(@NotNull @NotEmpty Set<@Valid Map<String, Object>> claims,
                                @NotNull @NotEmpty Set<@Valid VerifiableCredential> verifiableCredentials) {

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
                  "gx": "https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#"
                },
                "@id": "https://www.gaia-x4plcaad.info/claims/service-access-point/25929d5f-a577-475c-aab8-aa05c65e8d1c",
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
                "@id": "https://www.gaia-x4plcaad.info/claims/service-offering/25929d5f-a577-475c-aab8-aa05c65e8d1c",
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
                  "gx": "https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#",
                  "rdf": "http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                },
                "@id": "https://www.gaia-x4plcaad.info/claims/virtual-resource/25929d5f-a577-475c-aab8-aa05c65e8d1c",
                "@type": "gx:InstantiatedVirtualResource",
                "gx:maintainedBy": {
                  "id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json"
                },
                "gx:hostedOn": {
                  "id": "https://www.gaia-x4plcaad.info/claims/physical-resource/25929d5f-a577-475c-aab8-aa05c65e8d1c"
                },
                "gx:instanceOf": {
                  "id": "https://www.gaia-x4plcaad.info/claims/data-resource/25929d5f-a577-475c-aab8-aa05c65e8d1c"
                },
                "gx:tenantOwnedBy": {
                  "id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json"
                },
                "gx:serviceAccessPoint": {
                  "id": "https://www.gaia-x4plcaad.info/claims/service-access-point/25929d5f-a577-475c-aab8-aa05c65e8d1c"
                }
              },
              {
                "@context": {
                  "xsd": "http://www.w3.org/2001/XMLSchema#",
                  "gx": "https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#",
                  "rdf": "http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                },
                "@id": "https://www.gaia-x4plcaad.info/claims/physical-resource/25929d5f-a577-475c-aab8-aa05c65e8d1c",
                "@type": "gx:PhysicalResource",
                "gx:maintainedBy": {
                  "id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json"
                },
                "gx:ownedBy": {
                  "id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json"
                },
                "gx:manufacturedBy": {
                  "id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json"
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
                  "rdf": "http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                },
                "@id": "https://www.gaia-x4plcaad.info/claims/data-resource/25929d5f-a577-475c-aab8-aa05c65e8d1c",
                "@type": "gx:DataResource",
                "gx:copyrightOwnedBy": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json",
                "gx:license": "Apache-2.0",
                "gx:obsoleteDateTime": {
                  "@value": "2025-03-24T14:32:37",
                  "@type": "xsd:dateTime"
                },
                "gx:exposedThrough": {
                  "id": "https://www.gaia-x4plcaad.info/claims/service-offering/25929d5f-a577-475c-aab8-aa05c65e8d1c"
                },
                "gx:expirationDateTime": {
                  "@value": "2025-03-24T14:32:37",
                  "@type": "xsd:dateTime"
                },
                "gx:containsPII": false,
                "gx:policy": {
                  "@value": "package access_control  default allow = false  allow {     input.method == \\"GET\\"     input.path == \\"/public\\" }  allow {     input.method == \\"POST\\"     input.path == \\"/private\\"     input.user.role == \\"admin\\" }",
                  "@type": "xsd:string"
                },
                "gx:producedBy": {
                  "id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json"
                },
                "gx:name": {
                  "@value": "Generated Data Resource",
                  "@type": "xsd:string"
                },
                "gx:description": {
                  "@value": "Generated description.",
                  "@type": "xsd:string"
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
                  "@id": "https://www.gaia-x4plcaad.info/claims/data-resource/25929d5f-a577-475c-aab8-aa05c65e8d1c",
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
                  "id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json",
                  "issuer": "did:web:participant.gxfs.gx4fm.org:msg-systems-ag",
                  "issuanceDate": "2024-06-23T17:00:18.698Z",
                  "credentialSubject": {
                    "type": "gx:LegalParticipant",
                    "gx:legalName": "msg systems ag",
                    "gx:legalRegistrationNumber": {
                      "id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalRegistration.json"
                    },
                    "gx:headquarterAddress": {
                      "gx:countrySubdivisionCode": "DE-BY"
                    },
                    "gx:legalAddress": {
                      "gx:countrySubdivisionCode": "DE-BY"
                    },
                    "id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json"
                  },
                  "proof": {
                    "type": "JsonWebSignature2020",
                    "created": "2024-06-23T19:00:18.709+02:00",
                    "proofPurpose": "assertionMethod",
                    "verificationMethod": "did:web:participant.gxfs.gx4fm.org:msg-systems-ag#JWK2020-RSA",
                    "jws": "eyJhbGciOiJQUzI1NiIsImI2NCI6ZmFsc2UsImNyaXQiOlsiYjY0Il19..VgwUGGuUi8ZCjFGE39mTs3GHIhVLjOVi8_ki0zUeC-2RHKFGGEJb5iehYKC9AQXOV071R7lk1uudDUCwmjwtG8LJc3ExpWnuPgRdkiybSJbGTf3b_7akHDIiMUPL0r1RFjdJBxIM_DohDqRaPQkhB7nV0e5-daOmeIexgAsXHg8xG4RSK_Lu3liw3UcwUfYRtNLn7txghpZN7EIBzcpcxhc3yc6-ZxVNZfQHq1uelz-iHwke42hd2qZ1Qfy4mZQXyfIuCvo_O0q59QGa6f6VBvdHcejFxpjLd4Vl_bMAzdPy1mLefsjLVNQPj-2Dxbt5Jvy77lqd37bR_SdU2A6DogUCycHHrqc0Bd8-Ek69Tuz5yKUaGo03sceRUoWupEXKID6gPYuPoPuZnpIIlNkc_cKYZEG-WIeK_7nd-PfQ2470hAIqz4CVu1hapVRjOxMEuANJhXCgGLwJsDJBySwJ9c7T64gYRZKq4qUaGYw8ovT_clVdeVRNhZOopduh14U9zCvRjYU8zNHPrkMsVQJIP2Jt0y0cb7uLH2SQz2JtK3zdT8IFWtiYNZFpHgWtrjsEb0BtjgvgApwGrscPOHy7k4qiou4RC2JoSKtgzmn_9ivtcemdKTcaZIzrE8qSoRrITUl2Lis0daf1kkIKzrqaP67efJoh-wUnZGYxn6VE7mM"
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

    public static final String EXAMPLE_RESPONSE_200 = """
            [
              {
                "@context": [
                  "https://www.w3.org/2018/credentials/v1"
                ],
                "holder": "did:web:participant.gxfs.gx4fm.org:msg-systems-ag",
                "id": "did:web:sd-creator.gxfs.gx4fm.org:id-documents:9c4d3898d60641869182acaa283343cb",
                "proof": {
                  "created": "2024-07-02T15:40:43Z",
                  "jws": "eyJiNjQiOmZhbHNlLCJjcml0IjpbImI2NCJdLCJhbGciOiJQUzI1NiJ9..d6IisWPHxA5vOhDRX4IuoWa02SSTFVXAzCiKKQZ54PeZa-CRdTxh5OwOfaYA0cBCCOVki8MnYa409GbBW7cWwtY9570ki_DpuW8pwp7UKoyWAkQROztoi0lVXGcnT0EmPw7VVUioxLxxC2Kev35mIz1hDJBtJj_7ZKyfzicwjAVwYVgu1Mut09dnilbpfYzc96q3dlpE9mtQj6dJwwou1LquxhZU5WZ6308kqSx4Mox1ViyywV0FUhJuQeruwmBI2j8cJNwMLbZTQ-s7bhJ2UljrPQQWqwMAj8sVpCCwDkiuIY16jiXk6K6sh6ce4mrmNf4qbAHa3t5O3hUtEekMRQtJ03w03qlo0JbEo8h4_yKAewAuyAO6ee22aFVzvCVbHx7r56ELTJ2aDKVjypPgLx511xDow0NtUnCa1W_G9rb7gC9HrmTvVqlV78V8lF6tCKV6in4PXchvaVBhyXoPivzafUdVQRdIAa9ZHXVaV7Ns3-lSwDvrKzqkKx6ZfiKbN7apRsYi_yZHZqwTBeu17kc0JPVRLOGulMfO8wNintqIlRQx-4LRF5Q3E-xU0Ib8WYMcQPPeDzlP0RchT_96E-pnknnRZzhSBSKoA-JkH1NP7NpmkE-C2OM8w85c_fjEgJDgwa0uDckA2PWOfyxOxamStO7o0PZYMkMZtU_2epE",
                  "proofPurpose": "assertionMethod",
                  "type": "JsonWebSignature2020",
                  "verificationMethod": "did:web:participant.gxfs.gx4fm.org:msg-systems-ag#JWK2020-RSA"
                },
                "type": [
                  "VerifiablePresentation"
                ],
                "verifiableCredential": [
                  {
                    "@context": [
                      "https://www.w3.org/2018/credentials/v1",
                      "https://www.w3.org/2018/credentials/examples/v1"
                    ],
                    "credentialSubject": {
                      "@context": {
                        "gx": "https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#",
                        "rdf": "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
                        "xsd": "http://www.w3.org/2001/XMLSchema#"
                      },
                      "@id": "https://www.gaia-x4plcaad.info/claims/service-offering/25929d5f-a577-475c-aab8-aa05c65e8d1c",
                      "@type": "gx:ServiceOffering",
                      "gx:dataAccountExport": {
                        "gx:accessType": "digital",
                        "gx:formatType": {
                          "@type": "xsd:string",
                          "@value": "application/json"
                        },
                        "gx:requestType": "API"
                      },
                      "gx:policy": {
                        "@type": "xsd:string",
                        "@value": "package access_control default allow = false  allow { input.method == \\"GET\\" input.path == /public } allow { input.method == \\"POST\\"     input.path == \\"/private\\"     input.user.role == \\"admin\\" }"
                      },
                      "gx:providedBy": {
                        "id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json"
                      },
                      "gx:termsAndConditions": {
                        "gx:URL": {
                          "@type": "xsd:string",
                          "@value": "https://www.msg.group/sotermsandconditions"
                        },
                        "gx:hash": {
                          "@type": "xsd:string",
                          "@value": "d056db972da41899ed89cd3f93b8ccd59fc9314e2904af3455ce13d76a2ed99b"
                        }
                      }
                    },
                    "expirationDate": "2024-12-17T15:40:32Z",
                    "id": "did:web:sd-creator.gxfs.gx4fm.org:id-documents:2b6a3420ee2b408497ecc75ad4ea9b5c",
                    "issuanceDate": "2024-07-02T15:40:32Z",
                    "issuer": "did:web:participant.gxfs.gx4fm.org:msg-systems-ag",
                    "proof": {
                      "created": "2024-07-02T15:40:32Z",
                      "jws": "eyJiNjQiOmZhbHNlLCJjcml0IjpbImI2NCJdLCJhbGciOiJQUzI1NiJ9..WCVI-AaTtgdWNMHJ1i5tfM4L4cgrcnDDjnrhIeb1W8MJMKo_UDmXRt58Bkr-xQG7KWUrxdnjWLep2i5X94ZMFNdEveLbtootWvCeKgCMpNOEuxg5auLUnaAUWBXTBMv3ypNq3vsRQo9S29nq4mBVbFa7ihTKqQxCCrl3jSVr-iAo9vbvor4jCYnG-B5ply--1ouw9pGi3j48TglvGC-Sz1APkaOkMNVHCx6GlN4baxPAiEAedNbUPiy0ZgFcPydBQdvq4Z6GYysJEZjsei9i6QxMOe4Df2k_nqZrehRurEBDOGWiIjew9WIROsuEIfk12RmmMAOGrQRvO-Ltu4ToSsJiherj-jaXmElU7601zNFzyk-yDGluXZyeBykpzJ_bvmVe4JnvuJRRRBrq1Lv7gUCovM3U3JwxJaMIllmB7vtW9xmjqel_9zywWAvWK3lhwSPx-p0XMexFkARdWaFjRU1ZwGkGsuMGpUHp47pGBTP3uKUs1SYDTWDcjRP-vG5AbY1Y901zH1RcL8BX60zHJNuEkbkXX12eRhL7cGyeM8VW0l9WNxeLtcwhG3yZB7pp-BRIIGspIyJWhGtGkScaZwjRWE7pxG0vuuOkTcTKXKp0VoBBD5LLllzfFxzL3yP-1pg-oTx7K0aM1HYhTCWIuxzJJYK0mY5K05J58flyRGA",
                      "proofPurpose": "assertionMethod",
                      "type": "JsonWebSignature2020",
                      "verificationMethod": "did:web:participant.gxfs.gx4fm.org:msg-systems-ag#JWK2020-RSA"
                    },
                    "type": [
                      "VerifiableCredential"
                    ]
                  }
                ]
              },
              {
                "@context": [
                  "https://www.w3.org/2018/credentials/v1"
                ],
                "holder": "did:web:participant.gxfs.gx4fm.org:msg-systems-ag",
                "id": "did:web:sd-creator.gxfs.gx4fm.org:id-documents:274333bed3674a3cb58710efc225d833",
                "proof": {
                  "created": "2024-07-02T15:40:44Z",
                  "jws": "eyJiNjQiOmZhbHNlLCJjcml0IjpbImI2NCJdLCJhbGciOiJQUzI1NiJ9..DRK0bScWQNEnRvFacjTSg18hPQy1K0Xo4RTcayJDbzFXfRcqy_49hgbLaGmLsSxsge0wPuv999arbYHDZTvIOeOxFDwxbgi7h7i79aJvbck2ZoqvdLTSKPQRZH7VAAsiLm5Gz1yp0Cx_HDHdOHzLo07a2k0BZlT5TDTFjoV8T_M2AtS8IbU6amP8PVgHKdfRx093_AVlOjY0_W0VL2PZ6JcqWwNlKe_zVMfTWjYGxFJRQB_KvHfUX4XVTrVGaNkoJaXoxRwv7fteSBMlOrkUD2qKR1-fi7K6sBSqwaC0_m5y4ZKRzHXNf1kCgZImbdzihXughXJ3aw8KKDknhCJTV9zkBF2dORPUydTU0KOmN0n3nbLvuNvB_w7vkcDdA45LdE60Fh_N7mtrPfilHMW5TpfVwCVJyXxZG4QEe-QXE9uKnE9XMO4b5UwqaiR5hAHkd_DtiEkAleif6TWJGJVf-tHw7Txzz8Tl7deqTKfJf6tgaO9R_cREV_uFfi8oHw-Z7jMueKDOuoKQOV0B4zqoZT1GYfyDqjdtXiTQoC8QU3bBo_wN8zDY1RB7UOw43P_TSEJNjqHMnxjbKBnEYQQfOz8rlXLWQwxSVBESDgyBPv_Q0WUq0-clOjH5HYEBYhGkOGBnNfX1AgXYoojWzcuP6Q23rmgUAKUcEvXDfSENh8k",
                  "proofPurpose": "assertionMethod",
                  "type": "JsonWebSignature2020",
                  "verificationMethod": "did:web:participant.gxfs.gx4fm.org:msg-systems-ag#JWK2020-RSA"
                },
                "type": [
                  "VerifiablePresentation"
                ],
                "verifiableCredential": [
                  {
                    "@context": [
                      "https://www.w3.org/2018/credentials/v1",
                      "https://www.w3.org/2018/credentials/examples/v1"
                    ],
                    "credentialSubject": {
                      "@context": {
                        "gx": "https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#",
                        "rdf": "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
                        "xsd": "http://www.w3.org/2001/XMLSchema#"
                      },
                      "@id": "https://www.gaia-x4plcaad.info/claims/data-resource/25929d5f-a577-475c-aab8-aa05c65e8d1c",
                      "@type": "gx:DataResource",
                      "gx:containsPII": false,
                      "gx:copyrightOwnedBy": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json",
                      "gx:description": {
                        "@type": "xsd:string",
                        "@value": "Generated description."
                      },
                      "gx:expirationDateTime": {
                        "@type": "xsd:dateTime",
                        "@value": "2025-03-24T14:32:37"
                      },
                      "gx:exposedThrough": {
                        "id": "https://www.gaia-x4plcaad.info/claims/service-offering/25929d5f-a577-475c-aab8-aa05c65e8d1c"
                      },
                      "gx:license": "Apache-2.0",
                      "gx:name": {
                        "@type": "xsd:string",
                        "@value": "Generated Data Resource"
                      },
                      "gx:obsoleteDateTime": {
                        "@type": "xsd:dateTime",
                        "@value": "2025-03-24T14:32:37"
                      },
                      "gx:policy": {
                        "@type": "xsd:string",
                        "@value": "package access_control  default allow = false  allow {     input.method == \\"GET\\"     input.path == \\"/public\\" }  allow {     input.method == \\"POST\\"     input.path == \\"/private\\"     input.user.role == \\"admin\\" }"
                      },
                      "gx:producedBy": {
                        "id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json"
                      }
                    },
                    "expirationDate": "2024-12-17T15:40:31Z",
                    "id": "did:web:sd-creator.gxfs.gx4fm.org:id-documents:446c03b6bb754cd08ae52d623dcd0f40",
                    "issuanceDate": "2024-07-02T15:40:31Z",
                    "issuer": "did:web:participant.gxfs.gx4fm.org:msg-systems-ag",
                    "proof": {
                      "created": "2024-07-02T15:40:31Z",
                      "jws": "eyJiNjQiOmZhbHNlLCJjcml0IjpbImI2NCJdLCJhbGciOiJQUzI1NiJ9..NL3EgcTbe-bRqxDxKYljm-uinTnlH3DvooVNZiMtjHgbP_dvBDCM01yic_z3FbUC9wepTcYWs2lU4ljUGHl8HylL2UEmYAikc8g5F2-IgsxgiyFbYwKW8U2OnyuvILNotN4W1gqoy2sTG_1Lg0uvVSbb35wAL9r4W1lYtL1tCVyy93a9qTTDlXIen0wv8HE435qUhPR3jDxwuMOtn16ZMujDppI0AcUvlLOCssVvMoNXup9xPFz4yp9WE7fgow-Hfu_oiJ4zooPYyfP-pXtI9tGrvINk83o73pNFdcwoPaZsDrKgn2Drlu0JQKKyfwp8PllVwOc5exeuiwIqknsLTDkckBWTB31KydS8m9muKzhstMsZboTeMqT-rEr-ncEt9eu5_4B5hSCfNaPRPg-FSBs9LznuKu6aBCuxFT8eFp9pyrREEJWwg6CLS1VCKFj1QVM9OSdGRd50dsU2579-HKSiaidbxVxqI1Hju6AAyx73z5Dwhoj_HxdAcvpgUT8EGn2w60XCMqTFZ7-jdzFYFrCh1wSOan51kVCZwBczvhtBdKCCDmYubdv0_B5g7oLfSq_HW8_BHd0keZYrbrnKHZ9R-e-xk8kXmYt9i9gZnL0kQmRgk1Gu2-NELuIRxy4KEYVO6HY74sOdEkCluLMD48k130qfCF5QS3yGALB8VII",
                      "proofPurpose": "assertionMethod",
                      "type": "JsonWebSignature2020",
                      "verificationMethod": "did:web:participant.gxfs.gx4fm.org:msg-systems-ag#JWK2020-RSA"
                    },
                    "type": [
                      "VerifiableCredential"
                    ]
                  },
                  {
                    "@context": [
                      "https://www.w3.org/2018/credentials/v1",
                      "https://www.w3.org/2018/credentials/examples/v1"
                    ],
                    "credentialSubject": {
                      "@context": {
                        "gx": "https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#",
                        "rdf": "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
                        "xsd": "http://www.w3.org/2001/XMLSchema#"
                      },
                      "@id": "https://www.gaia-x4plcaad.info/claims/physical-resource/25929d5f-a577-475c-aab8-aa05c65e8d1c",
                      "@type": "gx:PhysicalResource",
                      "gx:description": {
                        "@type": "xsd:string",
                        "@value": "Generated PhysicalResource Description."
                      },
                      "gx:location": {
                        "@type": "xsd:string",
                        "@value": "DE"
                      },
                      "gx:maintainedBy": {
                        "id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json"
                      },
                      "gx:manufacturedBy": {
                        "id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json"
                      },
                      "gx:name": {
                        "@type": "xsd:string",
                        "@value": "Generated PhysicalResource"
                      },
                      "gx:ownedBy": {
                        "id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json"
                      }
                    },
                    "expirationDate": "2024-12-17T15:40:33Z",
                    "id": "did:web:sd-creator.gxfs.gx4fm.org:id-documents:107ee1a93e4a4fc88866cc1cb799f55a",
                    "issuanceDate": "2024-07-02T15:40:33Z",
                    "issuer": "did:web:participant.gxfs.gx4fm.org:msg-systems-ag",
                    "proof": {
                      "created": "2024-07-02T15:40:33Z",
                      "jws": "eyJiNjQiOmZhbHNlLCJjcml0IjpbImI2NCJdLCJhbGciOiJQUzI1NiJ9..L1iwcr2xZpORmJnHAzGHMU2jM-W-B7V-5M0rVm2azRD69esW9f2EVU-heLND6a-gzWM43i9oaQ84SaVVKGoNBz7lQMaro9rSg_wMUzGPMSrjjmO6FnNWj1NYX3X00SG-Fb9lPEILr7UJDiNDdKFeYxgV5rqIrrFdyJQ6k0oOnjqjs5z04Iq68q1wKdPLz-qtAPukof4Z0rkv_XLDK8UkrnQNWYVDpal5sZXgQ-W8CbW_7EJ4S3Li6g-oy-Q3he-ltbNVJ3z8qS6pLyHYBbvh7v_Nd_osS7Q3_qDB8Vy6UmWfBMmq9KuaEQQBoAyK0v0PbHTRH10vQ-hK806prqRmBfx9P0IomKl-l5mEX8RwQz43qlvIheI3iRPjMHH4xJa1F6oULaJVJJ8U1kPT-F9IOghfDv07Xmy0BwuwV78ksn6MBw5xC86Nn9xKJRQbn0jkkSjtX-qjyVuDMEnsy_3ZEw0GmDXfUn_wj__OOubrBR2Q-lzH2Ak5YDYqzyViba83w9rWi9KNqqdrPL9LE2asn_ticBoZiTPY0OLzMtkhJEEvuOfO9ILX8N0rtMpEIMkWWj7iTXzYJRnS8aMJfcImnDPzagfhKtLmXLy63yg-Et1gUxYGmRs1DbFJSjthZiIqh2o4FM-O73aLUf7t03ory2ZKwX0DCIPkuBc5ebNX_fU",
                      "proofPurpose": "assertionMethod",
                      "type": "JsonWebSignature2020",
                      "verificationMethod": "did:web:participant.gxfs.gx4fm.org:msg-systems-ag#JWK2020-RSA"
                    },
                    "type": [
                      "VerifiableCredential"
                    ]
                  },
                  {
                    "@context": [
                      "https://www.w3.org/2018/credentials/v1",
                      "https://www.w3.org/2018/credentials/examples/v1"
                    ],
                    "credentialSubject": {
                      "@context": {
                        "gax-core": "https://w3id.org/gaia-x/core#",
                        "skos": "http://www.w3.org/2004/02/skos/core#",
                        "surveyonto": "http://semanticweb.org/metadatasurveyontology/",
                        "trusted-cloud": "http://w3id.org/gaia-x/trusted-cloud#",
                        "xsd": "http://www.w3.org/2001/XMLSchema#"
                      },
                      "@id": "https://www.gaia-x4plcaad.info/claims/data-resource/25929d5f-a577-475c-aab8-aa05c65e8d1c",
                      "@type": [
                        "surveyonto:SurveyResultDataOffering"
                      ],
                      "surveyonto:belongs_to": {
                        "@id": "did:example:survey_service_offering_zcdkr7kqd47y0w5b4tg91w1etw"
                      },
                      "surveyonto:contract_id": {
                        "@type": "xsd:string",
                        "@value": "contract_zcdkr7kqd47y0w5b4tg91w1etw"
                      },
                      "surveyonto:edc_endpoint": {
                        "@type": "xsd:string",
                        "@value": "https://edcdb-1.gxfs.gx4fm.org/"
                      },
                      "surveyonto:survey_close_time": {
                        "@type": "xsd:dateTime",
                        "@value": "2024-04-29T12:16:17"
                      },
                      "surveyonto:survey_start_time": {
                        "@type": "xsd:dateTime",
                        "@value": "2023-03-29T12:16:17"
                      }
                    },
                    "expirationDate": "2024-12-17T15:40:29Z",
                    "id": "did:web:sd-creator.gxfs.gx4fm.org:id-documents:497b17f2e27f4da48333f709d00bb1af",
                    "issuanceDate": "2024-07-02T15:40:29Z",
                    "issuer": "did:web:participant.gxfs.gx4fm.org:msg-systems-ag",
                    "proof": {
                      "created": "2024-07-02T15:40:29Z",
                      "jws": "eyJiNjQiOmZhbHNlLCJjcml0IjpbImI2NCJdLCJhbGciOiJQUzI1NiJ9..nsbmHUOBdSUZqhBXvu4hFWZvIKSXhjrbKlFOifxcsmj2btaVhLXytTL5rszvFm7D9u9KMNx7tVdeU_u_eUlYiIvTevOt5f0h4rlp0mhxeEUNrssKxhRGGHRYV8q2l6QBBZWLisdc48S3pWmLHoyB_-RQwb2MfwTA4hiYjm1QjuxRlMGAr0bWhnaFXjzscT-ijrsGgMYXVbiyHFaj_w9U_f13HvbUf6K6Akq3RU95g4dDA_svxvF6yhPNhRGjFgx1uLbt1N2QPS3hs6ZbeqN0ELyhnb4kck6gnqiw_cRGUBOl9J4-OKAV6GxUDra12V9ooZoY_CFNiUqm8Y34gqc4HXk4pI2XD2xnDtPWP6LOY-pqiiWB46T94pDE8sP6Wro_lKaWKfmcPHFMOQFv7dNJNx8Actf8i3o1USL7FKNnNVIsB6IlC4dVkK9ZWHA8W4MZB2RD2GnPOA2ZnDtHl-MKg3SIIkjjElfqe6xQikawtyqvsZtfQ2Gf3Y3LpCUFK9mtBHnc0IbPC3nJc-_YFqcSKU6N7mbYLsMlGZg62U8o2Qk9ietgJXcViiqN4YXL-1FMEvKr3ENakdzujm_2RJ-LVEL4i45BYunqGHmj_2hPwI7MGHD895PGgdQz14B0ViTIMpBNdd7vh6krOaD9XdmJWdNOx_TneO5Mgyi_tC0NpGM",
                      "proofPurpose": "assertionMethod",
                      "type": "JsonWebSignature2020",
                      "verificationMethod": "did:web:participant.gxfs.gx4fm.org:msg-systems-ag#JWK2020-RSA"
                    },
                    "type": [
                      "VerifiableCredential"
                    ]
                  },
                  {
                    "@context": [
                      "https://www.w3.org/2018/credentials/v1",
                      "https://www.w3.org/2018/credentials/examples/v1"
                    ],
                    "credentialSubject": {
                      "@context": {
                        "gx": "https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#",
                        "rdf": "http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                      },
                      "@id": "https://www.gaia-x4plcaad.info/claims/virtual-resource/25929d5f-a577-475c-aab8-aa05c65e8d1c",
                      "@type": "gx:InstantiatedVirtualResource",
                      "gx:hostedOn": {
                        "id": "https://www.gaia-x4plcaad.info/claims/physical-resource/25929d5f-a577-475c-aab8-aa05c65e8d1c"
                      },
                      "gx:instanceOf": {
                        "id": "https://www.gaia-x4plcaad.info/claims/data-resource/25929d5f-a577-475c-aab8-aa05c65e8d1c"
                      },
                      "gx:maintainedBy": {
                        "id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json"
                      },
                      "gx:serviceAccessPoint": {
                        "id": "https://www.gaia-x4plcaad.info/claims/service-access-point/25929d5f-a577-475c-aab8-aa05c65e8d1c"
                      },
                      "gx:tenantOwnedBy": {
                        "id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json"
                      }
                    },
                    "expirationDate": "2024-12-17T15:40:28Z",
                    "id": "did:web:sd-creator.gxfs.gx4fm.org:id-documents:e1d70294b3fa4e879f22825b72f9d222",
                    "issuanceDate": "2024-07-02T15:40:28Z",
                    "issuer": "did:web:participant.gxfs.gx4fm.org:msg-systems-ag",
                    "proof": {
                      "created": "2024-07-02T15:40:28Z",
                      "jws": "eyJiNjQiOmZhbHNlLCJjcml0IjpbImI2NCJdLCJhbGciOiJQUzI1NiJ9..rPDTR0mRFa-0X4ChcN4hIU9ka-V1u_zDRag-K5q85gp2tX5tXYCNq1iTeVGU7QswWay0gCCOEbffuD7JAREw1X3Aq0tiqrkUkucvOyZEK-sVFoxgh3HduaW200lR3vNlTVGiUXHn1R8REOKytvjWc0uRHGG4RPtVM8IJYSm1AIiZRvtbJNnCpiz4cKIlLnPn3XBAh4KoIcSFuTk3Hum0IXQ4erlSP2ST61U6ZKubi1BGSQ8OLu93ZN10MFdjZUQXDEUxwOvZKJAv1bCMRCW2N2P8dMTO8FRYzC4EgiZeDxyOQTevcIJm7_OZtM0nBjyBK-Cf0KppEQJE2UcYYMSI8fyLg0LPIXuVbovkbA1SwYLFsLVpYRg5ubsi3ebb3F62E_TM-lJjCzKyyHQS4dQ_cwHKJaKxYjfdzADMrjMLyR0uIpdFueA2vUl5jgLtnfdR2U4viUQjgsiP-_dlc-_O8hFkQP1YyqDEI8Ek0D1ExHM2fGaDdjaYnOmf-ozVlM-GBaL4AI0X7SDkzO3cYX30f47yYJuJChRIx4xiw9_ktpIgyUGSZi3MOdu4gcBumP6p8r1Q_y7XFgsLqfD2I5ggT83mgNf61NqrW6EVvIiKUYTqmBqtW3RCyT-s71NqmSh3ixD9tha41DQcMt5GQe4feLUPwn1epJwKwFzKTwJvbYU",
                      "proofPurpose": "assertionMethod",
                      "type": "JsonWebSignature2020",
                      "verificationMethod": "did:web:participant.gxfs.gx4fm.org:msg-systems-ag#JWK2020-RSA"
                    },
                    "type": [
                      "VerifiableCredential"
                    ]
                  },
                  {
                    "@context": [
                      "https://www.w3.org/2018/credentials/v1",
                      "https://www.w3.org/2018/credentials/examples/v1"
                    ],
                    "credentialSubject": {
                      "@context": {
                        "gx": "https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#",
                        "xsd": "http://www.w3.org/2001/XMLSchema#"
                      },
                      "@id": "https://www.gaia-x4plcaad.info/claims/service-access-point/25929d5f-a577-475c-aab8-aa05c65e8d1c",
                      "@type": "gx:ServiceAccessPoint",
                      "gx:host": {
                        "@type": "xsd:string",
                        "@value": "edcdb-pr.gxfs.gx4fm.org/"
                      },
                      "gx:name": {
                        "@type": "xsd:string",
                        "@value": "Provider EDC"
                      },
                      "gx:openAPI": {
                        "@type": "xsd:string",
                        "@value": "https://app.swaggerhub.com/apis/eclipse-edc-bot/management-api/0.2.1"
                      },
                      "gx:port": {
                        "@type": "xsd:string",
                        "@value": "443"
                      },
                      "gx:protocol": {
                        "@type": "xsd:string",
                        "@value": "https"
                      },
                      "gx:version": {
                        "@type": "xsd:string",
                        "@value": "0.2.1"
                      }
                    },
                    "expirationDate": "2024-12-17T15:40:34Z",
                    "id": "did:web:sd-creator.gxfs.gx4fm.org:id-documents:1c58a5647c9e4e868e2196d26d421dbf",
                    "issuanceDate": "2024-07-02T15:40:34Z",
                    "issuer": "did:web:participant.gxfs.gx4fm.org:msg-systems-ag",
                    "proof": {
                      "created": "2024-07-02T15:40:34Z",
                      "jws": "eyJiNjQiOmZhbHNlLCJjcml0IjpbImI2NCJdLCJhbGciOiJQUzI1NiJ9..WaZ07yCGapqGPqXXdN9Ufcni7Y7WMnkzSbEq3UIk_LljvCnMPrRt-053ss_jTc5S7HPgI9crYx_QZPpcECta6-r3GIN3SparUkWtENtObrNNrf4y4EbrsmEr2QYcoz4qtq9XRB6zhNB9qxx4IsVy-DIqKj19CJHfajqeczAxVWTble0XazHoFOD-JWxBPqMVrkQPT-I68kgb7N_1jLmf31VfqWZ8vocaWMdGGb2Dlf2Tab0B-UI8-vfon-R85jBjGeliSaJ5xRklLLDiQOxOO-37Vgo8j1_kn4fVDh5JArAckHirS75nHjZiCRak8X67fEKNmeF68_30yxr3IdPRK7tNVMDQmw7Nkdku3YLvvxS4BiIfs4gyxkjdsCwBRxpji0AXCUA11uGg5mngwzal7GVh66C0iIeDJDeZzfWCSlDURQT30lUhc57zfykRYlh8Lh1XIsLY7-BBlSPg0odtDTMIyL-FoX0i4WXIdmHEoGKIPQcZfPnstWuuYlZ1RtDqEstJeZUhrtp-MqYIKQlcNlqac9JVfZhksbZue7sAc_lv7X4fob0ZXuNWY9XeUGKc6jAIz6QDF85EHE2dnRIjDDQ7fVGhpQoFZm9pS-x8UV96XaLZhpPKvnzy9yqkOisXgb3JquMXAJ1_peqvKpuRQQUNFXXrw7ZX1of5OPEo8ow",
                      "proofPurpose": "assertionMethod",
                      "type": "JsonWebSignature2020",
                      "verificationMethod": "did:web:participant.gxfs.gx4fm.org:msg-systems-ag#JWK2020-RSA"
                    },
                    "type": [
                      "VerifiableCredential"
                    ]
                  }
                ]
              },
              {
                "@context": [
                  "https://www.w3.org/2018/credentials/v1"
                ],
                "holder": "did:web:participant.gxfs.gx4fm.org:msg-systems-ag",
                "id": "did:web:sd-creator.gxfs.gx4fm.org:id-documents:b4a347f4bcbb48399d0066d4cd2a19be",
                "proof": {
                  "created": "2024-07-02T15:40:45Z",
                  "jws": "eyJiNjQiOmZhbHNlLCJjcml0IjpbImI2NCJdLCJhbGciOiJQUzI1NiJ9..mu98BlnzSsXj_pXRyqg6nsBmGMrXRaixoxGNux_JCFdXnb2v6NkFJiQ0HHyu_kXpsnmV5OBwQHAX12VZYiM3u3lbZtcHAZoly2ObZU1Wnp_fuCwr_NZ7byPbe5pISpCwyF7CIoQG13dLe2eQc6r2jyV2_feU1iQcO6EVnW3wlX43zvB7Sd1QdWT75i3PVubtspGxkrhY8FKlqaU1ZIUo_Ld8LKQx-AXwjDCMvIxdEAGP5BTmMh0m4I9MIh9Cyn6-UnBYDW2_fR3ZP6Qc2IDPDfFmbBEOZ-B9W2CoiuTKrdmL5wsngz2k9YhvHpBcOq7hAFR3LrWF8jplj-TmV0pI5ZUHXwhL-J3yJcgMYqVAcAGNmMYfRZ2Be6OoXpkcEWQ2bxiV-qkkWBpj9voQNo11ALu9uMINp86IYked7xj77IswscOsqvVM8HmxFCZbg5nVMDJWED2n5SOENWHhtt0bodqfbxWVDRiQTQbdkfg87AY-pucu1L83HBuS2IGWB1cmMoayAT49ZgvuJ7NbBrlb3gcZtZDXxL2x7t7lBzExB034RJ5LTBZuQGcV8RcFZGyKD99vnHEb44gob0HVMCvu6FDc-Vw5qG5H05sUehpquG2yfIgG3dRMQIseI4-KZMshtdD15Anb1zrOEXkSvFNzNXMqZL1pb9qWzGKROjpNpBE",
                  "proofPurpose": "assertionMethod",
                  "type": "JsonWebSignature2020",
                  "verificationMethod": "did:web:participant.gxfs.gx4fm.org:msg-systems-ag#JWK2020-RSA"
                },
                "type": [
                  "VerifiablePresentation"
                ],
                "verifiableCredential": [
                  {
                    "@context": [
                      "https://www.w3.org/2018/credentials/v1",
                      "https://w3id.org/security/suites/jws-2020/v1",
                      "https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#"
                    ],
                    "credentialSubject": [
                      {
                        "gx:integrity": "sha256-8337ca0e91cbd71512a97b3bc699dbeac85becde84d6d3814b14b6e197f9ff36",
                        "gx:integrityNormalization": "RFC8785:JCS",
                        "gx:type": "gx:LegalParticipant",
                        "gx:version": "22.10",
                        "id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json",
                        "type": "gx:compliance"
                      },
                      {
                        "gx:integrity": "sha256-f06808cc4d7175bbcef82502e5be52bd3e932763d45730d2cdd1c59523b41409",
                        "gx:integrityNormalization": "RFC8785:JCS",
                        "gx:type": "gx:DataResource",
                        "gx:version": "22.10",
                        "id": "did:web:sd-creator.gxfs.gx4fm.org:id-documents:446c03b6bb754cd08ae52d623dcd0f40",
                        "type": "gx:compliance"
                      },
                      {
                        "gx:integrity": "sha256-e6b51a6174cb2f098dd30a15347f8ed02503de83e14e534916f36d714553284e",
                        "gx:integrityNormalization": "RFC8785:JCS",
                        "gx:type": "gx:GaiaXTermsAndConditions",
                        "gx:version": "22.10",
                        "id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/termsAndConditions.json",
                        "type": "gx:compliance"
                      },
                      {
                        "gx:integrity": "sha256-2ab774b4fc577d392eed944398a651b85390b2509cf90e44f0035d9033be2688",
                        "gx:integrityNormalization": "RFC8785:JCS",
                        "gx:type": "gx:PhysicalResource",
                        "gx:version": "22.10",
                        "id": "did:web:sd-creator.gxfs.gx4fm.org:id-documents:107ee1a93e4a4fc88866cc1cb799f55a",
                        "type": "gx:compliance"
                      },
                      {
                        "gx:integrity": "sha256-9f7a947279367b57dfdfdc9c3e994765306bdeb4e68e7016df6a95b5bcd11e17",
                        "gx:integrityNormalization": "RFC8785:JCS",
                        "gx:type": "gx:ServiceOffering",
                        "gx:version": "22.10",
                        "id": "did:web:sd-creator.gxfs.gx4fm.org:id-documents:2b6a3420ee2b408497ecc75ad4ea9b5c",
                        "type": "gx:compliance"
                      },
                      {
                        "gx:integrity": "sha256-b64bda0d567e1a19258e5fa53f96ab9d6ef2313d313e372af6e8926bff212aef",
                        "gx:integrityNormalization": "RFC8785:JCS",
                        "gx:type": "gx:legalRegistrationNumber",
                        "gx:version": "22.10",
                        "id": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalRegistration.json",
                        "type": "gx:compliance"
                      },
                      {
                        "gx:integrity": "sha256-108c09ad4f9df71b05cb4ab1f12ced47b8efd022cd45140b54a85ea7d12d4392",
                        "gx:integrityNormalization": "RFC8785:JCS",
                        "gx:type": "gx:InstantiatedVirtualResource",
                        "gx:version": "22.10",
                        "id": "did:web:sd-creator.gxfs.gx4fm.org:id-documents:e1d70294b3fa4e879f22825b72f9d222",
                        "type": "gx:compliance"
                      },
                      {
                        "gx:integrity": "sha256-48369f2712b74b62a91edb876a353975cc6c3c9508b58ce60c0c033b2a2651ee",
                        "gx:integrityNormalization": "RFC8785:JCS",
                        "gx:type": "gx:ServiceAccessPoint",
                        "gx:version": "22.10",
                        "id": "did:web:sd-creator.gxfs.gx4fm.org:id-documents:1c58a5647c9e4e868e2196d26d421dbf",
                        "type": "gx:compliance"
                      }
                    ],
                    "expirationDate": "2024-09-30T15:40:42.984Z",
                    "id": "https://compliance.lab.gaia-x.eu/v1-staging/credential-offers/9e006062-3642-445c-a5de-bb92849d28d4",
                    "issuanceDate": "2024-07-02T15:40:42.984Z",
                    "issuer": "did:web:compliance.lab.gaia-x.eu:v1-staging",
                    "proof": {
                      "created": "2024-07-02T15:40:43.648Z",
                      "jws": "eyJhbGciOiJQUzI1NiIsImI2NCI6ZmFsc2UsImNyaXQiOlsiYjY0Il19..oms-RKhb8qxOiXmrU1jPYgukJXleLAVQ_vweFS2Nrg8AFuky-Mn7Vc-gZWEWGMdn5rqs_CFAi9UL6uVb9SgzkLndd4tVHPlYKgVDr6_hoyZyy-hJyJKSXwNj-8U_-SWPR4IJwRJDSkGdN4OLu8mUgwmRvkZKDJj2I6_CaF8yIiBXucLjB2FiRMURILDqWWpJKahoAElxz47k35nzr6fU0uP0Z4cghuJe50aPPMcmGLSGhrE6Gc_TMJacmPtDEaB_zW_HHg3leOFISTbvE0iT9DBeODEuxZaA5LGNyLlOdkULlJPPx7zQdPjxDlLsS6fswVLKjB8oh6nrUH3XfGXeKw",
                      "proofPurpose": "assertionMethod",
                      "type": "JsonWebSignature2020",
                      "verificationMethod": "did:web:compliance.lab.gaia-x.eu:v1-staging#X509-JWK2020"
                    },
                    "type": [
                      "VerifiableCredential"
                    ]
                  }
                ]
              }
            ]
            """;

    public static final String EXAMPLE_RESPONSE_409 = """
            {
              "message": "The signature of the document with ID did:web:sd-creator.gxfs.gx4fm.org:id-documents:d3ebe5f7d37e44d2b2d425fb9daaa4cd cannot be validated, please check the document has not been tampered",
              "errorDetails": "Conflict",
              "exceptionMessage": "The signature of the document with ID did:web:sd-creator.gxfs.gx4fm.org:id-documents:d3ebeaaa4cd cannot be validated, please check the document has not been tampered",
              "statusCode": 409,
              "verifiablePresentationId": "did:web:sd-creator.gxfs.gx4fm.org:id-documents:fb6f1ed484fb48e6b7b25453a0ac39d7"
            }
            """;

    public static final String EXAMPLE_RESPONSE_422 = """
            {
              "message": "Value does not have shape gx:DataAccountExportShape",
              "errorDetails": "verification_error",
              "exceptionMessage": "Unprocessable Entity",
              "statusCode": 422,
              "verifiablePresentationId": "did:web:sd-creator.gxfs.gx4fm.org:id-documents:fb6f1ed484fb48e6b7b25453a0ac39d7"
            }
            """;

    public static final String EXAMPLE_RESPONSE_500 = """
            {
              "message": "NullPointerException",
              "errorDetails": "Internal server error",
              "exceptionMessage": "null",
              "statusCode": 500,
              "verifiablePresentationId": "unknown"
            }
            """;
}
