package com.springsecurity.service;

import com.springsecurity.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Long save(User user);
    User update(User user);
    void deleteById(Long id);
    Optional<User> findById(Long id);
    List<User> findAll();
    Page<User> findAllPaginated(Pageable pageable);
    String verify(User user);
}
