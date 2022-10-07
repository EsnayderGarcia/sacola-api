package com.snayder.sacolaapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.snayder.sacolaapi.models.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {}
