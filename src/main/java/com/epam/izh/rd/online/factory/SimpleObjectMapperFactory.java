package com.epam.izh.rd.online.factory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SimpleObjectMapperFactory implements ObjectMapperFactory {

    public ObjectMapper getObjectMapper(){
        return new ObjectMapper();
    }
}
