package com.epam.izh.rd.online.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Покемон. Поля должны заполняться из JSON, который возвратит внешний REST-service
 * Для маппинка значений из массива stats рекомендуется использовать отдельный класс Stat и аннотацию @JsonCreator
 */
@Getter
@ToString
public class Pokemon {

    /**
     * Уникальный идентификатор, маппится из поля pokemonId
     */
    private long pokemonId;

    /**
     * Имя покемона, маппится из поля pokemonName
     */
    private String pokemonName;

    @JsonCreator
    public Pokemon(@JsonProperty("id") long pokemonId, @JsonProperty("name") String pokemonName) {
        this.pokemonId = pokemonId;
        this.pokemonName = pokemonName;
    }

    /**
     * Здоровье покемона, маппится из массива объектов stats со значением name: "hp"
     */
    @Setter
    private short hp;

    /**
     * Атака покемона, маппится из массива объектов stats со значением name: "attack"
     */
    @Setter
    private short attack;

    /**
     * Защита покемона, маппится из массива объектов stats со значением name: "defense"
     */
    @Setter
    private short defense;
}
