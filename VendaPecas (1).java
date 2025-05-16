import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class VendaPecas {
    static Scanner scanner = new Scanner(System.in);

    static class Item {
        String nome;
        double preco;

        Item(String nome, double preco) {
            this.nome = nome;
            this.preco = preco;
        }
    }

    // Categorias e produtos
    static Map<String, List<Item>> categorias = new LinkedHashMap<>();

    static {
        categorias.put("Placas Mãe", Arrays.asList(
            new Item("ASUS Prime B450M-A", 450.00),
            new Item("Gigabyte B550 AORUS PRO", 920.50),
            new Item("MSI MPG Z490 Gaming Edge", 1240.00)
        ));
        categorias.put("Placas de Vídeo", Arrays.asList(
            new Item("NVIDIA RTX 3060", 3200.00),
            new Item("AMD Radeon RX 6600 XT", 2800.00),
            new Item("NVIDIA RTX 3080 Ti", 9500.00)
        ));
        categorias.put("Memórias RAM", Arrays.asList(
            new Item("Corsair Vengeance 16GB DDR4", 550.00),
            new Item("Kingston HyperX Fury 8GB DDR4", 280.00)
        ));
        categorias.put("Fontes", Arrays.asList(
            new Item("Corsair 650W 80 Plus Gold", 400.00),
            new Item("EVGA 500W 80 Plus Bronze", 300.00)
        ));
    }

    public static void main(String[] args) throws IOException {
        List<Item> pedido = new ArrayList<>();

        System.out.println("===== CATÁLOGO DE PEÇAS DE COMPUTADOR =====");

        // Mostrar categorias e produtos
        int globalIndex = 1;
        Map<Integer, Item> mapaItens = new HashMap<>();

        for (Map.Entry<String, List<Item>> categoria : categorias.entrySet()) {
            System.out.println("\n--- " + categoria.getKey() + " ---");
            for (Item item : categoria.getValue()) {
                System.out.printf("[%d] %s - R$ %.2f%n", globalIndex, item.nome, item.preco);
                mapaItens.put(globalIndex, item);
                globalIndex++;
            }
        }

        System.out.println("\nSelecione os produtos (digite 0 para encerrar):");
        while (true) {
            System.out.print("Escolha: ");
            int escolha = scanner.nextInt();
            if (escolha == 0) break;
            if (mapaItens.containsKey(escolha)) {
                pedido.add(mapaItens.get(escolha));
                System.out.println("Adicionado: " + mapaItens.get(escolha).nome);
            } else {
                System.out.println("Opção inválida.");
            }
        }

        if (pedido.isEmpty()) {
            System.out.println("Nenhum produto selecionado. Saindo...");
            return;
        }

        double total = pedido.stream().mapToDouble(i -> i.preco).sum();
        System.out.printf("\nTotal do pedido: R$ %.2f%n", total);

        System.out.println("\nFormas de pagamento:\n1 - Débito\n2 - Crédito\n3 - Pix");
        int formaPagamento = scanner.nextInt();
        int parcelas = 1;

        if (formaPagamento == 2) {
            System.out.print("Quantas parcelas? ");
            parcelas = scanner.nextInt();
            double valorParcela = total / parcelas;
            System.out.printf("Pagamento em %d x de R$ %.2f%n", parcelas, valorParcela);
        }

        scanner.nextLine(); // limpa buffer
        String forma = "";
        String nomeCartao = "";

        if (formaPagamento == 1 || formaPagamento == 2) {
            forma = formaPagamento == 1 ? "Débito" : "Crédito";

            System.out.print("Número do cartão (16 dígitos): ");
            String numero = scanner.nextLine();
            while (numero.length() != 16 || !numero.matches("\\d+")) {
                System.out.print("Número inválido. Digite novamente: ");
                numero = scanner.nextLine();
            }

            System.out.print("Validade (MM/AA): ");
            String validade = scanner.nextLine();
            while (!validade.matches("\\d{2}/\\d{2}")) {
                System.out.print("Formato inválido. Digite novamente (MM/AA): ");
                validade = scanner.nextLine();
            }

            System.out.print("Código de segurança (3 dígitos): ");
            String codigo = scanner.nextLine();
            while (!codigo.matches("\\d{3}")) {
                System.out.print("Código inválido. Digite novamente: ");
                codigo = scanner.nextLine();
            }

            System.out.print("Nome no cartão: ");
            nomeCartao = scanner.nextLine();

            System.out.print("Digite a senha do cartão (simulada): ");
            String senha = scanner.nextLine();
            System.out.println("Processando pagamento...");
        } else {
            forma = "Pix";
            System.out.println("Chave PIX: 45 9991497, ou use o qr code");
            exibirQRCodeFakeBaixinhoEGordinho();
        }

        // Gera recibo
        try (FileWriter writer = new FileWriter("recibo.txt")) {
            writer.write("===== RECIBO DE COMPRA =====\n");
            for (Item item : pedido) {
                writer.write(String.format("%-30s R$ %.2f%n", item.nome, item.preco));
            }
            writer.write(String.format("Total: R$ %.2f%n", total));
            writer.write("Pagamento: " + forma + "\n");
            if (formaPagamento == 2) {
                writer.write("Parcelado em " + parcelas + "x\n");
            }
            if (!nomeCartao.isEmpty()) {
                writer.write("Nome do cartão: " + nomeCartao + "\n");
            }
            writer.write("============================\n");
            writer.write("Obrigado pela preferência!\n");
        }

        System.out.println("\nRecibo salvo em: recibo.txt");
        System.out.println("Pedido finalizado com sucesso!");
    }

    // QR Code baixinho e gordinho
    public static void exibirQRCodeFakeBaixinhoEGordinho() {
        String[] qrCompacto = {
            "████████████████",
            "█ █  █ ██ ██  ██",
            "█      █ ██ ██ █",
            "█  ██  ██  █  ██",
            "████████████████",
            "█ █ █ █ █ █ █ ██", 
            "██ █ █ █ █ █ █ █",
            "████████████████"
        };

        System.out.println("\nSeu PIX QR Code (fake baixinho e gordinho):");
        for (String linha : qrCompacto) {
            // duplica caractere pra largura "gordinha"
            StringBuilder linhaGordinha = new StringBuilder();
            for (char c : linha.toCharArray()) {
                linhaGordinha.append(c).append(c);
            }
            // imprime cada linha 2x pra altura "gordinha"
            System.out.println(linhaGordinha);
            System.out.println(linhaGordinha);
        }
        System.out.println("(Apenas aparência, não funciona para leitura real)\n");
    }
}
