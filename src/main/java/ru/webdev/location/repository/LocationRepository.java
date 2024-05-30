package ru.webdev.location.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.webdev.location.model.Location;

@Repository
public interface LocationRepository extends CrudRepository<Location, Integer> {

    Optional<Location> findByTitle(String title);

    Optional<Location> findByLocalNamesContainingIgnoreCase(String title);

}
