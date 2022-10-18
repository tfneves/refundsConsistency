package com.mercadolibre.refunds_consistency.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@Component
public class Contingency {

    private ArrayList<String> list;
    private String status;
}
