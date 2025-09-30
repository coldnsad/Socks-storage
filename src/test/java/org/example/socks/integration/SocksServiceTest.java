package org.example.socks.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.socks.dto.SocksDto;
import org.example.socks.exception.SocksNotEnoughException;
import org.example.socks.model.Socks;
import org.example.socks.repository.SocksRepository;
import org.example.socks.service.SocksService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@AutoConfigureMockMvc
public class SocksServiceTest {

    //Использование тестовой БД в docker-контейнере
    //Контейнер поднимается/удаляется при каждом запуске/завершении тестов
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15");

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    SocksService socksService;
    @Autowired
    SocksRepository socksRepository;

    //Чистим БД после каждого теста, чтобы не было ошибок дубликатов
    @AfterEach
    public void cleanDb() {
        socksRepository.deleteAll();
    }

    @Test
    @DisplayName("Тест проверяет процесс отпуска носков, если их достаточно на складе")
    void outcomeSocks_WhenSocksIsEnough_ThenUpdateSocksEntity() throws Exception {
        //Подготовка
        SocksDto socksDto = SocksDto.builder()
                .color("green")
                .cottonPercentage(2)
                .count(3)
                .build();
        Socks createdSocks = Socks.builder()
                .color("green")
                .cottonPercentage(2)
                .count(10)
                .build();
        socksRepository.save(createdSocks);


        //Действие и проверка
        mockMvc.perform(post("/api/socks/outcome")
                        .content(objectMapper.writeValueAsString(socksDto))
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Socks updatedSocks = socksRepository.
                findByColorAndCottonPercentage(socksDto.color(), socksDto.cottonPercentage()).get();
        assertThat(updatedSocks.getCount()).isEqualTo(7);
    }

    @Test
    @DisplayName("Тест проверяет процесс отпуска носков, если их недостаточно на складе")
    void outcomeSocks_WhenSocksIsNotEnough_ThenThrowsException() {
        //Подготовка
        SocksDto socksDto = SocksDto.builder()
                .color("green")
                .cottonPercentage(2)
                .count(3)
                .build();
        Socks createdSocks = Socks.builder()
                .color("green")
                .cottonPercentage(2)
                .count(2)
                .build();
        socksRepository.save(createdSocks);

        //Действие и проверка
        assertThrows(SocksNotEnoughException.class, () -> socksService.outcomeSocks(socksDto));
    }

}
