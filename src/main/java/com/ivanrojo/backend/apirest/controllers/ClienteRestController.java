package com.ivanrojo.backend.apirest.controllers;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.ivanrojo.backend.apirest.models.entity.Cliente;
import com.ivanrojo.backend.apirest.models.services.IClienteService;

@RestController
@RequestMapping(path = "/api/clientes")
@CrossOrigin(origins = "http://localhost:3000")
public class ClienteRestController {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private IClienteService clienteService;
	
	@GetMapping("/")
	@ResponseStatus(HttpStatus.OK)
	public List<Cliente> index(){
		return clienteService.findAll();
	}
	
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Cliente show(@PathVariable(value = "id") Long id
			) {
		if (id >= 0) {
			return clienteService.findById(id); 
		}
		else {
			return null;
		}
	}
	@PostMapping("/create")
	@ResponseStatus(HttpStatus.CREATED)
	public Cliente store(@RequestBody Cliente cliente) {
		Date hoy = new Date();
		Timestamp ts = new Timestamp(hoy.getTime());
		cliente.setCreatedAt(ts);
		cliente.setUpdatedAt(ts);
		return clienteService.save(cliente);
	}
	
	@PutMapping("/update/{id}")
	
	public Cliente update(@PathVariable(value = "id") Long id, @RequestBody Cliente cliente) {
		log.info(id.toString());
		Cliente bd_cliente = clienteService.findById(id);
		if (bd_cliente != null) {
			bd_cliente.setNombre(cliente.getNombre());
			bd_cliente.setApellido(cliente.getApellido());
			bd_cliente.setEmail(cliente.getEmail());
			Date hoy = new Date();
			Timestamp ts = new Timestamp(hoy.getTime());
			bd_cliente.setUpdatedAt(ts);
			return clienteService.save(bd_cliente);
		}
		else {
			return null;
		}
	}
	
	@DeleteMapping("/delete/{id}")
	public void destroy(@PathVariable(value = "id") Long id) {
		Cliente cliente = clienteService.findById(id);
		if(cliente != null) {
			clienteService.delete(id);
		}
	}
	
}
