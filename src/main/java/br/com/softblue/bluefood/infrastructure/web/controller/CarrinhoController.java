package br.com.softblue.bluefood.infrastructure.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import br.com.softblue.bluefood.application.services.RestauranteDiferenteException;
import br.com.softblue.bluefood.domain.pedido.Carrinho;
import br.com.softblue.bluefood.domain.restaurante.ItemCardapio;
import br.com.softblue.bluefood.domain.restaurante.ItemCardapioRepository;

@Controller
@RequestMapping("/cliente/carrinho")
@SessionAttributes("carrinho")
public class CarrinhoController {

    @Autowired
    private ItemCardapioRepository icr;

    @ModelAttribute("carrinho")
    public Carrinho carrinho() {
        return new Carrinho();
    }

    @GetMapping(path = "/visualizar")
    public String viewCarrinho(){
        return "cliente-carrinho";
    }

    // Chamado quando o botão adicionar item ao carrinho é chamado
    @GetMapping(path = "/adicionar")
    public String adicionarItem(@RequestParam("itemId") Integer itemId, @RequestParam("qtd") Integer quantidade,
            @RequestParam("observacoes") String observacoes, @ModelAttribute("carrinho") Carrinho carrinho,
            Model model) {

        ItemCardapio itemCardapio = icr.findById(itemId).orElseThrow();

        try {
            carrinho.adicionarItem(itemCardapio, quantidade, observacoes);
        } catch (RestauranteDiferenteException e) {
            model.addAttribute("msg", "Não é possivel adicionar itens de restaurantes diferentes");
        }

        return "cliente-carrinho";
    }

    
    @GetMapping(path = "/remover")
    public String removerItem(
        @RequestParam("itemId") Integer itemId,
        @ModelAttribute("carrinho") Carrinho carrinho,
        SessionStatus sessionStatus,
        Model model) {

        ItemCardapio itemCardapio = icr.findById(itemId).orElseThrow();
        carrinho.removerItem(itemCardapio);
        
        if(carrinho.isEmpty()){
            sessionStatus.setComplete();
        }

        return "cliente-carrinho";
    }
    
}