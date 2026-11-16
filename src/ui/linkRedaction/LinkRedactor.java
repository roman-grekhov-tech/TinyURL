package ui.linkRedaction;

import model.ShortLinkInfo;

import java.util.Scanner;

public class LinkRedactor {
    private final ShortLinkInfo info;
    private final Scanner scanner;

    public LinkRedactor(ShortLinkInfo info, Scanner scanner) {
        this.info = info;
        this.scanner = scanner;
    }

    public LinkRedaction start() {
        if (info == null) {
            System.out.println("Ссылка не найдена.");
            return null;
        }

        while (true) {
            printMainMenu();
            String input = scanner.nextLine();

            if (input.equals("1")) {
                System.out.print("Введите новый лимит: ");
                int newLimit = Integer.parseInt(scanner.nextLine());
                return new LinkEdit(newLimit);
            }

            if (input.equals("2")) {
                return new LinkDeletion();
            }

            if (input.equals("0")) return null;
        }

    }

    private void printMainMenu() {
        System.out.println("Оригинальная ссылка: " + info.getOriginalLink());
        System.out.println("Лимит: " + info.getLimit());
        System.out.println("Использовано раз: " + info.getTimesUsed());
        System.out.println("Истекает: " + info.getExpirationDate());
        System.out.println("------------------------------");
        System.out.println();

        System.out.println("1. Изменить лимит ссылки");
        System.out.println("2. Удалить ссылку");
        System.out.println();
        System.out.println("0. Выйти");
    }
}
