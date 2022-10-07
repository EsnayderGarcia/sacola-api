package com.snayder.sacolaapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDTO {
	
	private Long produtoId;
	private Long sacolaId;
	private int quantidade;

}
