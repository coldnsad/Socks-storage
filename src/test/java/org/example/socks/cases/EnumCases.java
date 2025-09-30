package org.example.socks.cases;

import org.springframework.http.HttpStatus;


public enum EnumCases {
    POSITIVE_CASE(
            1L,
            HttpStatus.OK
    ),
    NEGATIVE_CASE(
            -2L,
            HttpStatus.BAD_REQUEST
    );

    private final Long id;
    private final HttpStatus status;

    EnumCases(Long id, HttpStatus status) {
        this.id = id;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
