package ui;

import model.linkRedaction.LinkRedaction;
import model.ShortLinkInfo;
import model.linkRedaction.LinkDeletion;
import model.linkRedaction.LinkEdit;

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

            switch (input) {
                case "1" -> {
                    System.out.print("Введите новый лимит: ");
                    int newLimit = Integer.parseInt(scanner.nextLine());
                    if (newLimit < 1) {
                        System.out.println("Ошибка: Лимит должен быть больше 0");
                        continue;
                    }
                    return new LinkEdit(newLimit);
                }
                case "2" -> {
                    return new LinkDeletion();
                }
                case "0" -> {
                    return null;
                }
            }

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
        System.out.println();
        System.out.print("> ");
    }
}
