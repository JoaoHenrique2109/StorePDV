package com.example.StorePDV.service;

import com.example.StorePDV.exception.CampoObrigatorioException;
import com.example.StorePDV.exception.EntidadeNaoEncontradaException;
import com.example.StorePDV.exception.OperacaoNaoPermitidaException;
import com.example.StorePDV.model.Fornecedor;
import com.example.StorePDV.repository.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FornecedorService {

    @Autowired
    private FornecedorRepository fornecedorRepository;

    public Fornecedor cadastrar(Fornecedor fornecedor) {
        validar(fornecedor);
        return fornecedorRepository.save(fornecedor);
    }

    public Fornecedor alterar(Long id, Fornecedor dados) {
        Fornecedor fornecedor = buscarPorId(id);
        if (dados.getNome() != null && !dados.getNome().isBlank())
            fornecedor.setNome(dados.getNome());
        if (dados.getTelefone() != null) fornecedor.setTelefone(dados.getTelefone());
        if (dados.getEmail() != null) fornecedor.setEmail(dados.getEmail());
        if (dados.getCidade() != null) fornecedor.setCidade(dados.getCidade());
        if (dados.getUf() != null) fornecedor.setUf(dados.getUf());
        return fornecedorRepository.save(fornecedor);
    }

    public void remover(Long id) {
        Fornecedor fornecedor = buscarPorId(id);
        try {
            fornecedorRepository.delete(fornecedor);
        } catch (Exception e) {
            throw new OperacaoNaoPermitidaException(
                    "Não é possível remover o fornecedor '" + fornecedor.getNome() + "': possui produtos vinculados.");
        }
    }

    public List<Fornecedor> listarTodos() {
        return fornecedorRepository.findAll();
    }

    public Fornecedor buscarPorId(Long id) {
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Fornecedor", id));
    }

    public Fornecedor buscarPorCnpj(String cnpj) {
        return fornecedorRepository.findByCnpj(cnpj)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Fornecedor", "CNPJ: " + cnpj));
    }

    public List<Fornecedor> buscarPorNome(String nome) {
        List<Fornecedor> resultado = fornecedorRepository.findByNomeContainingIgnoreCase(nome);
        if (resultado.isEmpty())
            throw new EntidadeNaoEncontradaException("Fornecedor", nome);
        return resultado;
    }

    private void validar(Fornecedor f) {
        if (f == null)
            throw new CampoObrigatorioException("fornecedor");
        if (f.getNome() == null || f.getNome().isBlank())
            throw new CampoObrigatorioException("nome");
        if (f.getCnpj() == null || f.getCnpj().isBlank())
            throw new CampoObrigatorioException("CNPJ");
    }
}