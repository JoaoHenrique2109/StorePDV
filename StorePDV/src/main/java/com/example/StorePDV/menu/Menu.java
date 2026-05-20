package com.example.StorePDV.menu;

import com.example.StorePDV.model.Produto;
import com.example.StorePDV.repository.ClienteRepository;
import com.example.StorePDV.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class Menu implements CommandLineRunner {

    @Autowired
        private ProdutoRepository produtoRepository;
        private ClienteRepository clienteRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Aplicação iniciada!");

        Scanner sc = new Scanner(System.in);
        System.out.println("=== STORE PDV ===");
        System.out.println("1 - Cadastrar Produto");
        System.out.println("2 - Listar Produtos");
        System.out.println("Escolha uma opcao: ");
        int opcao = sc.nextInt();

        switch (opcao) {
            case 1:
                System.out.println("Nome do produto: ");
                sc.nextLine();
                String nome = sc.nextLine();

                System.out.println("Preco do produto: ");
                double preco = sc.nextDouble();

                Produto produto = new Produto(nome, preco);
                produto.setNome(nome);
                produto.setPreco(preco);

                produtoRepository.save(produto);
                System.out.println("Produto cadastrado com sucesso!");
                break;


        }
    }
}
