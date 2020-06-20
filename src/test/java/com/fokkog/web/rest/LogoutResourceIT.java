package com.fokkog.web.rest;

import com.fokkog.TableOfPrizesApp;
import com.fokkog.config.TestSecurityConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.fokkog.web.rest.TestUtil.ID_TOKEN;
import static com.fokkog.web.rest.TestUtil.authenticationToken;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link LogoutResource} REST controller.
 */
@SpringBootTest(classes = {TableOfPrizesApp.class, TestSecurityConfiguration.class})
public class LogoutResourceIT {

    @Autowired
    private WebApplicationContext context;

    private MockMvc restLogoutMockMvc;

    private OidcIdToken idToken;

    @BeforeEach
    public void before() throws Exception {
        Map<String, Object> claims = new HashMap<>();
        claims.put("groups", Collections.singletonList("ROLE_USER"));
        claims.put("sub", 123);
        this.idToken = new OidcIdToken(ID_TOKEN, Instant.now(), Instant.now().plusSeconds(60), claims);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken(idToken));
        SecurityContextHolderAwareRequestFilter authInjector = new SecurityContextHolderAwareRequestFilter();
        authInjector.afterPropertiesSet();

        this.restLogoutMockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    public void getLogoutInformation() throws Exception {
        String logoutUrl = "https://tableofprizes.b2clogin.com/tableofprizes.onmicrosoft.com/oauth2/v2.0/logout?p=b2c_1_signupsignin";
        restLogoutMockMvc.perform(post("/api/logout"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.logoutUrl").value(logoutUrl))
            .andExpect(jsonPath("$.idToken").value(ID_TOKEN));
    }
}
