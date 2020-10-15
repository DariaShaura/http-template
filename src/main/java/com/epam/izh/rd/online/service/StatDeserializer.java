package com.epam.izh.rd.online.service;

import com.epam.izh.rd.online.entity.Stat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ShortNode;

import java.io.IOException;

public class StatDeserializer extends StdDeserializer<Stat> {

    public StatDeserializer() {
        this(null);
    }

    public StatDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Stat deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        short baseStat = (short) node.get("base_stat").asInt();
        String name = node.get("stat").get("name").asText();

        return new Stat(baseStat, name);
    }
}
