package br.com.softblue.bluefood.infrastructure.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.softblue.bluefood.application.services.ClienteService;
import br.com.softblue.bluefood.application.services.RestauranteService;
import br.com.softblue.bluefood.application.services.ValidationException;
import br.com.softblue.bluefood.domain.cliente.Cliente;
import br.com.softblue.bluefood.domain.cliente.ClienteRepository;
import br.com.softblue.bluefood.domain.restaurante.CategoriaRestaurante;
import br.com.softblue.bluefood.domain.restaurante.CategoriaRestauranteRepository;
import br.com.softblue.bluefood.domain.restaurante.SearchFilter;
import br.com.softblue.bluefood.utils.SecurityUtils;

@Controller
@RequestMapping(path = "/cliente")
public class ClienteController {

	@Autowired
	private ClienteService cs;
	
	@Autowired
	private CategoriaRestauranteRepository crr;

	@Autowired
	private ClienteRepository cr;

	@Autowired
	private RestauranteService rs;

	
	@GetMapping(path = "/home")
	public String home(Model model) {

		List<CategoriaRestaurante> categorias = crr.findAll(Sort.by("nome"));
		model.addAttribute("categorias", categorias);
		model.addAttribute("searchFilter", new SearchFilter());
		return "cliente-home";
	}
	
	@GetMapping(path = "/edit")
	public String edit(Model model){
		//Integer clienteId = SecurityUtils.loggedCliente().getId();
		//Cliente cliente = cr.findById(SecurityUtils.loggedCliente().getId()).orElseThrow();
		model.addAttribute("cliente", cr.findById(SecurityUtils.loggedCliente().getId()).orElseThrow());
		HelperController.setEditMode(model, true);
		return "cliente-cadastro";
	}

	@PostMapping("/save")
	public String save(@ModelAttribute("cliente") @Valid Cliente cliente, Errors errors, Model model) {
		if(!errors.hasErrors()) {
			try {
				cs.saveCLiente(cliente);
				model.addAttribute("msg", "Cliente cadastrado com sucesso");
			} catch (ValidationException e) {
				errors.rejectValue("email", null, e.getMessage());
			}
			
		}
		HelperController.setEditMode(model, true);
		return "cliente-cadastro";
	}

	@GetMapping(path = "/search")
	public String search(@ModelAttribute ("searchFilter") SearchFilter filter,@RequestParam(value = "cmd", required = false) String command, Model model){

		filter.processFilter(command);

		//Pode ser feito assim:

		/*List<Restaurante> restaurantes = rs.search(filter);
		model.addAttribute("restaurantes", restaurantes);*/

		//Ou assim:
		model.addAttribute("restaurantes", rs.search(filter));

		//Adiciona novamente ao model para que esteja disponível na página cliente-busca
		model.addAttribute("searchFilter", filter);

		HelperController.addCategoriasToRequest(crr, model);
		return "cliente-busca";
	}
}
