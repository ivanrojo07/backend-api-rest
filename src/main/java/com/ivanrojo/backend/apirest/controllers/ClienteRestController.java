package com.ivanrojo.backend.apirest.controllers;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	public ResponseEntity<?> show(@PathVariable(value = "id") Long id
			) {
		Cliente cliente = null;
		Map<String, Object> response = new HashMap<>();
		try {
			cliente = clienteService.findById(id);
		}catch(DataAccessException e) {
			response.put("mensaje","Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(cliente == null) {
			response.put("mensaje", "El cliente ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
//		if (id >= 0) {
//			return clienteService.findById(id); 
//		}
//		else {
//			return null;
//		}
	}
	@PostMapping("/create")
//	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> store(@RequestBody Cliente cliente) {
		Cliente cliente_new = null;
		Map<String, Object> response = new HashMap<>();
		try {
			Date hoy = new Date();
			Timestamp ts = new Timestamp(hoy.getTime());
			cliente.setCreatedAt(ts);
			cliente.setUpdatedAt(ts);
			cliente_new = clienteService.save(cliente);
		}catch(DataAccessException e) {
			response.put("mensaje","Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El cliente ha sido creado con éxito");
		response.put("cliente", cliente_new);
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
		
		
		
	}
	
	@PutMapping("/update/{id}")
	
	public ResponseEntity<?> update(@PathVariable(value = "id") Long id, @RequestBody Cliente cliente) {
		Cliente bd_cliente = clienteService.findById(id);
		
		Cliente cliente_updated = null;
		
		Map<String, Object> response = new HashMap<>();
		
		if (bd_cliente == null) {
			response.put("mensaje", "El cliente ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
		}
		try {
			
			bd_cliente.setNombre(cliente.getNombre());
			bd_cliente.setApellido(cliente.getApellido());
			bd_cliente.setEmail(cliente.getEmail());
			Date hoy = new Date();
			Timestamp ts = new Timestamp(hoy.getTime());
			bd_cliente.setUpdatedAt(ts);
			cliente_updated = clienteService.save(bd_cliente);
		}catch(DataAccessException e) {
			response.put("mensaje","Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El cliente ha sido actualizado con éxito");
		response.put("cliente", cliente_updated);
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> destroy(@PathVariable(value = "id") Long id) {
		Cliente cliente = clienteService.findById(id);
		Map<String, Object> response = new HashMap<>();
		if(cliente == null) {
			response.put("mensaje", "El cliente ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			clienteService.delete(id);
		}
		catch(DataAccessException e) {
			response.put("mensaje","Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El cliente ha sido eliminado con éxito");
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
		
	}
	
}
