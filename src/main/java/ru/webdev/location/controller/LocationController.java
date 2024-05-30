package ru.webdev.location.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    RestTemplate restTemplate= new RestTemplate();

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
        // Проверяем, существует ли объект в базе данных
        if (repository.findByTitle(location.getTitle()).isPresent()) {
            // Возвращаем ошибку, если объект уже существует
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        // Запрос к внешнему сервису для получения координат
        String url = geoUrl + location.getTitle() + "&limit=" + geoLimit + "&appid=" + geoKey;
        String jsonResponse = restTemplate.getForObject(url, String.class);

        // Распарсим ответ
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        // Получаем первый элемент
        JsonNode locationNode = rootNode.get(0);

        // Извлечем координаты
        Float latitude = locationNode.get("lat").floatValue();
        Float longitude = locationNode.get("lon").floatValue();

        // Сохраняем объект в репозиторий
        Location newLocation  = new Location(location.getTitle(), latitude, longitude);
        repository.save(newLocation);

        // Возвращаем успешный ответ с созданным объектом
        return new ResponseEntity<>(newLocation, HttpStatus.CREATED);
    }

}
