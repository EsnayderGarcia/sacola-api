package com.snayder.sacolaapi.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_sacola")
public class Sacola implements Serializable {

	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;
	
	@OneToMany(mappedBy = "sacola", cascade = CascadeType.ALL)
	@Setter(value = AccessLevel.NONE)
	private final List<Item> itensSacola = new ArrayList<>();
	
	private BigDecimal valorTotal;
	
	private Boolean fechada;
	
	@Enumerated
	private FormaPagamento formaPagamento;

	public void incrementarValorTotal(Item item) {
		var valorUnitario = item.getProduto().getValorUnitario();
		var quantidade = new BigDecimal(item.getQuantidade());
		var valorTotalItem = valorUnitario.multiply(quantidade);

		valorTotal = valorTotal.add(valorTotalItem);
	}

	public void decrementarValorTotal(Item item) {
		var valorUnitario = item.getProduto().getValorUnitario();
		var quantidade = new BigDecimal(item.getQuantidade());
		var valorTotalItem = valorUnitario.multiply(quantidade);

		valorTotal = valorTotal.subtract(valorTotalItem);
	}
	
}









