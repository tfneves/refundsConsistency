package com.mercadolibre.refunds_consistency.service;

import com.mercadolibre.refunds_consistency.constants.HeadersName;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ConnectionService {

    private Map<String, String> requestHeaders;

    /**
     * Realiza chamada para a URI informada
     * @param urlRequest
     * @param httpMethodRequest
     * @param headers
     * @return ResponseEntity
     */
    public ResponseEntity doRequestApi(String urlRequest, HttpMethod httpMethodRequest, String... headers) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<?> httpEntity = new HttpEntity<>(this.setHeaders(headers));
        try{
            return restTemplate.exchange(urlRequest, httpMethodRequest, httpEntity, String.class);
        }catch(RestClientException e){
            return null;
        }
    }

    /**
     * Define os headers necessarios para a requisicao
     * @param headers
     * @return HttpHeaders
     */
    private HttpHeaders setHeaders(String... headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        for(String header : headers){
            httpHeaders.set(header, this.requestHeaders.get(header));
        }
        return httpHeaders;
    }

    /**
     * Recupera os headers enviados na requisicao feita pelo cliente
     * e padroniza seus nomes para upper case
     * @param requestHeaders
     * @return void
     */
    public void getClientHeaders(Map<String, String> requestHeaders) {
        Map<String, String> toUpperCaseHeaders = new HashMap<>();
        requestHeaders.forEach((key, value) -> {
            if(key.equalsIgnoreCase(HeadersName.ONE_SOURCE_COOKIE_HEADER)){
                toUpperCaseHeaders.put(key.toUpperCase(), "session_id="+value);
            }else{
                toUpperCaseHeaders.put(key.toUpperCase(), value);
            }
        });
        this.requestHeaders = toUpperCaseHeaders;
    }
}
