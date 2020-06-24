package br.com.softblue.bluefood.domain.restaurante;

import org.springframework.data.jpa.repository.JpaRepository;

//JpaRepository<T, IDr> - Recebe por generics o tipo que será trabalhado, no caso, Cliente, e o tipo do ID que o identifica, Integer
public interface ItemCardapioRepository extends JpaRepository<ItemCardapio, Integer> {
	
}
