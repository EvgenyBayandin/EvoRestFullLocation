package ru.webdev.location.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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

    public Location(@NonNull String title) {
        this.title = title;
    }
}
