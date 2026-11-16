package ui;

import model.App;
import model.ShortLinkInfo;
import model.ShortLinkUseResult;
import ui.linkRedaction.LinkRedaction;
import ui.linkRedaction.LinkRedactor;

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

            if (input.equals("3")) {
                System.out.print("Введите короткую ссылку для настройки: ");
                String shortLink = scanner.nextLine();
                ShortLinkInfo info = app.getShortLinkInfo(shortLink);

                if (info == null) {
                    System.out.println("Неверный пользователь");
                    continue;
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

            if (input.equals("4")) {
                String newUsersString = scanner.nextLine();
                UUID newUser = UUID.fromString(newUsersString);
                app.changeUSer(newUser);
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

    private void printCurrentUser(UUID user) {
        System.out.print("\t\t");
        if (user == null) {
            System.out.println("Гость");
        } else {
            System.out.println(user);
        }
    }
}
