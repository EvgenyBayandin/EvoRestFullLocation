package ru.webdev.location.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.webdev.location.model.Location;
import ru.webdev.location.repository.LocationRepository;

@RestController
@RequestMapping("/location")
public class LocationController {

    @Value("${geo.url}")
    private String geoUrl;

    @Value("${geo.limit}")
    private int geoLimit;

    @Value("${geo.key}")
    private String geoKey;

    @Autowired
    private LocationRepository repository;

    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    public Iterable<Location> findAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Location> findById(@PathVariable("id") int id) {
        return repository.findById(id);
    }

    @PostMapping
    public ResponseEntity<Location> save(@RequestBody Location location) throws JsonProcessingException {
        String url = geoUrl + location.getName() + "&limit=" + geoLimit + "&appid=" + geoKey;
        String jsonResponse = restTemplate.getForObject(url, String.class);

        // Распарсим ответ
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        if (rootNode.isArray() && rootNode.size() > 0) {
            JsonNode locationNode = rootNode.get(0);

            String name = locationNode.get("name").asText();
            // Вернем координаты и локальные названия если объект уже существует, иначе создаем новый объект
            Optional<Location> existingLocation = repository.findByNameIgnoreCase(name);
            if (existingLocation.isPresent()) {
                return new ResponseEntity(existingLocation, HttpStatus.OK);
            }

            Float latitude = locationNode.get("lat").floatValue();
            Float longitude = locationNode.get("lon").floatValue();
            JsonNode localNamesNode = locationNode.get("local_names");
            List<String> localNames = new ArrayList<>();
            localNamesNode.fieldNames().forEachRemaining(fieldName -> {
                String localName = localNamesNode.get(fieldName).textValue();
                localNames.add(localName);
            });

            Location newLocation = new Location(name, latitude, longitude);
            newLocation.setLocalNames(localNames);
            repository.save(newLocation);

            return new ResponseEntity<>(newLocation, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
