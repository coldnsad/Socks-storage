package org.example.socks.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.socks.Mapper.SocksMapper;
import org.example.socks.dto.SocksDto;
import org.example.socks.dto.filter.SocksFilterDto;
import org.example.socks.exception.SocksNotEnoughException;
import org.example.socks.exception.SocksNotFoundException;
import org.example.socks.exception.WrongExcelFileException;
import org.example.socks.model.Socks;
import org.example.socks.repository.SocksRepository;
import org.example.socks.service.SocksService;
import org.example.socks.util.excel.ExcelUtil;
import org.example.socks.util.specification.SocksSpecificationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocksServiceImpl implements SocksService {

    private final SocksRepository socksRepository;
    private final SocksMapper socksMapper;

    @Override
    public Socks addSocks(SocksDto socksDto) {
        log.info("Creating socks ...");
        Optional<Socks> existingSocks = socksRepository
                .findByColorAndCottonPercentage(socksDto.color(), socksDto.cottonPercentage());
        if(existingSocks.isPresent()){
            existingSocks.get().setCount(socksDto.count());
            log.info("Socks with id {} updated successfully",  existingSocks.get().getId());
            return socksRepository.save(existingSocks.get());
        }
        Socks socks = socksMapper.toEntity(socksDto);
        log.info("Created new socks with id {}",  socks.getId());
        return socksRepository.save(socks);
    }

    @Override
    public List<SocksDto> getSocks(SocksFilterDto socksFilterDto, Pageable pageable) {
        log.info("Getting socks ...");
        Specification<Socks> specification = SocksSpecificationUtil.build(socksFilterDto);
        Page<Socks> socksPages = socksRepository.findAll(specification, pageable);
        List<Socks> socksList = socksPages.getContent();
        return socksMapper.toDtos(socksList);
    }

    @Override
    public void outcomeSocks(SocksDto socksDto) {
        log.info("Outcoming socks ...");
        Socks socks = socksRepository.
                findByColorAndCottonPercentage(socksDto.color(), socksDto.cottonPercentage())
                .orElseThrow(SocksNotFoundException::new);
        if (socks.getCount() > 0 && (socks.getCount() - socksDto.count() > 0)) {
            socks.setCount(socks.getCount() - socksDto.count());
            socksRepository.save(socks);
        } else {
            throw new SocksNotEnoughException("Socks arent enough for outcome");
        }
    }

    @Override
    public SocksDto updateSocks(Long id, SocksDto socksDto) {
        log.info("Updating socks ...");
        Socks socks = socksRepository
                .findById(id)
                .orElseThrow(SocksNotFoundException::new);
        socks.setColor(socksDto.color());
        socks.setCottonPercentage(socksDto.cottonPercentage());
        socks.setCount(socksDto.count());
        return socksMapper.toDto(socksRepository.save(socks));
    }

    @Override
    public void saveFromBatch(MultipartFile file) {
        if (!ExcelUtil.isExcelFile(file)) {
            throw new WrongExcelFileException("Wrong file format");
        }
        log.info("Batch fileName is {}", file.getOriginalFilename());
        List<Socks> socksFromExcel = ExcelUtil.getSocksFromFile(file);
        log.info("socksFromExcel is {}", socksFromExcel);
        socksRepository.saveAll(socksFromExcel);
    }
}
