package org.example.socks.dto.filter;

import jakarta.validation.constraints.Min;

public record SocksFilterDto(
        String color,
        @Min(0)
        Integer moreThanCount,
        @Min(0)
        Integer lessThanCount,
        @Min(0)
        Integer moreThanCotton,
        @Min(0)
        Integer lessThanCotton
) { }
