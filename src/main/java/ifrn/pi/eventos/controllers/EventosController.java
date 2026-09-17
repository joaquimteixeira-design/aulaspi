package ifrn.pi.eventos.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ifrn.pi.eventos.models.Convidado;
import ifrn.pi.eventos.models.Evento;
import ifrn.pi.eventos.repositories.ConvidadoRepository;
import ifrn.pi.eventos.repositories.EventoRepository;

@Controller
@RequestMapping("/eventos")
public class EventosController {

	@Autowired
	private EventoRepository er;
	@Autowired
	private ConvidadoRepository cr; 

	@GetMapping("/form")
	public String form(Evento evento) {
		return "eventos/formEvento";
	}

	@PostMapping
	public String salvar(Evento evento) {
		System.out.println(evento);
		
		// Correção: Se o evento já possui um ID, estamos editando um registro existente
		if (evento.getId() != null) {
			Optional<Evento> eventoBanco = er.findById(evento.getId());
			if (eventoBanco.isPresent()) {
				// Busca a lista anterior de convidados do banco e reatribui ao objeto editado
				evento.setConvidados(eventoBanco.get().getConvidados());
			}
		}
		
		er.save(evento);
		return "redirect:/eventos";
	}

	@GetMapping
	public ModelAndView listar() {
		List<Evento> eventos = er.findAll();
		ModelAndView mv = new ModelAndView("eventos/lista");
		mv.addObject("eventos", eventos);
		return mv;
	}

	@GetMapping("/{id}")
	public ModelAndView detalhar(@PathVariable Long id) {
		ModelAndView md = new ModelAndView();
		Optional<Evento> opt = er.findById(id);
		if (opt.isEmpty()) {
			md.setViewName("redirect:/eventos");
			return md;
		}
		md.setViewName("eventos/detalhes");
		Evento evento = opt.get();
		md.addObject("evento", evento);

		List<Convidado> convidado = cr.findByEvento(evento);
		md.addObject("convidado", convidado);
		
		// Envia um objeto vazio para o formulário de adicionar convidado não quebrar
		md.addObject("convidadoSelecionado", new Convidado());
		
		return md;
	}

	@PostMapping("/{idEvento}")
	public String savarConvidado(@PathVariable Long idEvento, Convidado convidado) {
		System.out.println("Id do evento: " + idEvento);
		System.out.println(convidado);
		
		Optional<Evento> opt = er.findById(idEvento);
		if(opt.isEmpty()) {
			return "redirect:/eventos";	
		}
		Evento evento = opt.get();
		convidado.setEvento(evento);
		
		cr.save(convidado);
		
		return "redirect:/eventos/{idEvento}";
	}

	@GetMapping("/{id}/selecionar")
	public ModelAndView selecionarEvento(@PathVariable Long id) {
		ModelAndView md = new ModelAndView();
		Optional<Evento> opt = er.findById(id);
		if(opt.isEmpty()) {
			md.setViewName("redirect:/eventos");
			return md;
		}
		
		Evento evento = opt.get();
		md.setViewName("eventos/formEvento");
		md.addObject("evento", evento);
		
		return md;
	}

	@GetMapping("/{id}/remover")
	public String apagarEvento(@PathVariable Long id) {
		Optional<Evento> opt = er.findById(id);
		
		if(!opt.isEmpty()) {		
			Evento evento = opt.get();
			er.delete(evento);
		}
		
		return "redirect:/eventos"; 
	}
	
	// --- MÉTODOS PARA OS CONVIDADOS ---

	@GetMapping("/{idEvento}/convidado/{idConvidado}/selecionar")
	public ModelAndView selecionarConvidado(@PathVariable Long idEvento, @PathVariable Long idConvidado) {
		ModelAndView md = new ModelAndView();
		
		Optional<Evento> optEvento = er.findById(idEvento);
		Optional<Convidado> optConvidado = cr.findById(idConvidado);
		
		// Se o evento ou o convidado não existirem, volta para a listagem
		if(optEvento.isEmpty() || optConvidado.isEmpty()) {
			md.setViewName("redirect:/eventos");
			return md;
		}
		
		md.setViewName("eventos/detalhes");
		
		Evento evento = optEvento.get();
		md.addObject("evento", evento);
		
		List<Convidado> convidado = cr.findByEvento(evento);
		md.addObject("convidado", convidado);
		
		// Coloca o convidado localizado dentro do formulário para edição
		md.addObject("convidadoSelecionado", optConvidado.get());
		
		return md;
	}

	@GetMapping("/{idEvento}/convidado/{idConvidado}/remover")
	public String apagarConvidado(@PathVariable Long idEvento, @PathVariable Long idConvidado) {
		Optional<Convidado> opt = cr.findById(idConvidado);
		
		if(!opt.isEmpty()) {
			Convidado convidado = opt.get();
			cr.delete(convidado);
		}
		
		return "redirect:/eventos/" + idEvento;
	}
}

