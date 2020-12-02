package by.itechart.retailers.repository;

import by.itechart.retailers.entity.DeletedStatus;
import by.itechart.retailers.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByUpcAndCustomer_IdAndStatus(Integer upc,Long customerId, DeletedStatus status);

    Page<Product> findAllByCustomer_IdAndStatus(Pageable pageable, Long customerId, DeletedStatus status);
}
