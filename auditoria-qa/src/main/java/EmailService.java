/**package br.pucpr.auditoria;*/

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Envia e-mails reais usando a API REST do Brevo (https://www.brevo.com).
 * Passos para configurar:
 * 1) Criar uma conta gratuita no Brevo
 * 2) Verificar o e-mail remetente (Senders & IP > Senders)
 * 3) Gerar uma chave em SMTP & API > API Keys
 * 4) Colar a chave e o e-mail verificado nas constantes abaixo
 */
public class EmailService {

    private static final String API_KEY = "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";
    private static final String EMAIL_REMETENTE = "solicitacaonc@gmail.com";
    private static final String URL_API = "https://api.brevo.com/v3/smtp/email";

    /** Retorna true se a API aceitou o envio (HTTP 201). */
    public static boolean enviar(String destinatario, String assunto, String corpo) throws Exception {
        // Monta o JSON esperado pela API do Brevo
        String json = "{"
                + "\"sender\": {\"name\": \"Auditoria QA\", \"email\": \"" + EMAIL_REMETENTE + "\"},"
                + "\"to\": [{\"email\": \"" + destinatario + "\"}],"
                + "\"subject\": \"" + escapar(assunto) + "\","
                + "\"textContent\": \"" + escapar(corpo) + "\""
                + "}";

        HttpRequest requisicao = HttpRequest.newBuilder()
                .uri(URI.create(URL_API))
                .header("api-key", API_KEY)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> resposta = HttpClient.newHttpClient()
                .send(requisicao, HttpResponse.BodyHandlers.ofString());

        // Imprime a resposta no console para ajudar a depurar
        System.out.println("Status: " + resposta.statusCode() + " - " + resposta.body());

        return resposta.statusCode() == 201;
    }

    /** Escapa caracteres que quebrariam o JSON (aspas, barras e quebras de linha). */
    private static String escapar(String texto) {
        if (texto == null) return "";
        return texto.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\r", "")
                    .replace("\n", "\\n");
    }
}
