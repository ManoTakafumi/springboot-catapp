package com.example.catapp.repository;

import com.example.catapp.entity.Cat;
import com.example.catapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CatRepository extends JpaRepository<Cat, Long> {

    List<Cat> findByUser(User user);
}
