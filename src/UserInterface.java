import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

public class UserInterface {
    private final App app;

    public UserInterface(App app) {
        this.app = app;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMainMenu();
            String input = scanner.nextLine();

            if (input.equals("1")) {
                System.out.println("Введите ссылку, для которой нужно создать короткую ссылку: ");
                String originalLink = scanner.nextLine();
                String shortLink = app.addShortLunk(originalLink);
                System.out.println("Короткая ссылка: " + shortLink);
            }

            if (input.equals("2")) {
                System.out.println("Введите короткую ссылку, по которой нужно перейти: ");
                String shortLink = scanner.nextLine();
                ShortLinkUseResult result = app.useShortLink(shortLink);
                handleShortLinkUseResult(result);
            }

            if (input.equals("0")) {
                System.out.println("Приложение завершает работу...");
                break;
            }
        }

        scanner.close();
    }

    private void printMainMenu() {
        System.out.println();
        System.out.println("1. Создать короткую ссылку");
        System.out.println("2. Перейти по короткой ссылке");
        System.out.println("-----------------------------");
        System.out.println("0. Завершить работу");
        System.out.println();
        System.out.print("> ");
    }

    private void openOriginalLink(String originalLink) {
        try {
            Desktop.getDesktop().browse(new URI(originalLink));
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleShortLinkUseResult(ShortLinkUseResult result) {
        switch (result.getResult()) {
            case SUCCESS -> {
                System.out.println("Ссылка найдена, открываем...");
                openOriginalLink(result.getOriginalLink());
            }
            case NOT_FOUND -> System.out.println("Ссылка не найдена");
            case LIMIT_REACHED -> System.out.println("Лимит использования ссылки исчерпан");
            case EXPIRED -> System.out.println("Время действия ссылки истекло");
        }
    }
}
