package ifrn.pi.eventos.models;

import java.util.List; // IMPORTANTE: Adicione esta importação
import jakarta.persistence.CascadeType; // IMPORTANTE: Adicione esta importação
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany; // IMPORTANTE: Adicione esta importação

@Entity
public class Evento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String local;
	private String data;
	private String horario;
	
	// MAPEAMENTO ADICIONADO: Diz que um evento tem muitos convidados e remove em cascata
	@OneToMany(mappedBy = "evento", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<Convidado> convidados;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getLocal() {
		return local;
	}
	public void setLocal(String local) {
		this.local = local;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getHorario() {
		return horario;
	}
	public void setHorario(String horario) {
		this.horario = horario;
	}
	
	// GETTER E SETTER DA LISTA (Opcional, mas bom ter)
	public List<Convidado> getConvidados() {
		return convidados;
	}
	public void setConvidados(List<Convidado> convidados) {
		this.convidados = convidados;
	}

	@Override
	public String toString() {
		return "Evento [id=" + id + ", nome=" + nome + ", local=" + local + ", data=" + data + ", horario=" + horario
				+ "]";
	}
}
