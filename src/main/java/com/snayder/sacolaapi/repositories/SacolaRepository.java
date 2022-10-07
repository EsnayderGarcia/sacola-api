package com.snayder.sacolaapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.snayder.sacolaapi.models.Sacola;

@Repository
public interface SacolaRepository extends JpaRepository<Sacola, Long> {}
