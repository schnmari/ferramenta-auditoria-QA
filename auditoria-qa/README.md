# Automação de Auditoria de QA

Trabalho da disciplina de Qualidade de Software (PUCPR) referente ao RA1
Aplicação desktop em JavaFX que automatiza uma auditoria de Garantia da Qualidade em duas partes:

**1. Checklist de auditoria**
Tabela editável com numeração, descrição, resultado (Sim / Não / Não se aplica), data e hora da identificação da NC, responsável pela resolução, classificação da NC (Baixa / Média / Alta / Urgente), ação corretiva, data prevista de resolução, data e hora do escalonamento, data e hora da conclusão e status (Concluída / Fechada por exceção). 
Abaixo, um resumo com o total de NCs por classificação e seu tempo de resolução (10, 20, 30 e 40 min) e a taxa de aderência, calculada como `itens conformes / (total de itens − não aplicáveis)`.

**2. Comunicação de Não Conformidade**
Formulário de Solicitação de Resolução de NC que, ao ser enviado, dispara um e-mail real ao destinatário pela API REST do Brevo.

## Como rodar
Requer JDK 17+ e Maven.
```
mvn javafx:run
```
Para o envio de e-mail, preencha `API_KEY` e `EMAIL_REMETENTE` em `EmailService.java` com uma chave e um remetente verificado no Brevo.

## Arquivos
- `Main.java` — janela com as duas abas
- `ItemChecklist.java` — modelo de uma linha do checklist
- `TelaChecklist.java` — checklist, resumo de NCs e cálculo de aderência
- `TelaComunicacaoNC.java` — formulário de comunicação de NC
- `EmailService.java` — envio de e-mail via API
 
