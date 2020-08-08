package com.example.demo.repository;

import com.example.demo.domain.Email;
import org.springframework.data.repository.CrudRepository;

public interface EmailRepository extends CrudRepository<Email, Long> {

}
