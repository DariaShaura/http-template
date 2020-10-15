package com.epam.izh.rd.online.service;

import com.epam.izh.rd.online.entity.Pokemon;
import com.epam.izh.rd.online.entity.Stat;
import com.epam.izh.rd.online.factory.ObjectMapperFactory;
import com.epam.izh.rd.online.factory.SimpleObjectMapperFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SimplePokemonFetchingService implements PokemonFetchingService {

    private final String pokeapi = "https://pokeapi.co/api/v2/";

    private OkHttpClient client;
    private Request request;
    private Call call;
    private Response response;
    private String url = "";
    private Properties properties;

    public SimplePokemonFetchingService() {
        client = new OkHttpClient();

        try(InputStream inputStream = SimplePokemonFetchingService.class.getClassLoader().getResourceAsStream("application.properties")) {
            properties = new Properties();
            if(inputStream != null) {
                properties.load(inputStream);
            }
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * @param name - имя покемона
     * @return сущность Pokemon
     * @throws IllegalArgumentException при условии, если имя покемона указано неверно
     */
    public Pokemon fetchByName(String name) throws IllegalArgumentException{

        url = properties.getProperty("url.pokemon");

        if(url.equals("")){
            return null;
        }

        if(url.charAt(url.length()-1) != '/'){
            url = url + "/";
        }

        request =  new Request.Builder()
                .url(String.format("%s%s", url, name))
                .header("User-Agent", "")
                .build();

        call = client.newCall(request);

        String json;
        try {
            response = call.execute();
            if(response.code() == 200){
                json = response.body().string();
            }
            else {
                throw new IOException();
            }
        }
        catch(IOException e){
            throw new IllegalArgumentException();
        }

        ObjectMapper objectMapper = new SimpleObjectMapperFactory().getObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Pokemon pokemon;

        try {
            pokemon = objectMapper.readValue(json, Pokemon.class);

            JsonNode stats = objectMapper.readTree(json).get("stats");
            Stat[] statArray = objectMapper.readValue(stats.toString(), Stat[].class);
            for (int i = 0; i < statArray.length; i++) {
                switch (statArray[i].getName()) {
                    case "hp":
                        pokemon.setHp(statArray[i].getBaseStat());
                        break;
                    case "attack":
                        pokemon.setAttack(statArray[i].getBaseStat());
                        break;
                    case "defense":
                        pokemon.setDefense(statArray[i].getBaseStat());
                        break;
                }
            }
        }
        catch(JsonProcessingException e){
            return null;
        }

        return pokemon;
    }

    /**
     * @param name - имя покемона
     * @return картинка покемона в виде массива байтов
     * @throws IllegalArgumentException при условии, если имя покемона указано неверно
     */
    public byte[] getPokemonImage(String name) throws IllegalArgumentException {

        url = properties.getProperty("url.pokemon");

        if(url.equals("")){
            return null;
        }

        if(url.charAt(url.length()-1) != '/'){
            url = url + "/";
        }

        byte[] picture = null;

        request = new Request.Builder()
                .url(String.format("%s%s", url, name))
                .header("User-Agent", "")
                .build();

        call = client.newCall(request);

        String json;
        try {
            response = call.execute();
            json = response.body().string();
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }

        ObjectMapper objectMapper = new SimpleObjectMapperFactory().getObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            String pictureURL = objectMapper.readTree(json).get("sprites").get("front_default").asText();
            pictureURL = pictureURL.replace("https://", properties.getProperty("url.image"));

            request = new Request.Builder()
                    .url(pictureURL)
                    .header("User-Agent", "")
                    .build();

            call = client.newCall(request);

            response = call.execute();

            if(response.code() == 200) {
                picture = response.body().bytes();
            }
            else{
                throw new Exception("code = 401");
            }

        } catch (Exception e) {
            return null;
        }

        return picture;
    }
}
