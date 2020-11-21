package by.itechart.retailers.controller;

import by.itechart.retailers.dto.LocationDto;
import by.itechart.retailers.service.interfaces.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public ResponseEntity findAll(Pageable pageable) {
        return new ResponseEntity<>(locationService.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/{locationId}")
    public ResponseEntity findById(@PathVariable(name = "locationId") Long locationId) {
        return new ResponseEntity<>(locationService.findById(locationId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody LocationDto locationDto) {
        return new ResponseEntity<>(locationService.create(locationDto), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity update(@RequestBody LocationDto locationDto) {
        return new ResponseEntity<>(locationService.update(locationDto), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestBody List<LocationDto> locationDtos) {
        return new ResponseEntity<>(locationService.delete(locationDtos), HttpStatus.OK);
        //TODO:вернуть List locations которые не удалиллись потому что там есть активные юзеры
        //TODO: получить фидбек
        }

}
