package com.fokkog.security.oauth2;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for the {@link AudienceValidator} utility class.
 */
public class AudienceValidatorTest {

    private final AudienceValidator validator = new AudienceValidator(Arrays.asList("api://default"));

    @Test
    public void testInvalidAudience() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("aud", "bar");
        Jwt badJwt = mock(Jwt.class);
        when(badJwt.getAudience()).thenReturn(Convert(claims.values()));
        assertThat(validator.validate(badJwt).hasErrors()).isTrue();
    }

    @Test
    public void testValidAudience() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("aud", "api://default");
        Jwt jwt = mock(Jwt.class);
        when(jwt.getAudience()).thenReturn(Convert(claims.values()));
        assertThat(validator.validate(jwt).hasErrors()).isFalse();
    }

    public List<String> Convert(Collection<Object> values) {
        ArrayList<String> newList = new ArrayList<String>();
        values.forEach((Object o) -> {newList.add(o.toString());});
        return newList;
    } 
}
