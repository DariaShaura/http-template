package com.epam.izh.rd.online.entity;

import com.epam.izh.rd.online.service.StatDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;

@Getter
@JsonDeserialize(using = StatDeserializer.class)
public class Stat {

    private short baseStat;

    private String name;

    public Stat(short baseStat, String name) {
        this.baseStat = baseStat;
        this.name = name;
    }
}
