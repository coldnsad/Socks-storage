package org.example.socks.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.socks.dto.SocksDto;
import org.example.socks.dto.exception.ErrorResponseDto;
import org.example.socks.dto.filter.FilteredSocksDto;
import org.example.socks.dto.filter.SocksFilterDto;
import org.example.socks.service.SocksService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/socks")
@Validated
public class SocksController {

    private final SocksService socksService;

    @Operation(summary = "Получение списка носков с учётом фильтрации и пагинации")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Получен пустой/непустой массив данных",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SocksDto.class))
                            )
                    }
            )
    })
    @Tag(name = "Get")
    @GetMapping()
    public ResponseEntity<FilteredSocksDto> getSocks(@ParameterObject SocksFilterDto socksFilterDto, @ParameterObject Pageable pageable) {
        FilteredSocksDto filteresReult = socksService.getSocks(socksFilterDto, pageable);
        return ResponseEntity.ok().body(filteresReult);
    }

    @Tag(name = "Post")
    @PostMapping("/income")
    @SneakyThrows
    public ResponseEntity<Void> incomeSocks(@RequestBody SocksDto socksDto) {
        socksService.addSocks(socksDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Tag(name = "Post")
    @PostMapping("/outcome")
    public ResponseEntity<Void> outcomeSocks(@RequestBody SocksDto socksDto) {
        socksService.outcomeSocks(socksDto);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "Put")
    @PutMapping("{id}")
    public ResponseEntity<SocksDto> updateSocks(@Min(0) @PathVariable Long id, @RequestBody SocksDto socksDto) {
        return ResponseEntity.ok(socksService.updateSocks(id, socksDto));
    }

    @Operation(summary = "Загрузка носков из файла Excel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные загружены успешно",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)
                            )
                    }
            ),
            @ApiResponse(responseCode = "415", description = "Неверный формат файла,",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            )
                    }
            )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Файл в формате Excel",
            required = true)
    @Tag(name = "Post")
    @PostMapping(value = "/batch", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> incomeSocksBatch(@RequestParam("file") MultipartFile file) {
        socksService.saveFromBatch(file);
        return ResponseEntity.ok(file.getOriginalFilename());
    }
}
