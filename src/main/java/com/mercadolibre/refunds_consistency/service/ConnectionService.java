package com.mercadolibre.refunds_consistency.service;

import com.mercadolibre.refunds_consistency.constants.ConnectionConstants;
import com.mercadolibre.refunds_consistency.constants.HeadersNames;
import com.mercadolibre.refunds_consistency.dto.RequestResponse;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import java.util.HashMap;
import java.util.Map;

@Service
public class ConnectionService {

    private Map<String, String> requestHeaders;

    /**
     * Realiza chamada sem payload para a URI informada
     * @param urlRequest
     * @param httpMethodRequest
     * @param headers
     * @return ResponseEntity
     */
    public ResponseEntity doRequestApi(String urlRequest, HttpMethod httpMethodRequest, String... headers) {
        RestTemplate restTemplate = this.restTemplateWithTimeout(ConnectionConstants.TIMEOUT_REQUEST);
        HttpHeaders requestHeaders = this.setHeaders(headers);
        HttpEntity<?> httpEntity = new HttpEntity<>(requestHeaders);
        try{
            return restTemplate.exchange(urlRequest, httpMethodRequest, httpEntity, String.class);
        }catch(HttpClientErrorException | HttpServerErrorException | ResourceAccessException exceptionRequest) {
            if(exceptionRequest instanceof ResourceAccessException &&
                ((ResourceAccessException) exceptionRequest).getCause()
                    .getCause().getMessage().equals(ConnectionConstants.TIMEOUT_MESSAGE)
            ) {
                RequestResponse.statusResponse = HttpStatus.REQUEST_TIMEOUT;
            }else{
                RequestResponse.statusResponse = ((HttpStatusCodeException) exceptionRequest).getStatusCode();
            }
            return null;
        }
    }

    /**
     * Realiza chamada com payload para a URI informada
     * @param urlRequest
     * @param httpMethodRequest
     * @param headers
     * @return ResponseEntity
     */
    public ResponseEntity doRequestApi(String urlRequest, HttpMethod httpMethodRequest, Object payloadEntity, String... headers) {
        RestTemplate restTemplate = this.restTemplateWithTimeout(ConnectionConstants.TIMEOUT_REQUEST);
        HttpEntity<?> httpEntity = new HttpEntity<>(payloadEntity, this.setHeaders(headers));
        try{
            return restTemplate.exchange(urlRequest, httpMethodRequest, httpEntity, String.class);
        }catch(HttpClientErrorException | HttpServerErrorException | ResourceAccessException exceptionRequest) {
            if(exceptionRequest instanceof ResourceAccessException
                && ((ResourceAccessException) exceptionRequest).getCause()
                    .getCause().getMessage().equals(ConnectionConstants.TIMEOUT_MESSAGE)
            ) {
                RequestResponse.statusResponse = HttpStatus.REQUEST_TIMEOUT;
            }else{
                RequestResponse.statusResponse = ((HttpStatusCodeException) exceptionRequest).getStatusCode();
            }
            return null;
        }
    }

    /**
     * Cria custom RestTemplate com timeout de requisição definido
     * @param timeout
     * @return RestTemplate
     */
    private RestTemplate restTemplateWithTimeout(Integer timeout) {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(timeout);
        httpRequestFactory.setConnectTimeout(timeout);
        httpRequestFactory.setReadTimeout(timeout);
        return new RestTemplate(httpRequestFactory);
    }

    /**
     * Define os headers necessarios para a requisicao
     * @param headers
     * @return HttpHeaders
     */
    private HttpHeaders setHeaders(String... headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
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
            if(key.equalsIgnoreCase(HeadersNames.ONE_SOURCE_COOKIE_HEADER.getHeaderName())){
                toUpperCaseHeaders.put(key.toUpperCase(), "session_id="+value);
            }else{
                toUpperCaseHeaders.put(key.toUpperCase(), value);
            }
        });
        this.requestHeaders = toUpperCaseHeaders;
    }
}
