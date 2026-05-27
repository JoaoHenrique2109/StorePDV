package com.example.StorePDV.repository;

import com.example.StorePDV.model.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessaoRepository extends JpaRepository<Sessao, Long> {
    List<Sessao> findByUsuario_Id(Long usuarioId);
    Optional<Sessao> findByUsuario_IdAndStatusTrue(Long usuarioId);
}