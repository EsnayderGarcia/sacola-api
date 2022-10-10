package com.snayder.sacolaapi.resources;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snayder.sacolaapi.dtos.ItemDTO;
import com.snayder.sacolaapi.dtos.ItemRequestDTO;
import com.snayder.sacolaapi.dtos.SacolaDTO;
import com.snayder.sacolaapi.services.SacolaService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("sacolas")
@AllArgsConstructor
public class SacolaResource {
	
	private SacolaService service;
	
	@GetMapping("/{sacolaId}")
	@ApiOperation("Ver sacola")
	public ResponseEntity<SacolaDTO> verSacola(
			@PathVariable @ApiParam(value = "Id da Sacola", example = "1") Long sacolaId) {
		
		var sacola = service.verSacola(sacolaId);
		return ResponseEntity.ok(sacola);
	}
	 
	@PostMapping
	@ApiOperation("Adicionar item a uma sacola")
	public ResponseEntity<ItemDTO> adicionarItemSacola(@RequestBody ItemRequestDTO item) {
		var itemSalvo  = service.adicionarItemSacola(item);
		return new ResponseEntity<ItemDTO>(itemSalvo, HttpStatus.CREATED);
	}
	 
	@PatchMapping("/{sacolaId}")
	@ApiOperation(value = "Fechar sacola", notes = "Informe 0 para pagamento em dinheiro, 1 para pagamento via máquina ou 2 para pagamento via pix.")
	public ResponseEntity<SacolaDTO> fecharSacola(
			@PathVariable @ApiParam(value = "Id da Sacola", example = "1") Long sacolaId, 
			@RequestParam  @ApiParam(value = "Forma de Pagamento", example = "1") int formaPagamento) {
		
		var sacola = service.fecharSacola(sacolaId, formaPagamento);
		return ResponseEntity.ok(sacola);
	}
	
	@DeleteMapping("/{sacolaId}/{itemId}")
	@ApiOperation("Remover item da sacola")
	public ResponseEntity<Void> removerItemSacola(
			@PathVariable @ApiParam(value = "Id da Sacola", example = "1") Long sacolaId,
			@PathVariable @ApiParam(value = "Id do Item", example = "1") Long itemId) {
		
		service.removerItemSacola(sacolaId, itemId);
		return ResponseEntity.noContent().build();
	}

}

 















