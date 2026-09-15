package ifrn.pi.eventos.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ifrn.pi.eventos.models.Convidado;
import ifrn.pi.eventos.models.Evento;

public interface ConvidadoRepository extends JpaRepository<Convidado, Long> {
    
    // Corrija de finByEvento para findByEvento
    List<Convidado> findByEvento(Evento evento);
    
}

