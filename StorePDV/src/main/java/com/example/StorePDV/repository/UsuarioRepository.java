// ── UsuarioRepository.java ───────────────────────────────────────────
package com.example.StorePDV.repository;

import com.example.StorePDV.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByCargo(String cargo);
}
