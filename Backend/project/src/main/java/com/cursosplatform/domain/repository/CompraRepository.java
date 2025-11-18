package com.cursosplatform.domain.repository;

import com.cursosplatform.domain.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Integer> {

    List<Compra> findByUsuarioIdUsuario(Integer idUsuario);

    List<Compra> findByEstadoCompra(Compra.EstadoCompra estadoCompra);
}
