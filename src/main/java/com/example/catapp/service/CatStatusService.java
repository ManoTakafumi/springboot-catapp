package com.example.catapp.service;

import com.example.catapp.entity.Cat;
import com.example.catapp.repository.CatRepository;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatStatusService {

    private final CatRepository catRepository;

    public CatStatusService(CatRepository catRepository) {
        this.catRepository = catRepository;
    }

    @Scheduled(fixedRate = 10000)
    public void decreaseStatus() {

        List<Cat> cats = catRepository.findAll();

        for (Cat cat : cats) {

            cat.setHunger(
                    Math.max(cat.getHunger() - 5, 0)
            );

            if (cat.getHunger() == 0) {

                cat.setStamina(
                        Math.max(cat.getStamina() - 5, 0)
                );
            }

            if (cat.getEnergy() == 0) {

                cat.setStamina(
                        Math.max(cat.getStamina() - 5, 0)
                );
            }

            cat.setEnergy(
                    Math.max(cat.getEnergy() - 3, 0)
            );

            catRepository.save(cat);
        }

        System.out.println("猫ステータス更新");
    }
}
