
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

/**
 * Classe principal. Cria a janela com duas abas:
 * 1) Checklist de auditoria
 * 2) Comunicação de Não Conformidade (formulário que envia e-mail)
 */
public class Main extends Application {

    @Override
    public void start(Stage janela) {
        TabPane abas = new TabPane();

        Tab abaChecklist = new Tab("Checklist de Auditoria", new TelaChecklist().criar());
        Tab abaNC = new Tab("Comunicação de NC", new TelaComunicacaoNC().criar());

        // Impede que o usuário feche as abas
        abaChecklist.setClosable(false);
        abaNC.setClosable(false);

        abas.getTabs().addAll(abaChecklist, abaNC);

        janela.setTitle("Automação de Auditoria de QA");
        janela.setScene(new Scene(abas, 1400, 750));
        janela.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
