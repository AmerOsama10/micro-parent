package com.learn.productservice.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.learn.productservice.model.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

	@Query("{'name': 0}")
	Optional<Product> findByName(String name);
}
