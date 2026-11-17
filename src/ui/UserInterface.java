package ui;

import model.AddShortLinkResult;
import model.App;
import model.ShortLinkInfo;
import model.ShortLinkUseResult;
import model.linkRedaction.LinkRedaction;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.UUID;

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
                startAddShortLink(scanner);
            }

            if (input.equals("2")) {
                startUseShortLink(scanner);
            }

            if (input.equals("3")) {
                startEditShortLink(scanner);
            }

            if (input.equals("4")) {
                startChangeUser(scanner);
            }

            if (input.equals("0")) {
                System.out.println("Приложение завершает работу...");
                app.shutdown();
                break;
            }
        }

        scanner.close();
    }

    private void printMainMenu() {
        System.out.println();
        printCurrentUser(app.getUser());
        System.out.println();
        System.out.println("1. Создать короткую ссылку");
        System.out.println("2. Перейти по короткой ссылке");
        System.out.println("3. Настройки короткой ссылки");
        System.out.println("4. Сменить пользователя");
        System.out.println("-----------------------------");
        System.out.println("0. Завершить работу");
        System.out.println();
        System.out.print("> ");
    }

    private void startAddShortLink(Scanner scanner) {
        System.out.println("Введите ссылку, для которой нужно создать короткую ссылку: ");
        System.out.println("Пример: https://primer.ru");
        String originalLink = scanner.nextLine();
        AddShortLinkResult result = app.addShortLunk(originalLink);
        handleAddShortLinkResult(result);
    }

    private void startUseShortLink(Scanner scanner) {
        System.out.println("Введите короткую ссылку, по которой нужно перейти: ");
        String shortLink = scanner.nextLine();
        ShortLinkUseResult result = app.useShortLink(shortLink);
        handleShortLinkUseResult(result);
    }

    private void startEditShortLink(Scanner scanner) {
        System.out.print("Введите короткую ссылку для настройки: ");
        String shortLink = scanner.nextLine();
        ShortLinkInfo info = app.getShortLinkInfo(shortLink);

        if (info == null) {
            System.out.println("Неверный пользователь");
            return;
        }

        LinkRedactor redactor = new LinkRedactor(info, scanner);
        LinkRedaction redaction = redactor.start();

        if (redaction != null) {
            if (redaction.getStatus() == LinkRedaction.Status.DELETE) {
                app.deleteShortLink(shortLink);
            }
            if (redaction.getStatus() == LinkRedaction.Status.EDIT) {
                app.editLink(redaction, shortLink);
            }
        }
    }

    private void startChangeUser(Scanner scanner) {
        String newUsersString = scanner.nextLine();
        try {
            UUID newUser = UUID.fromString(newUsersString);
            app.changeUSer(newUser);
        } catch (IllegalArgumentException e) {
            System.out.println("Необходим корректный формат UUID");
        }
    }

    private void handleAddShortLinkResult(AddShortLinkResult result) {
        switch (result.getStatus()) {
            case SUCCESS -> System.out.println("Короткая ссылка: " + result.getShortLink());
            case ORIGINAL_LINK_IS_INVALID -> System.out.println("Проверьте валидность ссылки");
            case FAILED -> System.out.println("При генерации ссылки произошла ошибка. " +
                    "Пожалуйста, попробуйте еще раз.");
        }
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

    private void printCurrentUser(UUID user) {
        System.out.print("\t\t");
        if (user == null) {
            System.out.println("Гость");
        } else {
            System.out.println(user);
        }
    }
}
