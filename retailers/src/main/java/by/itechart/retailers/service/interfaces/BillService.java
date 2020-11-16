package by.itechart.retailers.service.interfaces;

import by.itechart.retailers.dto.BillDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BillService {
    BillDto findById(long billId);

    List<BillDto> findAll(Pageable pageable);

    BillDto create(BillDto billDto);

    BillDto update(BillDto billDto);
}