package com.simona.earthquakeservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "earthquakes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Earthquake {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String usgsId;

    private Double magnitude;

    private String magType;

    private String place;

    private String title;

    private LocalDateTime eventTime;

    private Double latitude;

    private Double longitude;
}