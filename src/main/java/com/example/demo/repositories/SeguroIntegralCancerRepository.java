package com.example.demo.repositories;

import com.example.demo.entities.SeguroIntegralCancer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SeguroIntegralCancerRepository extends MongoRepository<SeguroIntegralCancer, String> {
    //Optional<SeguroIntegralCancer> findById(String id);
}
