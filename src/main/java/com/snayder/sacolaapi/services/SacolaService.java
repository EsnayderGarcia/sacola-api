package com.snayder.sacolaapi.services;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snayder.sacolaapi.dtos.ItemDTO;
import com.snayder.sacolaapi.dtos.ItemRequestDTO;
import com.snayder.sacolaapi.dtos.SacolaDTO;
import com.snayder.sacolaapi.enums.FormaPagamento;
import com.snayder.sacolaapi.models.Item;
import com.snayder.sacolaapi.repositories.ItemRepository;
import com.snayder.sacolaapi.repositories.ProdutoRepository;
import com.snayder.sacolaapi.repositories.SacolaRepository;
import com.snayder.sacolaapi.services.exceptions.ResourceNotFoundException;
import com.snayder.sacolaapi.services.exceptions.SacolaApiException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SacolaService {

	private SacolaRepository sacolaRepository;
	private ProdutoRepository produtoRepository;
	private ItemRepository itemRepository;
	private ModelMapper mapper;

	@Transactional(readOnly = true)
	public SacolaDTO verSacola(Long sacolaId) {
		var sacola = sacolaRepository.findById(sacolaId)
				.orElseThrow(() -> new ResourceNotFoundException("Sacola não encontrada"));

		var sacolaDTO = mapper.map(sacola, SacolaDTO.class);
		carregarItensSacola(sacola.getItensSacola(), sacolaDTO);

		return sacolaDTO;
	}

	@Transactional
	public ItemDTO adicionarItemSacola(ItemRequestDTO itemRequestDTO) {
		try {
			var sacola = sacolaRepository.getReferenceById(itemRequestDTO.getSacolaId());

			if (sacola.getFechada())
				throw new SacolaApiException("Sacola fechada. Não é possível adicionar itens.");

			var itensSacola = sacola.getItensSacola();

			var item = Item.builder()
					.produto(produtoRepository
								.getReferenceById(itemRequestDTO.getProdutoId()))
					.sacola(sacola)
					.quantidade(itemRequestDTO.getQuantidade())
					.build();
 
			if (itensSacola.isEmpty())
					itensSacola.add(item);
			else
				verificarSeRestauranteEOMesmo(item, itensSacola);

			sacola.incrementarValorTotal(item);
			sacolaRepository.save(sacola);

			return mapper.map(item, ItemDTO.class);
		} 
		catch (EntityNotFoundException ex) {
			throw new ResourceNotFoundException("A sacola ou o produto informado não existe. Verifique os dados informados e tente novamente.");
		}
	}

	@Transactional
	public SacolaDTO fecharSacola(Long sacolaId, int formaPagamento) {
		try {
			var sacola = sacolaRepository.getReferenceById(sacolaId);

			if (sacola.getItensSacola().isEmpty())
				throw new SacolaApiException("Adicione itens a sacola para fechá-la.");

			FormaPagamento pagamento = 
					formaPagamento == 0 ? FormaPagamento.DINHEIRO : FormaPagamento.MAQUINA;
 
			sacola.setFormaPagamento(pagamento);
			sacola.setFechada(true);
			sacola = sacolaRepository.save(sacola);
 
			var sacolaDTO = mapper.map(sacola, SacolaDTO.class);
			carregarItensSacola(sacola.getItensSacola(), sacolaDTO);

			return sacolaDTO;
		} 
		catch (EntityNotFoundException ex) {
			throw new ResourceNotFoundException("A sacola informada não existe.");
		}
	}

	@Transactional
	public void removerItemSacola(Long sacolaId, Long itemId) {
		try {
			var sacola = sacolaRepository.getReferenceById(sacolaId);
	
			var itensSacola = sacola.getItensSacola();
	
			if (sacola.getFechada())
				throw new SacolaApiException("Sacola fechada. Não é possível remover itens.");
		 	
			else if (itensSacola.isEmpty())
				throw new SacolaApiException("Não é possível remover o item, pois a sacola está vazia.");
	
			var itemParaRemover = itensSacola.stream()
					.filter(i -> i.getId() == itemId)
					.findFirst()
					.orElseThrow(() -> new ResourceNotFoundException("Item não encontrado"));
	
			itensSacola.remove(itemParaRemover);
			itemRepository.delete(itemParaRemover);
	
			sacola.decrementarValorTotal(itemParaRemover);
			sacolaRepository.save(sacola);
		}
		catch (EntityNotFoundException ex) {
			throw new ResourceNotFoundException("A sacola informada não existe.");
		}
	}

	private void verificarSeRestauranteEOMesmo(Item item, List<Item> itensSacola) {
		var idDoRestauranteDoItemNovo = item.getProduto().getRestaurante().getId();
		var idDoRestauranteDoItemJaInserido = itensSacola.get(0).getProduto().getRestaurante().getId();

		if (idDoRestauranteDoItemNovo != idDoRestauranteDoItemJaInserido)
			throw new SacolaApiException("Não é possível adicionar produtos de restaurantes diferentes. Feche a sacola ou esvazie.");

		itensSacola.add(item);
	}

	private void carregarItensSacola(List<Item> itens, SacolaDTO dto) {
		itens.forEach(item -> dto.getItensSacola().add(mapper.map(item, ItemDTO.class)));
	}

}
