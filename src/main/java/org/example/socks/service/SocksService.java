package org.example.socks.service;

import org.example.socks.dto.SocksDto;
import org.example.socks.dto.filter.FilteredSocksDto;
import org.example.socks.dto.filter.SocksFilterDto;
import org.example.socks.model.Socks;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface SocksService {
    Socks addSocks(SocksDto socksDto);

    FilteredSocksDto getSocks(SocksFilterDto socksFilterDto, Pageable pageable);

    void outcomeSocks(SocksDto socksDto);

    SocksDto updateSocks(Long id, SocksDto socksDto);

    void saveFromBatch(MultipartFile file);
}
