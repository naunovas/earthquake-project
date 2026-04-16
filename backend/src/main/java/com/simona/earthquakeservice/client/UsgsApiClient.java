package com.simona.earthquakeservice.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UsgsApiClient {

    private final String URL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_hour.geojson";

    public String fetchEarthquakes() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(URL, String.class);
    }
}