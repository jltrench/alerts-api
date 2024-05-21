package com.jlt.security.alert;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AlertRepository extends JpaRepository<Alert, Integer> {
    List<Alert> findAllByAuthor(String author);
    Optional<Alert> findByIdAndAuthor(Integer id, String author);
}