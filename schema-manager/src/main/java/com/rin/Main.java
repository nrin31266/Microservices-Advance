package com.rin;


import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MenuHandler menu = new MenuHandler();
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n===== Schema Registry CLI =====");
            System.out.println("1. Liệt kê tất cả subjects");
            System.out.println("2. Đăng ký schema mới từ file JSON");
            System.out.println("3. Xem schema mới nhất của subject");
            System.out.println("4. Xem schema theo version");
            System.out.println("5. Xóa subject");
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> menu.listSubjects();
                case 2 -> menu.registerSchema();
                case 3 -> menu.getLatestSchema();
                case 4 -> menu.getSchemaByVersion();
                case 5 -> menu.deleteSubject();
                case 0 -> System.out.println("Tạm biệt!");
                default -> System.out.println("Lựa chọn không hợp lệ!");
            }
        } while (choice != 0);

        scanner.close();
    }
}
