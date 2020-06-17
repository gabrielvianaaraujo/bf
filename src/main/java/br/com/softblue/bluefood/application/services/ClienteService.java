package br.com.softblue.bluefood.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.softblue.bluefood.domain.cliente.Cliente;
import br.com.softblue.bluefood.domain.cliente.ClienteRepository;
import br.com.softblue.bluefood.domain.restaurante.Restaurante;
import br.com.softblue.bluefood.domain.restaurante.RestauranteRepository;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository cr;

	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Transactional
	public void saveCLiente(Cliente cliente) throws ValidationException{

		if(!validateEmail(cliente.getEmail(), cliente.getId())) {
			throw new ValidationException("O E-mail está duplicado");
		}
		if(cliente.getId()!= null) {
			Cliente clienteDB = cr.findById(cliente.getId()).orElseThrow();
			cliente.setSenha(clienteDB.getSenha());
		}
		else {
			cliente.encryptPassword();
		}
		cr.save(cliente);
	}
	
	private boolean validateEmail(String email, Integer id) {

		Restaurante restaurante = restauranteRepository.findByEmail(email);

		if(restaurante != null) return false;

		Cliente cliente = cr.findByEmail(email);
		if(cliente != null) {
			if(id == null) {
				return false;
			}
			if(!cliente.getId().equals(id)) {
				return false;
			}
		}
		
		return true;
	}
}
