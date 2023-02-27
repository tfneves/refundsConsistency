package com.mercadolibre.refunds_consistency.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContingencieDetails {

    private String contingencies;
    private String contingencie_status;
}
