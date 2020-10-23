package by.itechart.retailers.service.impl;

import by.itechart.retailers.converter.CustomerConverter;
import by.itechart.retailers.dto.LocationItemDto;
import by.itechart.retailers.service.LocationItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationItemServiceImpl implements LocationItemService {

    private LocationItemRepository locationItemRepository;
    private CustomerConverter customerConverter;

    @Autowired
    public LocationItemServiceImpl(LocationItemRepository locationItemRepository, CustomerConverter customerConverter) {
        this.locationItemRepository = locationItemRepository;
        this.customerConverter = customerConverter;
    }

    @Override
    public LocationItemDto findById(long locationItemId) {
        return null;
    }

    @Override
    public List<LocationItemDto> findAll() {
        return null;
    }

    @Override
    public LocationItemDto create(LocationItemDto locationItemDto) {
        return null;
    }

    @Override
    public LocationItemDto update(LocationItemDto locationItemDto) {
        return null;
    }
}