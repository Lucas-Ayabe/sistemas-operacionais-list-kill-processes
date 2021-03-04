package view;

import controller.ProcessController;
import java.util.Scanner;
import services.ConsoleReader;

public class Main {

    public static void main(String[] args) {
        var reader = new ConsoleReader(new Scanner(System.in));
        var controller = new ProcessController();
        var operationalSystem = System.getProperty("os.name");
        var option = 0;
        var optionMessage =
            "Menu\n0 - Sair;\n1 - listar;\n2 - matar (nome ou pid)\nDigite aqui ==> ";

        do {
            option = reader.askToInt(optionMessage);
            System.out.println("\n========\n");
            switch (option) {
                case 0:
                    System.out.println("Encerrando aplicação...");
                    break;
                case 1:
                    controller.list(operationalSystem);
                    break;
                case 2:
                    var answer = reader.ask("Nome do processo ou PID ===> ");
                    var isPid = answer.matches("[0-9]*");

                    if (isPid) {
                        controller.kill(
                            operationalSystem,
                            Integer.parseInt(answer)
                        );
                    } else {
                        controller.kill(operationalSystem, answer);
                    }
                    break;
                default:
                    System.out.println("Opção inválida, tente novamente :(");
                    break;
            }
            System.out.println("\n========\n");
        } while (option != 0);
    }
}
