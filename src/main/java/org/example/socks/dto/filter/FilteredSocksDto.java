package org.example.socks.dto.filter;

import lombok.Builder;
import org.example.socks.dto.SocksDto;

import java.util.List;

@Builder
public record FilteredSocksDto(
        List<SocksDto> content,
        int pageNumber,
        int elementsPerPage,
        int countPages,
        long countElements
) {
}
