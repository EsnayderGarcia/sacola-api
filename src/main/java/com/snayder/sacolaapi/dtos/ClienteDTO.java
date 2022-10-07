package com.snayder.sacolaapi.dtos;

import java.io.Serializable;

import javax.persistence.Embedded;

import com.snayder.sacolaapi.models.Endereco;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded=true)
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	private Long id;
	
	private String nome;
	
	@Embedded
	private Endereco endereco;
	
}
