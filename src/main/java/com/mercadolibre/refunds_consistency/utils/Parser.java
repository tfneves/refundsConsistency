package com.mercadolibre.refunds_consistency.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public abstract class Parser {

    /**
     * Converte JSON em um objeto do tipo do parametro model
     * @param response
     * @param model
     * @return Object
     */
    public static Object unmarshal(String response, Object model) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response, model.getClass());
        }catch(IOException e){
            return e.getMessage();
        }
    }
}
