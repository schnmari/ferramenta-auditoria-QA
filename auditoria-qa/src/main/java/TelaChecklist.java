/**package br.pucpr.auditoria;*/

import java.util.function.Function;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Tela do checklist de auditoria.
 * Contém a tabela principal (editável), a tabela de resumo das NCs
 * por classificação e a tabela com a taxa de aderência.
 */
public class TelaChecklist {

    // Lista de itens do checklist (a tabela observa essa lista)
    private final ObservableList<ItemChecklist> itens = FXCollections.observableArrayList();
    private TableView<ItemChecklist> tabela;

    // Labels da tabela de resumo de NCs por classificação
    private final Label qtdBaixa = new Label("0");
    private final Label qtdMedia = new Label("0");
    private final Label qtdAlta = new Label("0");
    private final Label qtdUrgente = new Label("0");
    private final Label qtdTotalNC = new Label("0");

    // Labels da tabela de aderência
    private final Label lblConformes = new Label("0");
    private final Label lblNaoAplicaveis = new Label("0");
    private final Label lblAvaliados = new Label("0");
    private final Label lblAderencia = new Label("0,0%");

    public VBox criar() {
        // ---------- Tabela principal ----------
        tabela = new TableView<>(itens);
        tabela.setEditable(true); // permite editar as células com duplo clique
        tabela.setPrefHeight(400);

        // Coluna Nº (não editável)
        TableColumn<ItemChecklist, String> colNumero = new TableColumn<>("Nº");
        colNumero.setCellValueFactory(dados -> dados.getValue().numeroProperty());
        colNumero.setPrefWidth(40);
        colNumero.setEditable(false);
        tabela.getColumns().add(colNumero);

        // Demais colunas (texto livre ou dropdown)
        tabela.getColumns().add(colunaTexto("Descrição", ItemChecklist::descricaoProperty, 260));
        tabela.getColumns().add(colunaCombo("Resultado", ItemChecklist::resultadoProperty, 110,
                "Sim", "Não", "Não se aplica"));
        tabela.getColumns().add(colunaTexto("Data/hora identificação NC", ItemChecklist::dataIdentificacaoProperty, 150));
        tabela.getColumns().add(colunaTexto("Responsável pela resolução", ItemChecklist::responsavelProperty, 150));
        tabela.getColumns().add(colunaCombo("Classificação NC", ItemChecklist::classificacaoProperty, 110,
                "Baixa", "Média", "Alta", "Urgente"));
        tabela.getColumns().add(colunaTexto("Ação corretiva indicada", ItemChecklist::acaoCorretivaProperty, 200));
        tabela.getColumns().add(colunaTexto("Data prevista resolução", ItemChecklist::dataPrevistaProperty, 130));
        tabela.getColumns().add(colunaTexto("Data/hora escalonamento", ItemChecklist::dataEscalonamentoProperty, 150));
        tabela.getColumns().add(colunaTexto("Data/hora conclusão NC", ItemChecklist::dataConclusaoProperty, 150));
        tabela.getColumns().add(colunaCombo("Status NC", ItemChecklist::statusProperty, 150,
                "Concluída", "Fechada por exceção"));

        // Itens iniciais de exemplo (baseados no checklist em PDF)
        itens.add(new ItemChecklist(1, "A Planilha de Customização de Processos foi desenvolvida?"));
        itens.add(new ItemChecklist(2, "Foi criado o repositório do projeto?"));
        itens.add(new ItemChecklist(3, "O Plano de Gerência de Configuração foi criado?"));
        itens.add(new ItemChecklist(4, "O Plano de Gerência de Configuração foi aprovado?"));
        itens.add(new ItemChecklist(5, "Os itens de configuração seguem o padrão de nomenclatura de arquivos?"));
        itens.add(new ItemChecklist(6, "O Plano de Garantia da Qualidade foi criado?"));

        // ---------- Botões ----------
        Button btnAdicionar = new Button("Adicionar item");
        btnAdicionar.setOnAction(e -> adicionarItem());

        Button btnRemover = new Button("Remover item selecionado");
        btnRemover.setOnAction(e -> removerItem());

        Button btnCalcular = new Button("Calcular aderência");
        btnCalcular.setOnAction(e -> calcular());

        HBox botoes = new HBox(10, btnAdicionar, btnRemover, btnCalcular);

        // ---------- Tabela pequena: NCs por classificação ----------
        GridPane gradeNC = criarGrade();
        gradeNC.addRow(0, negrito("Classificação"), negrito("Tempo para resolução"), negrito("Quantidade de NC"));
        gradeNC.addRow(1, new Label("Baixa"), new Label("10 min"), qtdBaixa);
        gradeNC.addRow(2, new Label("Média"), new Label("20 min"), qtdMedia);
        gradeNC.addRow(3, new Label("Alta"), new Label("30 min"), qtdAlta);
        gradeNC.addRow(4, new Label("Urgente"), new Label("40 min"), qtdUrgente);
        gradeNC.addRow(5, negrito("Total de NC"), new Label(""), qtdTotalNC);

        // ---------- Tabela pequena: taxa de aderência ----------
        GridPane gradeAderencia = criarGrade();
        gradeAderencia.addRow(0, negrito("Aderência da avaliação"), negrito("Valor"));
        gradeAderencia.addRow(1, new Label("Itens conformes (Sim)"), lblConformes);
        gradeAderencia.addRow(2, new Label("Itens não aplicáveis"), lblNaoAplicaveis);
        gradeAderencia.addRow(3, new Label("Itens avaliados (total - não aplicáveis)"), lblAvaliados);
        gradeAderencia.addRow(4, negrito("Taxa de aderência"), lblAderencia);

        HBox resumo = new HBox(40, gradeNC, gradeAderencia);

        VBox tela = new VBox(15, tabela, botoes, resumo);
        tela.setPadding(new Insets(15));
        return tela;
    }

