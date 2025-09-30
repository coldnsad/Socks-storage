package org.example.socks.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.socks.cases.EnumCases;
import org.example.socks.controller.SocksController;
import org.example.socks.dto.SocksDto;
import org.example.socks.dto.filter.FilteredSocksDto;
import org.example.socks.dto.filter.SocksFilterDto;
import org.example.socks.model.Socks;
import org.example.socks.service.SocksService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SocksController.class)
public class SocksControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockitoBean
    SocksService socksService;

    @Test
    @DisplayName("Тест проверяет работу Endpoint прихода носков")
    void incomeSocks_WhenValidRequest_ReturnsCreatedStatus() throws Exception {
        //Подготовка
        SocksDto socksDto = SocksDto.builder()
                .color("green")
                .cottonPercentage(2)
                .count(3)
                .build();
        Socks createdSocks = Socks.builder()
                .id(1L)
                .color("green")
                .cottonPercentage(2)
                .count(3)
                .build();

        when(socksService.addSocks(any(SocksDto.class))).thenReturn(createdSocks);

        //Действие и проверка
        mockMvc.perform(post("/api/socks/income")
                        .content(objectMapper.writeValueAsString(socksDto))
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Тест проверяет работу Endpoint получения носков с фильтрацией и пагинацией")
    void getSocks_WhenValidRequest_ReturnsOkStatusAndListOfPageSocksDto() throws Exception {
        //Подготовка
        SocksDto socksDto1 = SocksDto.builder()
                .color("green")
                .cottonPercentage(4)
                .count(5)
                .build();
        SocksDto socksDto2 = SocksDto.builder()
                .color("green")
                .cottonPercentage(2)
                .count(3)
                .build();
        List<SocksDto> socksDtos = List.of(socksDto1, socksDto2);
        FilteredSocksDto result = FilteredSocksDto.builder()
                .content(socksDtos)
                .pageNumber(0)
                .elementsPerPage(2)
                .countElements(2)
                .countPages(1)
                .build();

        when(socksService.getSocks(any(SocksFilterDto.class), any(Pageable.class))).thenReturn(result);

        //Действие и проверка
        mockMvc.perform(get("/api/socks")
                        .param("page", "0")
                        .param("size", "2")
                        .param("color", "green")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    //Параметризованный тест, используется для удобства, уменьшение кода
    @ParameterizedTest
    @EnumSource(EnumCases.class)
    @DisplayName("Тест проверяет возвращаемый статус при обновлении носков")
    void updateSocks_ReturnsOkOrBadRequestStatus(EnumCases cases) throws Exception {
        //Подготовка
        SocksDto socksDto = SocksDto.builder()
                .color("green")
                .cottonPercentage(4)
                .count(5)
                .build();

        //Действие
        MvcResult mvcResult = mockMvc.perform(put("/api/socks/{id}", cases.getId())
                        .content(objectMapper.writeValueAsString(socksDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        //Проверка
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(cases.getStatus().value());

    }

}
