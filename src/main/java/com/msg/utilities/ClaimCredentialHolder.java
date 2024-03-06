package com.msg.utilities;

import java.util.Map;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ClaimCredentialHolder {
    Set<Map<String, Object>> claims;
    Set<Map<String, Object>> verifiableCredentials;
}
