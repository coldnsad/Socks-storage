package org.example.socks.service;

import org.example.socks.dto.SocksDto;
import org.example.socks.dto.filter.SocksFilterDto;
import org.example.socks.model.Socks;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SocksService {
    Socks addSocks(SocksDto socksDto);

    List<SocksDto> getSocks(SocksFilterDto socksFilterDto, Pageable pageable);

    void outcomeSocks(SocksDto socksDto);

    SocksDto updateSocks(Long id, SocksDto socksDto);

    void saveFromBatch(MultipartFile file);
}
