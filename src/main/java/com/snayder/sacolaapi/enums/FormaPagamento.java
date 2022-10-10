package com.snayder.sacolaapi.enums;

import com.snayder.sacolaapi.services.exceptions.SacolaApiException;

public enum FormaPagamento {
	
	DINHEIRO(0), PIX(1), MAQUINA(2);
	
	private int valor;
	
	private FormaPagamento(int valor) {
		this.valor = valor;
	}
	
	private int getValor() {
		return valor;
	}
	
	public static FormaPagamento valueOf(int valor) {
		for (FormaPagamento value : FormaPagamento.values()) {
			if(value.getValor() == valor) return value;
		}
		
		throw new SacolaApiException("Forma de Pagamento Inválida!");
	}
	
}







