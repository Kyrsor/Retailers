package by.itechart.retailers.service.impl;

import by.itechart.retailers.converter.ProductConverter;
import by.itechart.retailers.dto.ProductDto;
import by.itechart.retailers.dto.UserDto;
import by.itechart.retailers.entity.*;
import by.itechart.retailers.exceptions.BusinessException;
import by.itechart.retailers.repository.*;
import by.itechart.retailers.service.interfaces.CategoryService;
import by.itechart.retailers.service.interfaces.ProductService;
import by.itechart.retailers.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductConverter productConverter;
    private final ApplicationRecordRepository applicationRecordRepository;
    private final LocationProductRepository locationProductRepository;
    private final InnerApplicationRepository innerApplicationRepository;
    private final SupplierApplicationRepository supplierApplicationRepository;
    private final UserService userService;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductConverter productConverter, ApplicationRecordRepository applicationRecordRepository, LocationProductRepository locationProductRepository, InnerApplicationRepository innerApplicationRepository, SupplierApplicationRepository supplierApplicationRepository, UserService userService, CategoryRepository categoryRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.productConverter = productConverter;
        this.applicationRecordRepository = applicationRecordRepository;
        this.locationProductRepository = locationProductRepository;
        this.innerApplicationRepository = innerApplicationRepository;
        this.supplierApplicationRepository = supplierApplicationRepository;
        this.userService = userService;
        this.categoryRepository = categoryRepository;
        this.categoryService = categoryService;
    }


    @Override
    public ProductDto findById(long productId) {
        Product product = productRepository.findById(productId)
                                           .orElse(new Product());

        return productConverter.entityToDto(product);
    }

    @Override
    public List<ProductDto> findAll(Pageable pageable) {
        UserDto userDto = userService.getUser();
        Page<Product> productPage = productRepository.findAllByCustomer_IdAndAndStatus(pageable, userDto.getCustomer()
                                                                                                        .getId(), DeletedStatus.ACTIVE);

        return productConverter.entityToDto(productPage.toList());
    }

    @Override
    public ProductDto create(ProductDto productDto) throws BusinessException {
        Product product = productConverter.dtoToEntity(productDto);
        if (upcExists(product.getUpc(), product.getCustomer()
                                               .getId(), DeletedStatus.ACTIVE)) {
            throw new BusinessException("Upc should be unique");
        }
        Category category = categoryRepository.findByNameAndCustomer_Id(product.getCategory()
                                                                               .getName(), product.getCustomer()
                                                                                                  .getId());
        if (category == null) {
            category = new Category();
            category.setCustomer(product.getCustomer());
            category.setName(product.getCategory()
                                    .getName());
            product.setCategory(category);
        } else {
            product.setCategory(category);
        }

        Product persistProduct = productRepository.save(product);

        return productConverter.entityToDto(persistProduct);
    }

    @Override
    public ProductDto update(ProductDto productDto) {
        Product product = productConverter.dtoToEntity(productDto);
        Product persistProduct = productRepository.findById(product.getId())
                                                  .orElse(new Product());

        persistProduct.setCategory(product.getCategory());
        persistProduct.setLabel(product.getLabel());
        persistProduct.setUpc(product.getUpc());
        persistProduct.setVolume(product.getVolume());
        persistProduct.setCustomer(product.getCustomer());
        persistProduct = productRepository.save(persistProduct);

        return productConverter.entityToDto(persistProduct);
    }

    @Override
    public List<ProductDto> delete(List<ProductDto> productDtos) {
        List<Product> products = productConverter.dtoToEntity(productDtos);

        for (Product product : products) {
            List<ApplicationRecord> applicationRecords = applicationRecordRepository.findAllByProduct(product);
            List<LocationProduct> locationProducts = locationProductRepository.findAllByProduct(product);
            List<SupplierApplication> supplierApplications = supplierApplicationRepository.
                                                                                                  findAllByRecordsListIn(applicationRecords);
            List<InnerApplication> innerApplications = innerApplicationRepository.
                                                                                         findAllByRecordsListIn(applicationRecords);

            long locationProductCount = locationProducts.stream()
                                                        .filter(locationProduct -> (locationProduct.getAmount() != 0))
                                                        .count();
            if (locationProductCount > 0) {
                continue;
            }

            long supplierApplicationCount = supplierApplications.stream()
                                                                .filter(supplierApplication -> (supplierApplication.getApplicationStatus()
                                                                                                                   .equals(ApplicationStatus.OPEN)))
                                                                .count();
            if (supplierApplicationCount > 0) {
                continue;
            }

            long innerApplicationCount = innerApplications.stream()
                                                          .filter(innerApplication -> (innerApplication.getApplicationStatus()
                                                                                                       .equals(ApplicationStatus.OPEN)))
                                                          .count();
            if (innerApplicationCount > 0) {
                continue;
            }

            Optional<Product> productById = productRepository.findById(product.getId());
            productById.ifPresent(prod -> {
                prod.setStatus(DeletedStatus.DELETED);
                productRepository.save(prod);
                products.remove(product);
            });
        }
        return productConverter.entityToDto(products);
    }

    @Override
    public boolean upcExists(Integer upc, Long customerId, DeletedStatus status) {
        return productRepository.findByUpcAndCustomer_IdAndStatus(upc, customerId, status)
                                .isPresent();
    }
}


