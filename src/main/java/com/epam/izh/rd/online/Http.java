package com.epam.izh.rd.online;

import com.epam.izh.rd.online.entity.Pokemon;
import com.epam.izh.rd.online.service.PokemonFetchingService;
import com.epam.izh.rd.online.service.PokemonFightingClubService;
import com.epam.izh.rd.online.service.SimplePokemonFetchingService;
import com.epam.izh.rd.online.service.SimplePokemonFightingClubService;

public class Http {
    public static void main(String[] args) {
        PokemonFetchingService pokemonFetchingService = new SimplePokemonFetchingService();
        PokemonFightingClubService pokemonFightingClubService = new SimplePokemonFightingClubService();

        try {
            Pokemon pikachu = pokemonFetchingService.fetchByName("pikachu");
            Pokemon slowpoke = pokemonFetchingService.fetchByName("slowpoke");

            byte[] picture = pokemonFetchingService.getPokemonImage("pikachu");
            Pokemon winner = pokemonFightingClubService.doBattle(pikachu, slowpoke);

            pokemonFightingClubService.showWinner(winner);
        }
        catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }
}
//