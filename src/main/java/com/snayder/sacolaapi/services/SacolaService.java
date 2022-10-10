package com.snayder.sacolaapi.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snayder.sacolaapi.dtos.ItemDTO;
import com.snayder.sacolaapi.dtos.ItemRequestDTO;
import com.snayder.sacolaapi.dtos.SacolaDTO;
import com.snayder.sacolaapi.enums.FormaPagamento;
import com.snayder.sacolaapi.models.Item;
import com.snayder.sacolaapi.models.Sacola;
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
		var sacola = getSacola(sacolaId);
		
		var sacolaDTO = mapper.map(sacola, SacolaDTO.class);
		carregarItensSacola(sacola.getItensSacola(), sacolaDTO);

		return sacolaDTO;
	}

	@Transactional
	public ItemDTO adicionarItemSacola(ItemRequestDTO itemRequestDTO) {
		var sacola = getSacola(itemRequestDTO.getSacolaId());

		if (sacola.getFechada())
			throw new SacolaApiException("Sacola fechada. " 
						+ "Não é possível adicionar itens.");

		var item = Item.builder()
				.produto(produtoRepository.findById(itemRequestDTO.getProdutoId())
						.orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado!")))
				.sacola(sacola)
				.quantidade(itemRequestDTO.getQuantidade()).build();

		if (sacola.getItensSacola().isEmpty())
			sacola.getItensSacola().add(item);
		else
			verificarRestaurante(item, sacola.getItensSacola());

		sacola.incrementarValorTotal(item);
		sacolaRepository.save(sacola);
		
		return mapper.map(item,ItemDTO.class);
	}

	@Transactional
	public SacolaDTO fecharSacola(Long sacolaId, int formaPagamento) {
		var sacola = getSacola(sacolaId);

		if (sacola.getItensSacola().isEmpty())
			throw new SacolaApiException("Adicione itens a sacola para fechá-la.");

		var pagamento = FormaPagamento.valueOf(formaPagamento);

		sacola.setFormaPagamento(pagamento);
		sacola.setFechada(true);
		sacola = sacolaRepository.save(sacola);

		var sacolaDTO = mapper.map(sacola, SacolaDTO.class);
		carregarItensSacola(sacola.getItensSacola(), sacolaDTO);

		return sacolaDTO;
	}

	@Transactional
	public void removerItemSacola(Long sacolaId, Long itemId) {
		var sacola = getSacola(sacolaId);

		if (sacola.getFechada())
			throw new SacolaApiException("Sacola fechada. Não é possível remover itens.");
		else if (sacola.getItensSacola().isEmpty())
			throw new SacolaApiException("Não é possível remover o item, pois a sacola está vazia.");

		var itemParaRemover = sacola.getItensSacola()
				.stream()
				.filter(i -> i.getId() == itemId)
				.findFirst()
				.orElseThrow(() -> new ResourceNotFoundException("Item não encontrado"));

		sacola.getItensSacola().remove(itemParaRemover);
		itemRepository.delete(itemParaRemover);

		sacola.decrementarValorTotal(itemParaRemover);
		sacolaRepository.save(sacola);
	}

	private void verificarRestaurante(Item item, List<Item> itensSacola) {
		var idDoRestauranteDoItemNovo = item.getProduto().getRestaurante().getId();
		var idDoRestauranteDoItemJaInserido = itensSacola.get(0).getProduto().getRestaurante().getId();

		if (!idDoRestauranteDoItemNovo.equals(idDoRestauranteDoItemJaInserido))
			throw new SacolaApiException(
					"Não é possível adicionar produtos de restaurantes diferentes. "
					+ "Feche a sacola ou esvazie.");

		itensSacola.add(item);
	}
	
	private Sacola getSacola(Long sacolaId) {
		return sacolaRepository.findById(sacolaId)
				.orElseThrow(() -> new ResourceNotFoundException("Sacola não encontrada!"));
	}

	private void carregarItensSacola(List<Item> itens, SacolaDTO dto) {
		itens.forEach(item -> dto.getItensSacola().add(mapper.map(item, ItemDTO.class)));
	}

}
