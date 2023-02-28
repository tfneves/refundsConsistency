package com.mercadolibre.refunds_consistency.constants;

import lombok.Getter;

public enum HeadersNames {
    TIGER_TOKEN_HEADER("X-Tiger-Token"),
    X_CALLER_SCOPES_HEADER("X-Caller-Scopes"),
    HIDDEN_REFUNDS("X-Render-Hidden-Refunds");

    @Getter
    private String headerName;

    HeadersNames(String headerName) {
        this.headerName = headerName;
    }
}
