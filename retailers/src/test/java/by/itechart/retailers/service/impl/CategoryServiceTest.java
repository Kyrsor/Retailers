package by.itechart.retailers.service.impl;

import by.itechart.retailers.converter.CategoryConverter;
import by.itechart.retailers.dto.CategoryDto;
import by.itechart.retailers.dto.CustomerDto;
import by.itechart.retailers.dto.UserDto;
import by.itechart.retailers.entity.Category;
import by.itechart.retailers.entity.Customer;
import by.itechart.retailers.exceptions.BusinessException;
import by.itechart.retailers.repository.CategoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class CategoryServiceTest {

    @Mock
    CategoryRepository categoryRepository;
    @Mock
    CategoryConverter categoryConverter;
    @Mock
    UserServiceImpl userService;
    @InjectMocks
    CategoryServiceImpl categoryService;

    @Test
    public void findAllTest() {
        //given
        Long customerId = 1L;
        List<Category> categories = new ArrayList<>();
        categories.add(new Category());
        List<CategoryDto> categoryDtos = new ArrayList<>();
        categoryDtos.add(new CategoryDto());
        PageRequest pageable = PageRequest.of(0, 1);
        Page<Category> categoryPage = new PageImpl<>(categories, pageable, 1);
        CustomerDto customerDto = CustomerDto.builder()
                                             .id(customerId)
                                             .build();
        UserDto userDto = UserDto.builder()
                                 .customer(customerDto)
                                 .build();
        when(userService.getUser()).thenReturn(userDto);
        when(categoryRepository.findAllByCustomer_Id(pageable, customerId)).thenReturn(categoryPage);
        when(categoryConverter.entityToDto(categories)).thenReturn(categoryDtos);
        //when
        categoryService.findAll(pageable);
        //then
        verify(categoryRepository).findAllByCustomer_Id(pageable, customerId);
        verify(categoryConverter).entityToDto(categories);
    }

    @Test
    public void findByIdTest() {
        //given
        Category category = new Category();
        CategoryDto categoryDto = new CategoryDto();
        Long categoryId = 1L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryConverter.entityToDto(category)).thenReturn(categoryDto);
        //when
        categoryService.findById(categoryId);
        //then
        verify(categoryRepository).findById(categoryId);
        verify(categoryConverter).entityToDto(category);
    }


    @Test(expected = BusinessException.class)
    public void createTestBusinessExceptionName() {
        //given
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("name");

        Category category = new Category();
        category.setName("name");

        Long customerId = 1L;
        CustomerDto customer = CustomerDto.builder()
                                          .id(customerId)
                                          .build();
        UserDto userDto = UserDto.builder()
                                 .customer(customer)
                                 .build();
        List<Category> categories = new ArrayList<Category>() {{
            add(category);
        }};
        when(userService.getUser()).thenReturn(userDto);
        when(categoryRepository.findAllByNameAndCustomer_Id(category.getName(), customerId)).thenReturn(categories);
        //when
        categoryService.create(categoryDto);
        //then
        verify(categoryConverter).dtoToEntity(categoryDto);

    }

    @Test
    public void createTest() {
        //given
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("name");

        Category category = new Category();
        category.setName("name");

        Long customerId = 1L;
        CustomerDto customer = CustomerDto.builder()
                                          .id(customerId)
                                          .build();
        UserDto userDto = UserDto.builder()
                                 .customer(customer)
                                 .build();

        when(categoryConverter.dtoToEntity(categoryDto)).thenReturn(category);
        when(userService.getUser()).thenReturn(userDto);
        when(categoryRepository.findAllByNameAndCustomer_Id(category.getName(), customerId)).thenReturn(new ArrayList<>());
        //when
        categoryService.create(categoryDto);
        //then
        verify(categoryConverter).dtoToEntity(categoryDto);
        verify(categoryRepository).save(category);

    }


    @Test(expected = BusinessException.class)
    public void updateTestBusinessExceptionName() {
        Long customerId = 1L;
        CustomerDto customerDto = CustomerDto.builder()
                                             .id(customerId)
                                             .build();
        Customer customer = Customer.builder()
                                    .id(customerId)
                                    .build();
        CategoryDto categoryDto = CategoryDto.builder()
                                             .id(1L)
                                             .name("name")
                                             .customer(customerDto)
                                             .build();
        Category category = Category.builder()
                                    .id(1L)
                                    .name("name")
                                    .customer(customer)
                                    .build();
        UserDto userDto = UserDto.builder()
                                 .customer(customerDto)
                                 .build();
        List<Category> categories = new ArrayList<Category>() {{
            add(category);
        }};
        when(categoryConverter.dtoToEntity(categoryDto)).thenReturn(category);
        when(userService.getUser()).thenReturn(userDto);
        when(categoryRepository.findAllByNameAndCustomer_Id(category.getName(), customerId)).thenReturn(categories);
        //when
        categoryService.update(categoryDto);
        //then
        verify(categoryConverter).dtoToEntity(categoryDto);
    }

    @Test
    public void updateTest() {
        Long customerId = 1L;
        CustomerDto customerDto = CustomerDto.builder()
                                             .id(customerId)
                                             .build();
        Customer customer = Customer.builder()
                                    .id(customerId)
                                    .build();
        CategoryDto categoryDto = CategoryDto.builder()
                                             .id(1L)
                                             .name("name")
                                             .customer(customerDto)
                                             .build();
        Category category = Category.builder()
                                    .id(1L)
                                    .name("name")
                                    .customer(customer)
                                    .build();
        UserDto userDto = UserDto.builder()
                                 .customer(customerDto)
                                 .build();

        when(categoryConverter.dtoToEntity(categoryDto)).thenReturn(category);
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        //when
        categoryService.update(categoryDto);
        //then
        verify(categoryConverter).dtoToEntity(categoryDto);
        verify(categoryRepository).findById(category.getId());
        verify(categoryRepository).save(category);

    }

}
