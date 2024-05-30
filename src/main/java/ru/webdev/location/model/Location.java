package ru.webdev.location.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Location {

    @Id
    @GeneratedValue
    private int id;

    @NonNull
    private String title;

    @ElementCollection
    private List<String> localNames = new ArrayList<>();

    @NonNull
    private Float latitude;

    @NonNull
    private Float longitude;

    public Location(@NonNull String title) {
        this.title = title;
    }

    public Location(@NonNull String title, @NonNull Float latitude, @NonNull Float longitude, @NonNull List<String> localNames) {
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.localNames  = localNames;
    }
}
