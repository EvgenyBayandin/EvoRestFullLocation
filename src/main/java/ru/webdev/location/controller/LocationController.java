package ru.webdev.location.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.webdev.location.model.Location;
import ru.webdev.location.service.LocationService;

@RestController
@RequestMapping("/location")
public class LocationController {

    @Autowired
    private LocationService service;

    @GetMapping
    public Iterable<Location> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Location> findById(@PathVariable("id") int id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<Location> save(@RequestBody Location location) throws JsonProcessingException {
        return service.save(location);
    }
}
