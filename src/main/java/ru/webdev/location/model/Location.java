package ru.webdev.location.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class Location {

    @Id
    @GeneratedValue
    private int id;

    @NonNull
    private String name;

    @ElementCollection
    private List<String> localNames = new ArrayList<>();

    @NonNull
    private Float latitude;

    @NonNull
    private Float longitude;


    public Location(@NonNull String name, @NonNull Float latitude, @NonNull Float longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location(@NonNull List<String> localNames) {
        this.localNames = localNames;
    }
}
