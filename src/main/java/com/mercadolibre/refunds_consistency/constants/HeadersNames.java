package com.mercadolibre.refunds_consistency.constants;

import lombok.Getter;

public enum HeadersNames {
    FURY_HEADER("X-AUTH-TOKEN"),
    ONE_SOURCE_COOKIE_HEADER("COOKIE");

    @Getter
    private String headerName;

    HeadersNames(String headerName) {
        this.headerName = headerName;
    }
}
