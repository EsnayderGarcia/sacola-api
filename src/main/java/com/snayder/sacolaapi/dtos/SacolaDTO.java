package com.snayder.sacolaapi.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.snayder.sacolaapi.enums.FormaPagamento;

import lombok.AccessLevel;
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
public class SacolaDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	private Long id;
	
	private ClienteDTO cliente;
	
	@Setter(value = AccessLevel.NONE)
	private final List<ItemDTO> itensSacola = new ArrayList<>();
	
	private BigDecimal valorTotal;
	
	private Boolean fechada;
	
	private FormaPagamento formaPagamento;
	
}









