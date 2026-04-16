package com.simona.earthquakeservice.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simona.earthquakeservice.client.UsgsApiClient;
import com.simona.earthquakeservice.model.Earthquake;
import com.simona.earthquakeservice.repository.EarthquakeRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.time.LocalDateTime;
import java.util.List;
import com.simona.earthquakeservice.exception.ResourceNotFoundException;

@Service
public class EarthquakeService {

    private final UsgsApiClient client;
    private final EarthquakeRepository repository;
    private final ObjectMapper objectMapper;

    public EarthquakeService(UsgsApiClient client, EarthquakeRepository repository) {
        this.client = client;
        this.repository = repository;
        this.objectMapper = new ObjectMapper();
    }

    public void fetchAndStoreEarthquakes() throws Exception {

        String json = client.fetchEarthquakes();

        JsonNode root = objectMapper.readTree(json);
        JsonNode features = root.get("features");

        // izbrishi stari zapisi
        repository.deleteAll();

        for (JsonNode feature : features) {

            JsonNode properties = feature.get("properties");
            JsonNode geometry = feature.get("geometry");

            Double magnitude = properties.get("mag") != null && !properties.get("mag").isNull()
                    ? properties.get("mag").asDouble()
                    : null;

            String magType = properties.get("magType") != null && !properties.get("magType").isNull()
                    ? properties.get("magType").asText()
                    : "unknown";

            String place = properties.get("place") != null && !properties.get("place").isNull()
                    ? properties.get("place").asText()
                    : "unknown location";

            String title = properties.get("title") != null && !properties.get("title").isNull()
                    ? properties.get("title").asText()
                    : "unknown title";

            if (properties.get("time") == null || properties.get("time").isNull()) {
                continue;
            }

            long timeMillis = properties.get("time").asLong();
            LocalDateTime eventTime = Instant.ofEpochMilli(timeMillis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();


            if (geometry == null || geometry.get("coordinates") == null || geometry.get("coordinates").size() < 2) {
                continue;
            }
            JsonNode coordinates = geometry.get("coordinates");


            Double longitude = coordinates.get(0).asDouble();
            Double latitude = coordinates.get(1).asDouble();

            // za filter: magnitude > 2.0
            if (magnitude == null || magnitude <= 2.0) continue;

            Earthquake eq = Earthquake.builder()
                    .usgsId(feature.get("id").asText())
                    .magnitude(magnitude)
                    .magType(magType)
                    .place(place)
                    .title(title)
                    .eventTime(eventTime)
                    .latitude(latitude)
                    .longitude(longitude)
                    .build();

            repository.save(eq);
        }

    }
    public List<Earthquake> getAllEarthquakes() {
        return repository.findAll();
    }
    public List<Earthquake> getEarthquakesAfter(LocalDateTime time) {
        return repository.findByEventTimeAfter(time);
    }
    public void deleteEarthquakeById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Earthquake with id " + id + " not found");
        }

        repository.deleteById(id);
    }
}