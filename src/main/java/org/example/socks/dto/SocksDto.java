package org.example.socks.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SocksDto(
        Long id,
        @NotNull
        String color,
        @Min(1)
        @Max(100)
        @NotNull
        Integer cottonPercentage,
        @Min(1)
        @NotNull
        Integer count
) { }
