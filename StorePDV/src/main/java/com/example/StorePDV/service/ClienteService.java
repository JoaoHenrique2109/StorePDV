package com.example.StorePDV.service;

import com.example.StorePDV.exception.CampoObrigatorioException;
import com.example.StorePDV.exception.EntidadeNaoEncontradaException;
import com.example.StorePDV.exception.OperacaoNaoPermitidaException;
import com.example.StorePDV.model.Cliente;
import com.example.StorePDV.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente cadastrar(Cliente cliente) {
        validar(cliente);
        return clienteRepository.save(cliente);
    }

    public Cliente alterar(Long id, Cliente dados) {
        Cliente cliente = buscarPorId(id);
        if (dados.getNome() != null && !dados.getNome().isBlank())
            cliente.setNome(dados.getNome());
        if (dados.getTelefone() != null) cliente.setTelefone(dados.getTelefone());
        if (dados.getEmail() != null) cliente.setEmail(dados.getEmail());
        if (dados.getEndereco() != null) cliente.setEndereco(dados.getEndereco());
        return clienteRepository.save(cliente);
    }

    public void remover(Long id) {
        Cliente cliente = buscarPorId(id);
        try {
            clienteRepository.delete(cliente);
        } catch (Exception e) {
            throw new OperacaoNaoPermitidaException(
                    "Não é possível remover o cliente '" + cliente.getNome() + "': possui vendas vinculadas.");
        }
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente", id));
    }

    public Cliente buscarPorCpf(String cpf) {
        return clienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente", "CPF: " + cpf));
    }

    public List<Cliente> buscarPorNome(String nome) {
        List<Cliente> resultado = clienteRepository.findByNomeContainingIgnoreCase(nome);
        if (resultado.isEmpty())
            throw new EntidadeNaoEncontradaException("Cliente", nome);
        return resultado;
    }

    private void validar(Cliente cliente) {
        if (cliente == null)
            throw new CampoObrigatorioException("cliente");
        if (cliente.getNome() == null || cliente.getNome().isBlank())
            throw new CampoObrigatorioException("nome");
        if (cliente.getCpf() == null || cliente.getCpf().isBlank())
            throw new CampoObrigatorioException("CPF");
    }
}