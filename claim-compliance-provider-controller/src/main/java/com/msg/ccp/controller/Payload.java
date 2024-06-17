package com.msg.ccp.controller;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class Payload {
    final Set<Map<String, Object>> claims;
    final Set<VerifiableCredential> verifiableCredentials;
}
