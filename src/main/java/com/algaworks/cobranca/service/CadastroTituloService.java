package com.algaworks.cobranca.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.algaworks.cobranca.enums.StatusTitulo;
import com.algaworks.cobranca.model.Titulo;
import com.algaworks.cobranca.repository.Titulos;
import com.algaworks.cobranca.repository.filter.TituloFilter;

@Service
public class CadastroTituloService {

	@Autowired
	private Titulos titulos;
	
	public void salvar(Titulo titulo){
		
		try {
			
			this.titulos.save(titulo);
		}
		
		catch(DataIntegrityViolationException exception){
			
			throw new IllegalArgumentException("Formato de data inválido");
		}
	}
	
	public void excluir(Long codigo){
		
		this.titulos.delete(codigo);
	}
	
	public String receber(Long codigo){
		
		Titulo titulo = this.titulos.findOne(codigo);
		titulo.setStatus(StatusTitulo.RECEBIDO);
		this.titulos.save(titulo);
		
		return titulo.getStatus().getDescricao();
	}
	
	public List<Titulo> filtrar(TituloFilter filtro){
		
		String descricao = filtro.getDescricao() == null ? "%" : filtro.getDescricao();
		return this.titulos.findByDescricaoContaining(descricao);
	}
}
