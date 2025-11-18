package com.cursosplatform.domain.repository;

import com.cursosplatform.domain.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {

    List<Pago> findByCompraIdCompra(Integer idCompra);

    List<Pago> findByEstadoPago(Pago.EstadoPago estadoPago);
}
