package com.algaworks.cobranca.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.cobranca.enums.StatusTitulo;
import com.algaworks.cobranca.model.Titulo;
import com.algaworks.cobranca.repository.filter.TituloFilter;
import com.algaworks.cobranca.service.CadastroTituloService;

@Controller
@RequestMapping("/titulos")
public class TituloController {
	
	private static final String CADASTRO_TITULO = "CadastroTitulo";
	
	@Autowired
	private CadastroTituloService cadastroTituloService;

	@RequestMapping("/novo")
	public ModelAndView novo() {
		
		ModelAndView modelAndView =  new ModelAndView(CADASTRO_TITULO);
		modelAndView.addObject( new Titulo());
		return modelAndView;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Titulo titulo, Errors erros, RedirectAttributes attributes){
		
		if(erros.hasErrors())
			return CADASTRO_TITULO;
		
		try {
		
			cadastroTituloService.salvar(titulo);
			attributes.addFlashAttribute("mensagem", "Título salvo com sucesso!");
			return "redirect:/titulos/novo";
		}
		
		catch(IllegalArgumentException exception){
			
			erros.rejectValue("dataVencimento", null, "Formato de data inválido");
			return CADASTRO_TITULO;
		}
	}
	
	@RequestMapping
	public ModelAndView pesquisar(@ModelAttribute("filtro") TituloFilter filtro){
		
		List<Titulo> titulos = this.cadastroTituloService.filtrar(filtro);
		ModelAndView modelAndView =  new ModelAndView("PesquisaTitulos");
		modelAndView.addObject("titulos", titulos);
		return modelAndView;
	}
	
	
	@RequestMapping("{codigo}")
	public ModelAndView edicao(@PathVariable("codigo") Titulo titulo){
	
		ModelAndView modelAndView =  new ModelAndView(CADASTRO_TITULO);
		modelAndView.addObject(titulo);
		return modelAndView;
	}
	
	@RequestMapping(value = "{codigo}", method = RequestMethod.DELETE)
	public String excluir(@PathVariable Long codigo, RedirectAttributes attributes){
		
		this.cadastroTituloService.excluir(codigo);
		attributes.addFlashAttribute("mensagem", "Título excluido com sucesso!");
		return "redirect:/titulos";
	}
	
	@RequestMapping(value = "/{codigo}/receber", method = RequestMethod.PUT)
	public @ResponseBody String receber(@PathVariable Long codigo){
		
		return this.cadastroTituloService.receber(codigo);
	}
	
	@ModelAttribute("statusTitulo")
	public List<StatusTitulo> statusTitulo(){
		
		return Arrays.asList(StatusTitulo.values());
	}
}