    /** Cria uma coluna de texto editável ligada a um campo do ItemChecklist. */
    private TableColumn<ItemChecklist, String> colunaTexto(String titulo,
            Function<ItemChecklist, StringProperty> campo, double largura) {
        TableColumn<ItemChecklist, String> coluna = new TableColumn<>(titulo);
        coluna.setCellValueFactory(dados -> campo.apply(dados.getValue()));
        coluna.setCellFactory(TextFieldTableCell.forTableColumn());
        coluna.setPrefWidth(largura);
        return coluna;
    }

    /** Cria uma coluna com dropdown (ComboBox) ligada a um campo do ItemChecklist. */
    private TableColumn<ItemChecklist, String> colunaCombo(String titulo,
            Function<ItemChecklist, StringProperty> campo, double largura, String... opcoes) {
        TableColumn<ItemChecklist, String> coluna = new TableColumn<>(titulo);
        coluna.setCellValueFactory(dados -> campo.apply(dados.getValue()));
        coluna.setCellFactory(ComboBoxTableCell.forTableColumn(opcoes));
        coluna.setPrefWidth(largura);
        return coluna;
    }

    private GridPane criarGrade() {
        GridPane grade = new GridPane();
        grade.setHgap(25);
        grade.setVgap(6);
        grade.setPadding(new Insets(10));
        grade.setStyle("-fx-border-color: gray; -fx-border-width: 1;");
        return grade;
    }

    private Label negrito(String texto) {
        Label label = new Label(texto);
        label.setStyle("-fx-font-weight: bold;");
        return label;
    }

    /** Pergunta a descrição e adiciona uma nova linha no fim do checklist. */
    private void adicionarItem() {
        TextInputDialog dialogo = new TextInputDialog();
        dialogo.setTitle("Novo item");
        dialogo.setHeaderText("Descrição do item do checklist:");
        dialogo.showAndWait().ifPresent(descricao -> {
            itens.add(new ItemChecklist(itens.size() + 1, descricao));
        });
    }

    /** Remove a linha selecionada e renumera as demais. */
    private void removerItem() {
        ItemChecklist selecionado = tabela.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            itens.remove(selecionado);
            for (int i = 0; i < itens.size(); i++) {
                itens.get(i).setNumero(i + 1);
            }
        }
    }

    /**
     * Calcula a aderência e a quantidade de NCs por classificação.
     * Aderência = itens conformes / (total de itens - não aplicáveis)
     */
    private void calcular() {
        int conformes = 0;
        int naoAplicaveis = 0;
        int baixa = 0, media = 0, alta = 0, urgente = 0;

        for (ItemChecklist item : itens) {
            String resultado = item.getResultado();

            if (resultado.equals("Sim")) {
                conformes++;
            } else if (resultado.equals("Não se aplica")) {
                naoAplicaveis++;
            } else if (resultado.equals("Não")) {
                // É uma NC: conta pela classificação escolhida
                String classificacao = item.getClassificacao();
                if (classificacao.equals("Baixa")) baixa++;
                else if (classificacao.equals("Média")) media++;
                else if (classificacao.equals("Alta")) alta++;
                else if (classificacao.equals("Urgente")) urgente++;
            }
        }

        int avaliados = itens.size() - naoAplicaveis;
        double aderencia = 0;
        if (avaliados > 0) {
            aderencia = (conformes * 100.0) / avaliados;
        }

        qtdBaixa.setText(String.valueOf(baixa));
        qtdMedia.setText(String.valueOf(media));
        qtdAlta.setText(String.valueOf(alta));
        qtdUrgente.setText(String.valueOf(urgente));
        qtdTotalNC.setText(String.valueOf(baixa + media + alta + urgente));

        lblConformes.setText(String.valueOf(conformes));
        lblNaoAplicaveis.setText(String.valueOf(naoAplicaveis));
        lblAvaliados.setText(String.valueOf(avaliados));
        lblAderencia.setText(String.format("%.1f%%", aderencia));
    }
}
