package by.itechart.retailers.service.impl;

import by.itechart.retailers.converter.CustomerConverter;
import by.itechart.retailers.converter.UserConverter;
import by.itechart.retailers.dto.CustomerDto;
import by.itechart.retailers.dto.UserDto;
import by.itechart.retailers.entity.Customer;
import by.itechart.retailers.entity.Role;
import by.itechart.retailers.entity.Status;
import by.itechart.retailers.entity.User;
import by.itechart.retailers.repository.UserRepository;
import by.itechart.retailers.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserConverter userConverter;
    private CustomerConverter customerConverter;

    private BCryptPasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserConverter userConverter, CustomerConverter customerConverter, @Lazy BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.customerConverter = customerConverter;
        this.encoder = encoder;
    }


    @Override
    @Transactional
    public UserDto getUser() {
        Authentication authentication = SecurityContextHolder.getContext()
                                                             .getAuthentication();
        String currentPrincipalName = authentication.getName();
        return findByEmail(currentPrincipalName);
    }

    @Override
    @Transactional
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return userConverter.entityToDto(user);
    }

    @Override
    public String generatePassword() {
        String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String specialCharacters = "!@#$";
        String numbers = "1234567890";
        String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
        Random random = new Random();
        char[] charPassword = new char[8];

        charPassword[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
        charPassword[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
        charPassword[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
        charPassword[3] = numbers.charAt(random.nextInt(numbers.length()));

        for (int i = 0; i < 8; i++) {
            charPassword[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
        }
        return new String(charPassword);
    }

    @Override
    public String encodePassword(String password) {
        return encoder.encode(password);
    }

    @Override
    public UserDto findById(long userId) {
        User user = userRepository.findById(userId)
                                  .orElse(new User());

        return userConverter.entityToDto(user);
    }

    @Override
    public List<UserDto> findAll() {
        List<User> userList = userRepository.findAll();

        return userConverter.entityToDto(userList);
    }

    @Override
    public List<UserDto> findAllByCustomerId() {
        UserDto userDto = getUser();
        List<User> customerEmployeesList = userRepository.findAllByCustomer_Id(userDto.getCustomer()
                                                                                      .getId());
        return userConverter.entityToDto(customerEmployeesList);
    }

    @Override
    public UserDto create(UserDto userDto) {
        User user = userConverter.dtoToEntity(userDto);
        User persistUser = userRepository.save(user);

        return userConverter.entityToDto(persistUser);
    }

    @Override
    public UserDto create(CustomerDto customerDto) {
        Customer customer = customerConverter.dtoToEntity(customerDto);
        User user = new User();
        user.setEmail(customerDto.getEmail());
        user.setCustomer(customer);
        String password=generatePassword();
        user.setPassword(encodePassword(password));
        user.setUserStatus(Status.ACTIVE);
        user.setUserRole(Collections.singletonList(Role.ADMIN));

        userRepository.save(user);
        user.setPassword(password);
        return userConverter.entityToDto(user);
    }

    @Override
    public UserDto update(UserDto userDto) {
        User user = userConverter.dtoToEntity(userDto);
        User persistUser = userRepository.findById(user.getId())
                                         .orElse(new User());

        persistUser.setAddress(user.getAddress());
        persistUser.setBirthday(user.getBirthday());
        persistUser.setEmail(user.getEmail());
        persistUser.setFirstName(user.getFirstName());
        persistUser.setLastName(user.getLastName());
        persistUser.setPassword(user.getPassword());
        persistUser.setUserRole(user.getUserRole());
        persistUser.setUserStatus(user.getUserStatus());
        persistUser.setCustomer(user.getCustomer());
        persistUser.setLocation(user.getLocation());

        return userConverter.entityToDto(persistUser);
    }
}
