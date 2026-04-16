package com.simona.earthquakeservice.service;

import com.simona.earthquakeservice.model.Earthquake;
import com.simona.earthquakeservice.repository.EarthquakeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class EarthquakeServiceTest {

    @Autowired
    private EarthquakeService earthquakeService;

    @Autowired
    private EarthquakeRepository earthquakeRepository;

    @BeforeEach
    void setup() {
        earthquakeRepository.deleteAll();
    }

    @Test
    void shouldFilterEarthquakesAfterTime() {
        Earthquake oldEq = Earthquake.builder()
                .usgsId("old")
                .magnitude(3.0)
                .magType("ml")
                .place("Old place")
                .title("Old")
                .eventTime(LocalDateTime.of(2026, 4, 15, 10, 0))
                .latitude(1.0)
                .longitude(1.0)
                .build();

        Earthquake newEq = Earthquake.builder()
                .usgsId("new")
                .magnitude(4.0)
                .magType("ml")
                .place("New place")
                .title("New")
                .eventTime(LocalDateTime.of(2026, 4, 15, 12, 0))
                .latitude(2.0)
                .longitude(2.0)
                .build();

        earthquakeRepository.save(oldEq);
        earthquakeRepository.save(newEq);

        List<Earthquake> result =
                earthquakeService.getEarthquakesAfter(
                        LocalDateTime.of(2026, 4, 15, 11, 0)
                );

        assertEquals(1, result.size());
        assertEquals("new", result.get(0).getUsgsId());
    }

    @Test
    void shouldDeleteAllBeforeInsert() {
        Earthquake eq = Earthquake.builder()
                .usgsId("test")
                .magnitude(3.0)
                .magType("ml")
                .place("Test")
                .title("Test")
                .eventTime(LocalDateTime.now())
                .latitude(1.0)
                .longitude(1.0)
                .build();

        earthquakeRepository.save(eq);

        assertEquals(1, earthquakeRepository.count());

        earthquakeRepository.deleteAll();

        assertEquals(0, earthquakeRepository.count());
    }
}