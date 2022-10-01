package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Cliente;
import com.bolsadeideas.springboot.backend.apirest.models.repository.IClienteDao;

@Service
public class ClienteServiceImp implements IClienteService{
	
	@Autowired
	private IClienteDao repositoryClient;

	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAllClients() {
		return (List<Cliente>) repositoryClient.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente findClientById(Long id) {
		return repositoryClient.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Cliente saveClient(Cliente cliente) {
		return repositoryClient.save(cliente);
	}

	@Override
	@Transactional
	public void deleteClient(Long id) {
		repositoryClient.deleteById(id);
	}
		

}
