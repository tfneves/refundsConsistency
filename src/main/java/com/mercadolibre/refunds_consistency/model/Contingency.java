package com.mercadolibre.refunds_consistency.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class Contingency {

    private ArrayList<String> list;
    private String status;
}
