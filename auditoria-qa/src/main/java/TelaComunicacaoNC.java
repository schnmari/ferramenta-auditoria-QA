/**package br.pucpr.auditoria;*/

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * Formulário de "Solicitação de Resolução de Não Conformidade".
 * Ao clicar em Enviar, monta o texto com os dados preenchidos
 * e envia um e-mail real para o destinatário usando a EmailService.
 */
public class TelaComunicacaoNC {

    private final TextField projeto = new TextField();
    private final TextField responsavelResolucao = new TextField();
    private final TextField dataSolicitacao = new TextField();
    private final TextField prazoResolucao = new TextField();
    private final TextField numeroEscalonamento = new TextField();
    private final TextField responsavelQA = new TextField();
    private final TextArea descricao = new TextArea();
    private final ComboBox<String> classificacao = new ComboBox<>();
    private final TextArea acaoCorretiva = new TextArea();
    private final TextField historicoEscalonamento = new TextField();
    private final TextField superiorResponsavel = new TextField();
    private final TextField prazoEscalonamento = new TextField();
    private final TextArea observacoes = new TextArea();
    private final TextField emailDestinatario = new TextField();

    public ScrollPane criar() {
        classificacao.getItems().addAll("Baixa", "Média", "Alta", "Urgente");
        descricao.setPrefRowCount(3);
        acaoCorretiva.setPrefRowCount(3);
        observacoes.setPrefRowCount(3);

        GridPane grade = new GridPane();
        grade.setHgap(10);
        grade.setVgap(8);
        grade.setPadding(new Insets(15));

        Label titulo = new Label("Solicitação de Resolução de Não Conformidade");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        grade.add(titulo, 0, 0, 2, 1);

        // Cada linha: label na coluna 0 e campo na coluna 1
        grade.addRow(1, new Label("Projeto:"), projeto);
        grade.addRow(2, new Label("Responsável pela Resolução:"), responsavelResolucao);
        grade.addRow(3, new Label("Data da 1ª Solicitação:"), dataSolicitacao);
        grade.addRow(4, new Label("Prazo de Resolução:"), prazoResolucao);
        grade.addRow(5, new Label("Nº de Escalonamento:"), numeroEscalonamento);
        grade.addRow(6, new Label("Responsável por QA:"), responsavelQA);
        grade.addRow(7, new Label("Descrição:"), descricao);
        grade.addRow(8, new Label("Classificação:"), classificacao);
        grade.addRow(9, new Label("Ação Corretiva Indicada:"), acaoCorretiva);
        grade.addRow(10, new Label("Histórico de Escalonamento:"), historicoEscalonamento);
        grade.addRow(11, new Label("Superior Responsável:"), superiorResponsavel);
        grade.addRow(12, new Label("Prazo para Resolução (escalonamento):"), prazoEscalonamento);
        grade.addRow(13, new Label("Observações:"), observacoes);
        grade.addRow(14, new Label("E-mail do destinatário:"), emailDestinatario);

        Button btnEnviar = new Button("Enviar solicitação por e-mail");
        btnEnviar.setOnAction(e -> enviar());
        grade.add(btnEnviar, 1, 15);

        ScrollPane rolagem = new ScrollPane(grade);
        rolagem.setFitToWidth(true);
        return rolagem;
    }

    /** Monta o corpo do e-mail e envia. Mostra um alerta com o resultado. */
    private void enviar() {
        if (emailDestinatario.getText().isBlank()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Informe o e-mail do destinatário.");
            return;
        }

        String assunto = "Solicitação de Resolução de NC - " + projeto.getText();

        String corpo = "SOLICITAÇÃO DE RESOLUÇÃO DE NÃO CONFORMIDADE\n\n"
                + "Projeto: " + projeto.getText() + "\n"
                + "Responsável pela Resolução: " + responsavelResolucao.getText() + "\n"
                + "Data da 1ª Solicitação: " + dataSolicitacao.getText() + "\n"
                + "Prazo de Resolução: " + prazoResolucao.getText() + "\n"
                + "Nº de Escalonamento: " + numeroEscalonamento.getText() + "\n"
                + "Responsável por QA: " + responsavelQA.getText() + "\n\n"
                + "Descrição:\n" + descricao.getText() + "\n\n"
                + "Classificação: " + classificacao.getValue() + "\n\n"
                + "Ação Corretiva Indicada:\n" + acaoCorretiva.getText() + "\n\n"
                + "Histórico de Escalonamento: " + historicoEscalonamento.getText() + "\n"
                + "Superior Responsável: " + superiorResponsavel.getText() + "\n"
                + "Prazo para Resolução: " + prazoEscalonamento.getText() + "\n\n"
                + "Observações:\n" + observacoes.getText();

        try {
            boolean enviado = EmailService.enviar(emailDestinatario.getText(), assunto, corpo);
            if (enviado) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "E-mail enviado com sucesso!");
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "A API recusou o envio. Verifique a chave e o remetente.");
            }
        } catch (Exception ex) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro ao enviar: " + ex.getMessage());
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String mensagem) {
        Alert alerta = new Alert(tipo, mensagem);
        alerta.setHeaderText(null);
        alerta.showAndWait();
    }
}
