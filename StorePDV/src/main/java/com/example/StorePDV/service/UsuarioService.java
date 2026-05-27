package com.example.StorePDV.service;

import com.example.StorePDV.exception.CampoObrigatorioException;
import com.example.StorePDV.exception.EntidadeNaoEncontradaException;
import com.example.StorePDV.exception.LoginInvalidoException;
import com.example.StorePDV.model.Usuario;
import com.example.StorePDV.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario login(String email, String senha) {
        if (email == null || email.isBlank())
            throw new CampoObrigatorioException("email");
        if (senha == null || senha.isBlank())
            throw new CampoObrigatorioException("senha");

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(LoginInvalidoException::new);

        if (!usuario.getSenha().equals(senha))
            throw new LoginInvalidoException();

        return usuario;
    }

    public void recuperarSenha(String email) {
        if (email == null || email.isBlank())
            throw new CampoObrigatorioException("email");

        usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário", "email: " + email));

        // Aqui entraria a lógica de envio de e-mail real
        System.out.println("[SISTEMA] Link de recuperação enviado para: " + email);
    }

    public Usuario cadastrar(Usuario usuario) {
        validar(usuario);
        return usuarioRepository.save(usuario);
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário", id));
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    private void validar(Usuario u) {
        if (u == null)
            throw new CampoObrigatorioException("usuário");
        if (u.getNome() == null || u.getNome().isBlank())
            throw new CampoObrigatorioException("nome");
        if (u.getEmail() == null || u.getEmail().isBlank())
            throw new CampoObrigatorioException("email");
        if (u.getSenha() == null || u.getSenha().isBlank())
            throw new CampoObrigatorioException("senha");
    }
}