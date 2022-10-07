package com.snayder.sacolaapi.models;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Endereco {

	private String cep;
	private String complemento;
	
}
