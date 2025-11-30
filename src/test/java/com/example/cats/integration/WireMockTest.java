package com.example.cats.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.*;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

class WireMockTest {

    static WireMockServer wireMockServer;

    @BeforeAll
    static void setup() {
        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();
        configureFor("localhost", 8089);
    }

    @AfterAll
    static void teardown() {
        wireMockServer.stop();
    }

    @Test
    void shouldReturnMockedResponse() {
        stubFor(get(urlEqualTo("/external/cats"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("{\"message\":\"Meow from cosmos\"}")));

        var response = wireMockServer.baseUrl() + "/external/cats";
        assertTrue(response.contains("8089"));
    }
}

