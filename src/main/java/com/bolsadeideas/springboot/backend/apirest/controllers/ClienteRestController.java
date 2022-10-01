package com.bolsadeideas.springboot.backend.apirest.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Cliente;
import com.bolsadeideas.springboot.backend.apirest.models.services.IClienteService;

@CrossOrigin(origins = {"http://localhost:4200"})	//Para permitir a otros servidores acceder a mi dominio V-740 min 53
@RestController
@RequestMapping("/api")
public class ClienteRestController {
	
	@Autowired
	private IClienteService serviceClient;
	
	@GetMapping("/clientes")
	public List<Cliente> index() {
		return serviceClient.findAllClients();
	}
	
	//Cambio el retorno "Cliente" a "ResponseEntity<?>" para manejar errores -- IMPORTANTE
	@GetMapping("/clientes/{id}")
	public ResponseEntity<?> show(@PathVariable(name = "id") Long id) {
		Cliente client = null;
		Map<String, String> response = new HashMap<>();
		
		try {	//Por si ocurre un error en la base de datos
			client = serviceClient.findClientById(id);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error interno de la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, String>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if( client == null ) {
			response.put("mensaje", "El cliente con id " + id.toString() + " no existe en la base de datos" );
			return new ResponseEntity<Map<String, String>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Cliente>(client, HttpStatus.OK);
	}
	
	//@ResponseStatus(HttpStatus.CREATED) Ya no es necesaria porque cambie lo que retorna el método
	@PostMapping("/clientes")
	public ResponseEntity<?> create(@RequestBody Cliente client) {
		Cliente newClient = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			newClient = serviceClient.saveClient(client);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error interno de la base de datos al insertar.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "Cliente creado con exito");
		response.put("cliente", newClient);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	//@ResponseStatus(HttpStatus.CREATED)
	@PutMapping("/clientes/{id}")
	public ResponseEntity<?> update(@RequestBody Cliente client, @PathVariable Long id) {
		Cliente cliFind = serviceClient.findClientById(id);
		Map<String, Object> response = new HashMap<>();
		
		if( cliFind == null ) {
			response.put("mensaje", "El cliente con id " + id.toString() + " no existe en la base de datos" );
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		Cliente cliUpdate = null;
		
		try {
			cliFind.setName(client.getName());
			cliFind.setLastName(client.getLastName());
			cliFind.setEmail(client.getEmail());
			cliUpdate = serviceClient.saveClient(cliFind);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error interno de la base de datos al actualizar.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "Cliente actualizado con exito");
		response.put("cliente", cliUpdate);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	//@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/clientes/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			serviceClient.deleteClient(id);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error interno de la base de datos al eliminar.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "Cliente eliminado con exito");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
}
