package com.epam.izh.rd.online;

import com.epam.izh.rd.online.entity.Pokemon;
import com.epam.izh.rd.online.service.PokemonFetchingService;
import com.epam.izh.rd.online.service.PokemonFightingClubService;
import com.epam.izh.rd.online.service.SimplePokemonFetchingService;
import com.epam.izh.rd.online.service.SimplePokemonFightingClubService;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.sun.org.apache.xerces.internal.util.PropertyState.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PokemonFightingClubServiceTest {

    static Pokemon p1;
    static Pokemon p2;
    static PokemonFetchingService pokemonFetchingService;
    static PokemonFightingClubService pokemonFightingClubService;

    private static WireMockServer wireMockServer;

    @BeforeAll
    public static void setup () {
        wireMockServer = new WireMockServer(8090);
        wireMockServer.start();
        setupStub();

        pokemonFetchingService = new SimplePokemonFetchingService();
        pokemonFightingClubService = new SimplePokemonFightingClubService();

        File winnerImage = new File("winner.jpg");
        if(winnerImage.isFile()){
            winnerImage.delete();
        }
    }

    @BeforeEach
    public void fetchingPokemons(){
        p1 = pokemonFetchingService.fetchByName("pikachu");
        p2 = pokemonFetchingService.fetchByName("slowpoke");
    }

    @AfterAll
    public static void teardown () {
        wireMockServer.stop();
    }

    public static void setupStub() {
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

    @Test
    public void doBattleTest(){
        assertThat(p2, CoreMatchers.is(pokemonFightingClubService.doBattle(p1,p2)));
    }


    @Test
    public void showWinnerTest_pikachu(){
        pokemonFightingClubService.showWinner(p1);

        File winnerImage = new File("winner.jpg");

        assertTrue(new File("winner.jpg").isFile());

        winnerImage.delete();
    }

    @Test
    public void showWinnerTest_slowpoke(){
        pokemonFightingClubService.showWinner(p2);

        File winnerImage = new File("winner.jpg");

        assertTrue(new File("winner.jpg").isFile());

        winnerImage.delete();
    }

    @Test
    public void doDamageTest(){

        pokemonFightingClubService.doDamage(p1, p2);

        int attackedHealth = p2.getHp();

        assertEquals(70, attackedHealth);
    }
}
