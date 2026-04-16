package com.simona.earthquakeservice.repository;

import com.simona.earthquakeservice.model.Earthquake;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EarthquakeRepository extends JpaRepository<Earthquake, Long> {
    List<Earthquake> findByEventTimeAfter(LocalDateTime time);
}