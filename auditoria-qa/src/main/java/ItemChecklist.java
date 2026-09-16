/**package br.pucpr.auditoria;*/

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Representa uma linha do checklist.
 * Cada campo é uma StringProperty para que a TableView consiga
 * exibir o valor e salvar automaticamente o que o usuário editar.
 */
public class ItemChecklist {

    private final StringProperty numero = new SimpleStringProperty("");
    private final StringProperty descricao = new SimpleStringProperty("");
    private final StringProperty resultado = new SimpleStringProperty("");
    private final StringProperty dataIdentificacao = new SimpleStringProperty("");
    private final StringProperty responsavel = new SimpleStringProperty("");
    private final StringProperty classificacao = new SimpleStringProperty("");
    private final StringProperty acaoCorretiva = new SimpleStringProperty("");
    private final StringProperty dataPrevista = new SimpleStringProperty("");
    private final StringProperty dataEscalonamento = new SimpleStringProperty("");
    private final StringProperty dataConclusao = new SimpleStringProperty("");
    private final StringProperty status = new SimpleStringProperty("");

    public ItemChecklist(int numero, String descricao) {
        this.numero.set(String.valueOf(numero));
        this.descricao.set(descricao);
    }

    // Métodos "property" usados pelas colunas da tabela
    public StringProperty numeroProperty() { return numero; }
    public StringProperty descricaoProperty() { return descricao; }
    public StringProperty resultadoProperty() { return resultado; }
    public StringProperty dataIdentificacaoProperty() { return dataIdentificacao; }
    public StringProperty responsavelProperty() { return responsavel; }
    public StringProperty classificacaoProperty() { return classificacao; }
    public StringProperty acaoCorretivaProperty() { return acaoCorretiva; }
    public StringProperty dataPrevistaProperty() { return dataPrevista; }
    public StringProperty dataEscalonamentoProperty() { return dataEscalonamento; }
    public StringProperty dataConclusaoProperty() { return dataConclusao; }
    public StringProperty statusProperty() { return status; }

    // Getters simples usados no cálculo da aderência
    public String getResultado() { return resultado.get(); }
    public String getClassificacao() { return classificacao.get(); }

    public void setNumero(int numero) { this.numero.set(String.valueOf(numero)); }
}
