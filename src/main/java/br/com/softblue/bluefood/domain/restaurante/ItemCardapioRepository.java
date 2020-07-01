package br.com.softblue.bluefood.domain.restaurante;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

//JpaRepository<T, IDr> - Recebe por generics o tipo que será trabalhado, no caso, Cliente, e o tipo do ID que o identifica, Integer
public interface ItemCardapioRepository extends JpaRepository<ItemCardapio, Integer> {
    
    //Procura categorias excluindo as duplicidades
    @Query("SELECT DISTINCT ic.categoria FROM ItemCardapio ic WHERE ic.restaurante.id = ?1 ORDER BY ic.categoria")
    public List<String> findCategorias(Integer restauranteId);

}
