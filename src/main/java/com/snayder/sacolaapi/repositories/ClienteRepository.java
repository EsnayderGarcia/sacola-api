package com.snayder.sacolaapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.snayder.sacolaapi.models.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {}
