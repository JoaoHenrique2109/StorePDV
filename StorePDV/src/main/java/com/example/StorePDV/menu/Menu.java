package com.example.StorePDV.menu;

import com.example.StorePDV.model.*;
import com.example.StorePDV.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class Menu implements CommandLineRunner {

    @Autowired private ProdutoRepository produtoRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private FornecedorRepository fornecedorRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private VendaRepository vendaRepository;
    @Autowired private ItemVendaRepository itemVendaRepository;
    @Autowired private PagamentoRepository pagamentoRepository;
    @Autowired private PagamentoPixRepository pagamentoPixRepository;
    @Autowired private EstoqueRepository estoqueRepository;
    @Autowired private RelatorioRepository relatorioRepository;

    private final Scanner sc = new Scanner(System.in);
    private Usuario usuarioLogado = null;

    // ─────────────────────────────────────────────────────────────────
    //  ENTRY POINT
    // ─────────────────────────────────────────────────────────────────
    @Override
    public void run(String... args) throws Exception {
        System.out.println("╔══════════════════════════════╗");
        System.out.println("║        STORE PDV             ║");
        System.out.println("╚══════════════════════════════╝");

        if (usuarioRepository.count() == 0) {
            Usuario admin = new Usuario();
            admin.setNome("Administrador");
            admin.setEmail("admin@loja.com");
            admin.setSenha("123456");

            usuarioRepository.save(admin);
            System.out.println("\n[!] Primeiro acesso detectado. Banco de dados vazio.");
            System.out.println("[!] Usuário padrão criado com sucesso!");
            System.out.println(" -> Email: admin@loja.com");
            System.out.println(" -> Senha: 123456\n");
        }

        if (!login()) return;

        boolean rodando = true;
        while (rodando) {
            System.out.println("\n═══════════ MENU PRINCIPAL ═══════════");
            System.out.println("1  - Clientes");
            System.out.println("2  - Fornecedores");
            System.out.println("3  - Produtos");
            System.out.println("4  - Vendas");
            System.out.println("5  - Relatórios");
            System.out.println("0  - Sair");
            System.out.print("Opção: ");
            int opcao = lerInt();
            switch (opcao) {
                case 1 -> menuClientes();
                case 2 -> menuFornecedores();
                case 3 -> menuProdutos();
                case 4 -> menuVendas();
                case 5 -> menuRelatorios();
                case 0 -> rodando = false;
                default -> System.out.println("Opção inválida.");
            }
        }
        System.out.println("Sistema encerrado. Até logo!");
    }

    // ─────────────────────────────────────────────────────────────────
    //  RF-001 – LOGIN / RF-002 – RECUPERAR SENHA
    // ─────────────────────────────────────────────────────────────────
    private boolean login() {
        for (int tentativas = 0; tentativas < 3; tentativas++) {
            System.out.println("\n─── LOGIN ───");
            System.out.print("Email: ");
            String email = sc.nextLine().trim();
            System.out.print("Senha: ");
            String senha = sc.nextLine().trim();

            Optional<Usuario> opt = usuarioRepository.findByEmail(email);
            if (opt.isPresent() && opt.get().getSenha().equals(senha)) {
                usuarioLogado = opt.get();
                System.out.println("Bem-vindo(a), " + usuarioLogado.getNome() + "!");
                return true;
            }

            System.out.println("Login ou senha inválidos. Tentativas restantes: " + (2 - tentativas));

            if (tentativas == 1) {
                System.out.print("Esqueceu a senha? (s/n): ");
                if (sc.nextLine().trim().equalsIgnoreCase("s")) {
                    recuperarSenha();
                }
            }
        }
        System.out.println("Acesso bloqueado após 3 tentativas.");
        return false;
    }

    // RF-002
    private void recuperarSenha() {
        System.out.print("Informe seu email cadastrado: ");
        String email = sc.nextLine().trim();
        Optional<Usuario> opt = usuarioRepository.findByEmail(email);
        if (opt.isPresent()) {
            System.out.println("Um link de recuperação foi enviado para: " + email);
        } else {
            System.out.println("Email não encontrado.");
        }
    }

    // ─────────────────────────────────────────────────────────────────
    //  RF-003 a RF-006 – CLIENTES
    // ─────────────────────────────────────────────────────────────────
    private void menuClientes() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n─── CLIENTES ───");
            System.out.println("1 - Cadastrar cliente");
            System.out.println("2 - Alterar cliente");
            System.out.println("3 - Remover cliente");
            System.out.println("4 - Listar clientes");
            System.out.println("0 - Voltar");
            System.out.print("Opção: ");
            switch (lerInt()) {
                case 1 -> cadastrarCliente();
                case 2 -> alterarCliente();
                case 3 -> removerCliente();
                case 4 -> listarClientes();
                case 0 -> voltar = true;
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    // RF-003
    private void cadastrarCliente() {
        System.out.println("\n─── CADASTRAR CLIENTE ───");
        Cliente c = new Cliente();
        System.out.print("Nome: "); c.setNome(sc.nextLine().trim());
        System.out.print("CPF: "); c.setCpf(sc.nextLine().trim());
        System.out.print("Telefone: "); c.setTelefone(sc.nextLine().trim());
        System.out.print("Email: "); c.setEmail(sc.nextLine().trim());
        System.out.print("Endereço: "); c.setEndereco(sc.nextLine().trim());

        if (c.getNome().isEmpty() || c.getCpf().isEmpty()) {
            System.out.println("Nome e CPF são obrigatórios.");
            return;
        }
        clienteRepository.save(c);
        System.out.println("Cliente cadastrado com sucesso!");
    }

    // RF-004
    private void alterarCliente() {
        System.out.println("\n─── ALTERAR CLIENTE ───");
        System.out.print("Buscar por CPF ou nome: ");
        String busca = sc.nextLine().trim();
        Cliente cliente = buscarCliente(busca);
        if (cliente == null) return;

        System.out.println("Cliente: " + cliente.getNome() + " | Deixe em branco para manter o valor atual.");
        System.out.print("Novo nome [" + cliente.getNome() + "]: ");
        String nome = sc.nextLine().trim();
        if (!nome.isEmpty()) cliente.setNome(nome);

        System.out.print("Novo telefone [" + cliente.getTelefone() + "]: ");
        String tel = sc.nextLine().trim();
        if (!tel.isEmpty()) cliente.setTelefone(tel);

        System.out.print("Novo email [" + cliente.getEmail() + "]: ");
        String email = sc.nextLine().trim();
        if (!email.isEmpty()) cliente.setEmail(email);

        System.out.print("Novo endereço [" + cliente.getEndereco() + "]: ");
        String end = sc.nextLine().trim();
        if (!end.isEmpty()) cliente.setEndereco(end);

        clienteRepository.save(cliente);
        System.out.println("Cliente atualizado com sucesso!");
    }

    // RF-005
    private void removerCliente() {
        System.out.println("\n─── REMOVER CLIENTE ───");
        System.out.print("Buscar por CPF ou nome: ");
        String busca = sc.nextLine().trim();
        Cliente cliente = buscarCliente(busca);
        if (cliente == null) return;

        System.out.print("Confirmar remoção de '" + cliente.getNome() + "'? (s/n): ");
        if (!sc.nextLine().trim().equalsIgnoreCase("s")) return;

        try {
            clienteRepository.delete(cliente);
            System.out.println("Cliente removido com sucesso.");
        } catch (Exception e) {
            System.out.println("Não foi possível remover: cliente possui vínculos com vendas.");
        }
    }

    // RF-006
    private void listarClientes() {
        System.out.println("\n─── LISTA DE CLIENTES ───");
        List<Cliente> lista = clienteRepository.findAll();
        if (lista.isEmpty()) { System.out.println("Nenhum cliente encontrado."); return; }
        System.out.printf("%-5s %-25s %-15s %-20s%n", "ID", "Nome", "Telefone", "Email");
        System.out.println("─".repeat(70));
        for (Cliente c : lista) {
            System.out.printf("%-5d %-25s %-15s %-20s%n",
                    c.getId(), c.getNome(), nvl(c.getTelefone()), nvl(c.getEmail()));
        }
    }

    private Cliente buscarCliente(String busca) {
        Optional<Cliente> porCpf = clienteRepository.findByCpf(busca);
        if (porCpf.isPresent()) return porCpf.get();
        List<Cliente> porNome = clienteRepository.findByNomeContainingIgnoreCase(busca);
        if (porNome.isEmpty()) { System.out.println("Cliente não encontrado."); return null; }
        if (porNome.size() == 1) return porNome.get(0);
        System.out.println("Vários clientes encontrados:");
        for (int i = 0; i < porNome.size(); i++)
            System.out.println((i + 1) + " - " + porNome.get(i).getNome() + " | CPF: " + porNome.get(i).getCpf());
        System.out.print("Escolha o número: ");
        int idx = lerInt() - 1;
        return (idx >= 0 && idx < porNome.size()) ? porNome.get(idx) : null;
    }

    // ─────────────────────────────────────────────────────────────────
    //  RF-007 a RF-010 – FORNECEDORES
    // ─────────────────────────────────────────────────────────────────
    private void menuFornecedores() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n─── FORNECEDORES ───");
            System.out.println("1 - Cadastrar fornecedor");
            System.out.println("2 - Alterar fornecedor");
            System.out.println("3 - Remover fornecedor");
            System.out.println("4 - Listar fornecedores");
            System.out.println("0 - Voltar");
            System.out.print("Opção: ");
            switch (lerInt()) {
                case 1 -> cadastrarFornecedor();
                case 2 -> alterarFornecedor();
                case 3 -> removerFornecedor();
                case 4 -> listarFornecedores();
                case 0 -> voltar = true;
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    // RF-007
    private void cadastrarFornecedor() {
        System.out.println("\n─── CADASTRAR FORNECEDOR ───");
        Fornecedor f = new Fornecedor();
        System.out.print("Nome: "); f.setNome(sc.nextLine().trim());
        System.out.print("CNPJ: "); f.setCnpj(sc.nextLine().trim());
        System.out.print("Razão Social: "); f.setRazaoSocial(sc.nextLine().trim());
        System.out.print("Telefone: "); f.setTelefone(sc.nextLine().trim());
        System.out.print("Email: "); f.setEmail(sc.nextLine().trim());
        System.out.print("Cidade: "); f.setCidade(sc.nextLine().trim());
        System.out.print("Estado (UF): "); f.setUf(sc.nextLine().trim());

        if (f.getNome().isEmpty() || f.getCnpj().isEmpty()) {
            System.out.println("Nome e CNPJ são obrigatórios.");
            return;
        }
        fornecedorRepository.save(f);
        System.out.println("Fornecedor cadastrado com sucesso!");
    }

    // RF-008
    private void alterarFornecedor() {
        System.out.println("\n─── ALTERAR FORNECEDOR ───");
        System.out.print("Buscar por CNPJ ou nome: ");
        String busca = sc.nextLine().trim();
        Fornecedor f = buscarFornecedor(busca);
        if (f == null) return;

        System.out.println("Deixe em branco para manter o valor atual.");
        System.out.print("Novo nome [" + f.getNome() + "]: ");
        String nome = sc.nextLine().trim();
        if (!nome.isEmpty()) f.setNome(nome);

        System.out.print("Novo telefone [" + nvl(f.getTelefone()) + "]: ");
        String tel = sc.nextLine().trim();
        if (!tel.isEmpty()) f.setTelefone(tel);

        System.out.print("Novo email [" + nvl(f.getEmail()) + "]: ");
        String email = sc.nextLine().trim();
        if (!email.isEmpty()) f.setEmail(email);

        fornecedorRepository.save(f);
        System.out.println("Fornecedor atualizado com sucesso!");
    }

    // RF-009
    private void removerFornecedor() {
        System.out.println("\n─── REMOVER FORNECEDOR ───");
        System.out.print("Buscar por CNPJ ou nome: ");
        String busca = sc.nextLine().trim();
        Fornecedor f = buscarFornecedor(busca);
        if (f == null) return;

        System.out.print("Confirmar remoção de '" + f.getNome() + "'? (s/n): ");
        if (!sc.nextLine().trim().equalsIgnoreCase("s")) return;

        try {
            fornecedorRepository.delete(f);
            System.out.println("Fornecedor removido com sucesso.");
        } catch (Exception e) {
            System.out.println("Não foi possível remover: fornecedor possui vínculos com produtos.");
        }
    }

    // RF-010
    private void listarFornecedores() {
        System.out.println("\n─── LISTA DE FORNECEDORES ───");
        List<Fornecedor> lista = fornecedorRepository.findAll();
        if (lista.isEmpty()) { System.out.println("Nenhum fornecedor encontrado."); return; }
        System.out.printf("%-5s %-25s %-18s %-15s%n", "ID", "Nome", "CNPJ", "Telefone");
        System.out.println("─".repeat(65));
        for (Fornecedor f : lista) {
            System.out.printf("%-5d %-25s %-18s %-15s%n",
                    f.getId(), f.getNome(), nvl(f.getCnpj()), nvl(f.getTelefone()));
        }
    }

    private Fornecedor buscarFornecedor(String busca) {
        Optional<Fornecedor> porCnpj = fornecedorRepository.findByCnpj(busca);
        if (porCnpj.isPresent()) return porCnpj.get();
        List<Fornecedor> porNome = fornecedorRepository.findByNomeContainingIgnoreCase(busca);
        if (porNome.isEmpty()) { System.out.println("Fornecedor não encontrado."); return null; }
        if (porNome.size() == 1) return porNome.get(0);
        for (int i = 0; i < porNome.size(); i++)
            System.out.println((i + 1) + " - " + porNome.get(i).getNome());
        System.out.print("Escolha o número: ");
        int idx = lerInt() - 1;
        return (idx >= 0 && idx < porNome.size()) ? porNome.get(idx) : null;
    }

    // ─────────────────────────────────────────────────────────────────
    //  RF-011 a RF-014 – PRODUTOS
    // ─────────────────────────────────────────────────────────────────
    private void menuProdutos() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n─── PRODUTOS ───");
            System.out.println("1 - Cadastrar produto");
            System.out.println("2 - Alterar produto");
            System.out.println("3 - Remover produto");
            System.out.println("4 - Listar produtos");
            System.out.println("0 - Voltar");
            System.out.print("Opção: ");
            switch (lerInt()) {
                case 1 -> cadastrarProduto();
                case 2 -> alterarProduto();
                case 3 -> removerProduto();
                case 4 -> listarProdutos();
                case 0 -> voltar = true;
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    // RF-011
    private void cadastrarProduto() {
        System.out.println("\n─── CADASTRAR PRODUTO ───");
        Produto p = new Produto();
        System.out.print("Nome: "); p.setNome(sc.nextLine().trim());
        System.out.print("Descrição: "); p.setDescricao(sc.nextLine().trim());
        System.out.print("Preço de venda: R$ "); p.setPreco(lerDouble());
        System.out.print("Preço de custo: R$ "); p.setPrecoCusto(lerDouble());
        System.out.print("Quantidade em estoque: "); p.setQuantidade(lerInt());
        System.out.print("Estoque mínimo: "); p.setEstoqueMinimo(lerInt());

        if (p.getNome().isEmpty() || p.getPreco() <= 0) {
            System.out.println("Nome e preço são obrigatórios e preço deve ser maior que zero.");
            return;
        }

        produtoRepository.save(p);
        System.out.println("Produto cadastrado com sucesso!");
    }

    // RF-012
    private void alterarProduto() {
        System.out.println("\n─── ALTERAR PRODUTO ───");
        System.out.print("Buscar produto por nome: ");
        String busca = sc.nextLine().trim();
        Produto p = buscarProduto(busca);
        if (p == null) return;

        System.out.println("Deixe em branco para manter o valor atual.");
        System.out.print("Novo nome [" + p.getNome() + "]: ");
        String nome = sc.nextLine().trim();
        if (!nome.isEmpty()) p.setNome(nome);

        System.out.print("Novo preço [" + p.getPreco() + "] (0 para manter): ");
        double preco = lerDouble();
        if (preco > 0) p.setPreco(preco);

        System.out.print("Nova quantidade [" + p.getQuantidade() + "] (-1 para manter): ");
        int qtd = lerInt();
        if (qtd >= 0) p.setQuantidade(qtd);

        produtoRepository.save(p);
        System.out.println("Produto atualizado com sucesso!");
    }

    // RF-013
    private void removerProduto() {
        System.out.println("\n─── REMOVER PRODUTO ───");
        System.out.print("Buscar produto por nome: ");
        String busca = sc.nextLine().trim();
        Produto p = buscarProduto(busca);
        if (p == null) return;

        System.out.print("Confirmar remoção de '" + p.getNome() + "'? (s/n): ");
        if (!sc.nextLine().trim().equalsIgnoreCase("s")) return;

        try {
            produtoRepository.delete(p);
            System.out.println("Produto removido com sucesso.");
        } catch (Exception e) {
            System.out.println("Não foi possível remover: produto vinculado a vendas.");
        }
    }

    // RF-014
    private void listarProdutos() {
        System.out.println("\n─── LISTA DE PRODUTOS ───");
        List<Produto> lista = produtoRepository.findAll();
        if (lista.isEmpty()) { System.out.println("Nenhum produto encontrado."); return; }
        System.out.printf("%-5s %-25s %-10s %-8s %-8s%n", "ID", "Nome", "Preço", "Estoque", "Mín.");
        System.out.println("─".repeat(60));
        for (Produto p : lista) {
            String alerta = p.getQuantidade() <= p.getEstoqueMinimo() ? " ⚠ BAIXO" : "";
            System.out.printf("%-5d %-25s R$%-8.2f %-8d %-8d%s%n",
                    p.getId(), p.getNome(), p.getPreco(), p.getQuantidade(), p.getEstoqueMinimo(), alerta);
        }
    }

    private Produto buscarProduto(String busca) {
        List<Produto> lista = produtoRepository.findByNomeContainingIgnoreCase(busca);
        if (lista.isEmpty()) { System.out.println("Produto não encontrado."); return null; }
        if (lista.size() == 1) return lista.get(0);
        for (int i = 0; i < lista.size(); i++)
            System.out.println((i + 1) + " - " + lista.get(i).getNome() + " | R$ " + lista.get(i).getPreco());
        System.out.print("Escolha o número: ");
        int idx = lerInt() - 1;
        return (idx >= 0 && idx < lista.size()) ? lista.get(idx) : null;
    }

    // ─────────────────────────────────────────────────────────────────
    //  RF-015 a RF-020 – VENDAS
    // ─────────────────────────────────────────────────────────────────
    private void menuVendas() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n─── VENDAS ───");
            System.out.println("1 - Realizar venda");
            System.out.println("2 - Cancelar venda");
            System.out.println("3 - Alterar venda");
            System.out.println("4 - Listar vendas");
            System.out.println("0 - Voltar");
            System.out.print("Opção: ");
            switch (lerInt()) {
                case 1 -> realizarVenda();
                case 2 -> cancelarVenda();
                case 3 -> alterarVenda();
                case 4 -> listarVendas();
                case 0 -> voltar = true;
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    // RF-015
    private void realizarVenda() {
        System.out.println("\n─── PDV – NOVA VENDA ───");

        Venda venda = new Venda();
        venda.setDataVenda(LocalDateTime.now());
        venda.setUsuario(usuarioLogado);
        venda.setStatus(Venda.Status.ABERTA);

        // Cliente (opcional)
        System.out.print("Identificar cliente? (s/n): ");
        if (sc.nextLine().trim().equalsIgnoreCase("s")) {
            System.out.print("CPF ou nome do cliente: ");
            Cliente c = buscarCliente(sc.nextLine().trim());
            if (c != null) venda.setCliente(c);
        }

        venda = vendaRepository.save(venda);
        List<ItemVenda> itens = new ArrayList<>();
        double total = 0;

        boolean adicionando = true;
        while (adicionando) {
            System.out.print("\nNome do produto (ou 'fim' para finalizar): ");
            String nomeProd = sc.nextLine().trim();
            if (nomeProd.equalsIgnoreCase("fim")) { adicionando = false; continue; }

            Produto p = buscarProduto(nomeProd);
            if (p == null) continue;

            System.out.print("Quantidade: ");
            int qtd = lerInt();

            if (qtd <= 0) { System.out.println("Quantidade inválida."); continue; }
            if (p.getQuantidade() < qtd) {
                System.out.println("Estoque insuficiente. Disponível: " + p.getQuantidade());
                continue;
            }

            ItemVenda item = new ItemVenda();
            item.setVenda(venda);
            item.setProduto(p);
            item.setQuantidade(qtd);
            item.setPrecoUnitario(p.getPreco());
            itemVendaRepository.save(item);
            itens.add(item);

            // RF-019 – atualiza estoque
            p.setQuantidade(p.getQuantidade() - qtd);
            produtoRepository.save(p);

            total += item.getSubtotal();
            System.out.printf("Adicionado: %s x%d = R$ %.2f%n", p.getNome(), qtd, item.getSubtotal());
        }

        if (itens.isEmpty()) {
            System.out.println("Nenhum item adicionado. Venda cancelada.");
            vendaRepository.delete(venda);
            return;
        }

        venda.setTotal(total);
        System.out.printf("%n╔══════════════════════════╗%n");
        System.out.printf("║  TOTAL: R$ %-14.2f║%n", total);
        System.out.printf("╚══════════════════════════╝%n");

        // RF-020 – Pagamento
        System.out.println("Forma de pagamento:");
        System.out.println("1 - Dinheiro");
        System.out.println("2 - Cartão");
        System.out.println("3 - PIX");
        System.out.print("Opção: ");
        int formaPag = lerInt();

        if (formaPag == 3) {
            pagarComPix(venda, total);
        } else {
            Pagamento pag = new Pagamento();
            pag.setTipo(formaPag == 1 ? "DINHEIRO" : "CARTAO");
            pag.setValor(total);
            pag.setStatus("APROVADO");
            pag.setData(LocalDate.now());
            pag.setVenda(venda);
            pagamentoRepository.save(pag);
        }

        venda.setStatus(Venda.Status.FINALIZADA);
        vendaRepository.save(venda);
        System.out.println("Venda finalizada com sucesso! ID: " + venda.getId());

        // RF-018 – Comprovante
        emitirComprovante(venda, itens);
    }

    // RF-020 – PIX
    private void pagarComPix(Venda venda, double total) {
        PagamentoPix pix = new PagamentoPix();
        pix.setTipo("PIX");
        pix.setValor(total);
        pix.setStatus("PENDENTE");
        pix.setData(LocalDate.now());
        pix.setVenda(venda);
        pix.setChavePix("storePDV@loja.com");
        pix.setQrCode("00020126580014BR.GOV.BCB.PIX0136storePDV@loja.com5204000053039865802BR5913StorePDV6009SAO PAULO62070503***63046CA3");
        pix.setExpiracao(LocalDateTime.now().plusMinutes(30));
        pagamentoPixRepository.save(pix);

        System.out.println("\n──── QR CODE PIX ────");
        System.out.println("Chave: " + pix.getChavePix());
        System.out.println("Valor: R$ " + String.format("%.2f", total));
        System.out.println("Expira em: 30 minutos");
        System.out.println("█████████████████████");
        System.out.println("█ QR CODE SIMULADO  █");
        System.out.println("█ " + pix.getQrCode().substring(0, 20) + "... █");
        System.out.println("█████████████████████");
        System.out.print("Confirmar recebimento do PIX? (s/n): ");
        if (sc.nextLine().trim().equalsIgnoreCase("s")) {
            pix.setStatus("APROVADO");
            pagamentoPixRepository.save(pix);
            System.out.println("PIX confirmado!");
        } else {
            System.out.println("Aguardando confirmação do PIX...");
        }
    }

    // RF-018 – Comprovante
    private void emitirComprovante(Venda venda, List<ItemVenda> itens) {
        System.out.println("\n══════ COMPROVANTE DE VENDA ══════");
        System.out.println("StorePDV – Sistema de Gestão e Vendas");
        System.out.println("─────────────────────────────────────");
        System.out.println("Venda #" + venda.getId());
        System.out.println("Data: " + venda.getDataVenda().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        if (venda.getCliente() != null)
            System.out.println("Cliente: " + venda.getCliente().getNome());
        System.out.println("Operador: " + usuarioLogado.getNome());
        System.out.println("─────────────────────────────────────");
        for (ItemVenda item : itens) {
            System.out.printf("%-20s x%-3d R$%.2f%n",
                    item.getProduto().getNome(), item.getQuantidade(), item.getSubtotal());
        }
        System.out.println("─────────────────────────────────────");
        System.out.printf("TOTAL: R$ %.2f%n", venda.getTotal());
        System.out.println("══════════════════════════════════════");
        System.out.print("Imprimir comprovante? (s/n): ");
        sc.nextLine(); // consome enter
        System.out.println("Comprovante gerado.");
    }

    // RF-016
    private void cancelarVenda() {
        System.out.println("\n─── CANCELAR VENDA ───");
        System.out.print("ID da venda: ");
        long id = lerLong();
        Optional<Venda> opt = vendaRepository.findById(id);
        if (opt.isEmpty()) { System.out.println("Venda não encontrada."); return; }

        Venda venda = opt.get();
        if (venda.getStatus() == Venda.Status.CANCELADA) {
            System.out.println("Esta venda já está cancelada."); return;
        }
        if (venda.getStatus() == Venda.Status.FINALIZADA) {
            System.out.println("Não é possível cancelar uma venda já finalizada."); return;
        }

        // Reverte estoque
        List<ItemVenda> itens = itemVendaRepository.findByVenda_Id(id);
        for (ItemVenda item : itens) {
            Produto p = item.getProduto();
            p.setQuantidade(p.getQuantidade() + item.getQuantidade());
            produtoRepository.save(p);
        }

        venda.setStatus(Venda.Status.CANCELADA);
        vendaRepository.save(venda);
        System.out.println("Venda #" + id + " cancelada. Estoque revertido.");
    }

    // RF-017
    private void alterarVenda() {
        System.out.println("\n─── ALTERAR VENDA ───");
        System.out.print("ID da venda: ");
        long id = lerLong();
        Optional<Venda> opt = vendaRepository.findById(id);
        if (opt.isEmpty()) { System.out.println("Venda não encontrada."); return; }

        Venda venda = opt.get();
        if (venda.getStatus() != Venda.Status.ABERTA) {
            System.out.println("Apenas vendas ABERTAS podem ser alteradas."); return;
        }

        List<ItemVenda> itens = itemVendaRepository.findByVenda_Id(id);
        System.out.println("Itens atuais:");
        for (ItemVenda item : itens) {
            System.out.println("- " + item.getProduto().getNome() + " x" + item.getQuantidade());
        }

        System.out.print("Vincular cliente? (s/n): ");
        if (sc.nextLine().trim().equalsIgnoreCase("s")) {
            System.out.print("CPF ou nome do cliente: ");
            Cliente c = buscarCliente(sc.nextLine().trim());
            if (c != null) { venda.setCliente(c); vendaRepository.save(venda); System.out.println("Cliente atualizado."); }
        }
    }

    private void listarVendas() {
        System.out.println("\n─── LISTA DE VENDAS ───");
        List<Venda> lista = vendaRepository.findAll();
        if (lista.isEmpty()) { System.out.println("Nenhuma venda encontrada."); return; }
        System.out.printf("%-5s %-20s %-12s %-10s%n", "ID", "Data", "Total", "Status");
        System.out.println("─".repeat(52));
        for (Venda v : lista) {
            System.out.printf("%-5d %-20s R$%-10.2f %-10s%n",
                    v.getId(),
                    v.getDataVenda() != null ? v.getDataVenda().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "-",
                    v.getTotal(),
                    v.getStatus());
        }
    }

    // ─────────────────────────────────────────────────────────────────
    //  RF-021 – RELATÓRIOS
    // ─────────────────────────────────────────────────────────────────
    private void menuRelatorios() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n─── RELATÓRIOS ───");
            System.out.println("1 - Relatório de vendas por período");
            System.out.println("2 - Produtos com estoque baixo");
            System.out.println("3 - Resumo do dia");
            System.out.println("0 - Voltar");
            System.out.print("Opção: ");
            switch (lerInt()) {
                case 1 -> relatorioVendasPeriodo();
                case 2 -> relatorioEstoqueBaixo();
                case 3 -> resumoDia();
                case 0 -> voltar = true;
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private void relatorioVendasPeriodo() {
        System.out.println("\n─── RELATÓRIO DE VENDAS POR PERÍODO ───");
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.print("Data início (dd/MM/yyyy): ");
        LocalDateTime inicio = LocalDate.parse(sc.nextLine().trim(), fmt).atStartOfDay();
        System.out.print("Data fim (dd/MM/yyyy): ");
        LocalDateTime fim = LocalDate.parse(sc.nextLine().trim(), fmt).atTime(23, 59, 59);

        List<Venda> vendas = vendaRepository.findVendasPorPeriodo(inicio, fim);
        if (vendas.isEmpty()) { System.out.println("Sem dados disponíveis para o período."); return; }

        double totalGeral = 0;
        System.out.printf("%-5s %-20s %-25s %-10s%n", "ID", "Data", "Cliente", "Total");
        System.out.println("─".repeat(65));
        for (Venda v : vendas) {
            String cliente = v.getCliente() != null ? v.getCliente().getNome() : "Consumidor Final";
            System.out.printf("%-5d %-20s %-25s R$%.2f%n",
                    v.getId(),
                    v.getDataVenda().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    cliente, v.getTotal());
            totalGeral += v.getTotal();
        }
        System.out.println("─".repeat(65));
        System.out.printf("Total de vendas: %d | Valor total: R$ %.2f%n", vendas.size(), totalGeral);

        // Salva registro do relatório
        Relatorio rel = new Relatorio();
        rel.setTipo("VENDAS_PERIODO");
        rel.setDataGeracao(LocalDateTime.now());
        rel.setPeriodoInicio(inicio.toLocalDate());
        rel.setPeriodoFim(fim.toLocalDate());
        relatorioRepository.save(rel);
    }

    private void relatorioEstoqueBaixo() {
        System.out.println("\n─── PRODUTOS COM ESTOQUE BAIXO ───");
        List<Produto> lista = produtoRepository.findProdutosComEstoqueBaixo();
        if (lista.isEmpty()) { System.out.println("Nenhum produto com estoque abaixo do mínimo."); return; }
        System.out.printf("%-5s %-25s %-10s %-10s%n", "ID", "Nome", "Estoque", "Mínimo");
        System.out.println("─".repeat(55));
        for (Produto p : lista) {
            System.out.printf("%-5d %-25s %-10d %-10d%n", p.getId(), p.getNome(), p.getQuantidade(), p.getEstoqueMinimo());
        }
    }

    private void resumoDia() {
        System.out.println("\n─── RESUMO DO DIA ───");
        LocalDateTime inicio = LocalDate.now().atStartOfDay();
        LocalDateTime fim = LocalDate.now().atTime(23, 59, 59);
        List<Venda> vendas = vendaRepository.findVendasPorPeriodo(inicio, fim);
        long count = vendas.size();
        double total = vendas.stream().mapToDouble(Venda::getTotal).sum();
        System.out.println("Data: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("Total de vendas: " + count);
        System.out.printf("Faturamento: R$ %.2f%n", total);
        if (count > 0) System.out.printf("Ticket médio: R$ %.2f%n", total / count);
    }

    // ─────────────────────────────────────────────────────────────────
    //  UTILITÁRIOS
    // ─────────────────────────────────────────────────────────────────
    private int lerInt() {
        try {
            int val = Integer.parseInt(sc.nextLine().trim());
            return val;
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido, usando 0.");
            return 0;
        }
    }

    private long lerLong() {
        try { return Long.parseLong(sc.nextLine().trim()); }
        catch (NumberFormatException e) { return 0L; }
    }

    private double lerDouble() {
        try { return Double.parseDouble(sc.nextLine().trim().replace(",", ".")); }
        catch (NumberFormatException e) { return 0.0; }
    }

    private String nvl(String s) { return s != null ? s : ""; }
}
