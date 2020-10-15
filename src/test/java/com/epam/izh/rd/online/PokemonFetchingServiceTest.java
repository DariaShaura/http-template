package com.epam.izh.rd.online;

import com.epam.izh.rd.online.entity.Pokemon;
import com.epam.izh.rd.online.service.PokemonFetchingService;
import com.epam.izh.rd.online.service.SimplePokemonFetchingService;
import com.github.tomakehurst.wiremock.WireMockServer;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

public class PokemonFetchingServiceTest {

    private static WireMockServer wireMockServer;
    private static PokemonFetchingService pokemonFetchingService;

    @BeforeAll
    public static void simplePokemonFetchingServiceCreate()
    {
        pokemonFetchingService = new SimplePokemonFetchingService();
    }

    @BeforeAll
    public static void setup () {
        wireMockServer = new WireMockServer(8090);
        wireMockServer.start();
        setupStub();
    }

    @AfterAll
    public static void teardown () {
        wireMockServer.stop();
    }

    public static void setupStub() {
        wireMockServer.stubFor(get(urlMatching("/pokeapi.co/api/v2/pokemon/.*")).atPriority(5)
                .willReturn(aResponse().withStatus(401)));
        wireMockServer.stubFor(get(urlEqualTo("/pokeapi.co/api/v2/pokemon/pikachu")).atPriority(1)
                .willReturn(aResponse().withHeader("User-Agent", "")
                        .withStatus(200)
                        .withBodyFile("pikachu.json")));
        wireMockServer.stubFor(get(urlEqualTo("/pokeapi.co/api/v2/pokemon/slowpoke")).atPriority(2)
                .willReturn(aResponse().withHeader("User-Agent", "")
                        .withStatus(200)
                        .withBodyFile("slowpoke.json")));
        wireMockServer.stubFor(get(urlEqualTo("/raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png")).atPriority(3)
                .willReturn(aResponse().withHeader("User-Agent", "")
                        .withStatus(200)
                        .withBodyFile("25.png")));
        wireMockServer.stubFor(get(urlEqualTo("/raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/79.png")).atPriority(4)
                .willReturn(aResponse().withHeader("User-Agent", "")
                        .withStatus(200)
                        .withBodyFile("79.png")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"pikachu", "slowpoke"})
    public void fetchByNameTest(String name) {

        Pokemon pokemon = pokemonFetchingService.fetchByName(name);

        assertThat(pokemon.getPokemonName(), equalTo(name));
    }

    @ParameterizedTest
    @ValueSource(strings = {"pikasu", "123"})
    public void fetchByNameTestShouldThrowException(String name) {

        assertThrows(IllegalArgumentException.class, ()-> pokemonFetchingService.fetchByName(name));

    }


    @ParameterizedTest
    @ValueSource(strings = {"pikachu", "slowpoke"})
    public void getPokemonImageTest(String name) throws IOException {

        byte[] picture = new SimplePokemonFetchingService().getPokemonImage(name);

        assertNotNull(picture);
    }
}
