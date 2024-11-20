package com.luizsoftware.createUrlShortner;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Main implements RequestHandler<Map<String, Object>, Map<String, String>> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    //Método handleRequest é a forma padrão que a AWS Lambda procura na hora da requisição, porém é totalmente editável
    @Override
    public Map<String, String> handleRequest(Map<String, Object> input, Context context) {
        //pegando o corpo da requisição que é um objeto
        String body= (String) input.get("body");

        //Variavel para mapear o body
        Map<String, String> bodyMap;
        try{
            //Aqui há uma tentativa de mapear o body, transformar ele em chave valor, passamos o objeto e o tipo do objeto que queremos transformar
            bodyMap = objectMapper.readValue(body, Map.class);

        //Try/catch necessário para tratar as exceções que pode ser gerada com a conversão do bodyMap
        }catch (JsonProcessingException exception){
            throw new RuntimeException("Error parsing JSON body: " + exception.getMessage(), exception);
        }

        //Extraindo os valores passados no bodyMap
        String originalUrl = bodyMap.get("originalUrl");
        String expirationTime = bodyMap.get("expirationTime");

        //Gerador de UUID próprio
        String shortUrlCode = UUID.randomUUID().toString().substring(0,8);

        //Resposta provisória, forncese um code junto da hash aleatória criada com o UUID
        Map<String,String> response = new HashMap<>();
        response.put("code", shortUrlCode);

        return response;
    }
}