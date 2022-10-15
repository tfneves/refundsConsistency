package com.mercadolibre.refunds_consistency.constants;

import lombok.Getter;

public enum FinalStatus {
    SOLVED_INCONSISTENCY("Resolve inconsistency"),
    READY_BPO_REFUND("Request BPO to refund"),
    SOLVE_CONTINGENCY("Resolve contingencies"),
    PAGO_ROTO("Broken payment");

    @Getter
    private final String finalStatus;

    FinalStatus(String finalsStatus) {
        this.finalStatus = finalsStatus;
    }
}
