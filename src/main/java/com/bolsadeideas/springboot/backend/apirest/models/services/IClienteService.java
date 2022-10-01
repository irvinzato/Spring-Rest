package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Cliente;

public interface IClienteService {
	
	public List<Cliente> findAllClients();
	
	public Cliente findClientById(Long id);
	
	public Cliente saveClient(Cliente cliente);
	
	public void deleteClient(Long id);

}
