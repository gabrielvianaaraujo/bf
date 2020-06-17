package br.com.softblue.bluefood.infrastructure.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.softblue.bluefood.application.services.ClienteService;
import br.com.softblue.bluefood.application.services.ValidationException;
import br.com.softblue.bluefood.domain.cliente.Cliente;
import br.com.softblue.bluefood.domain.cliente.ClienteRepository;
import br.com.softblue.bluefood.utils.SecurityUtils;

@Controller
@RequestMapping(path = "/cliente")
public class ClienteController {

	@Autowired
	private ClienteService cs;
	
	@Autowired
	private ClienteRepository cr;

	@GetMapping(path = "/home")
	public String home() {
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
}
