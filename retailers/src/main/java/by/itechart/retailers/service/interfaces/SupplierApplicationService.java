package by.itechart.retailers.service.interfaces;

import by.itechart.retailers.dto.SupplierApplicationDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SupplierApplicationService {
    SupplierApplicationDto findById(long supplierApplicationId);

    List<SupplierApplicationDto> findAll(Pageable pageable);

    SupplierApplicationDto create(SupplierApplicationDto supplierApplicationDto);

    SupplierApplicationDto update(SupplierApplicationDto supplierApplicationDto);
}