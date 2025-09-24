package org.example.socks.Mapper;

import org.example.socks.dto.SocksDto;
import org.example.socks.model.Socks;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SocksMapper {
    SocksDto toDto(Socks socks);
    Socks toEntity(SocksDto socksDto);
    List<SocksDto> toDtos(List<Socks> socksList);
}
