package com.simona.earthquakeservice;
import com.simona.earthquakeservice.model.Earthquake;
import com.simona.earthquakeservice.service.EarthquakeService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/earthquakes")
@CrossOrigin(origins = "http://localhost:5173")
public class EarthquakeController {

    private final EarthquakeService earthquakeService;

    public EarthquakeController(EarthquakeService earthquakeService) {
        this.earthquakeService = earthquakeService;
    }

    @GetMapping("/fetch")
    public String fetch() throws Exception {
        earthquakeService.fetchAndStoreEarthquakes();
        return "Data fetched and stored!";
    }

    @GetMapping
    public List<Earthquake> getAll() {
        return earthquakeService.getAllEarthquakes();
    }

    @GetMapping("/after")
    @CrossOrigin(origins = "http://localhost:5173")
    public List<Earthquake> getAfter(@RequestParam LocalDateTime time) {
        return earthquakeService.getEarthquakesAfter(time);
    }

    @DeleteMapping("/{id}")
    public String deleteEarthquake(@PathVariable Long id) {
        earthquakeService.deleteEarthquakeById(id);
        return "Earthquake with id " + id + " was deleted successfully.";
    }
}