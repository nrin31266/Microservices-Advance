package com.rin;


import java.nio.file.Path;
import java.util.Scanner;

public class MenuHandler {
    private final SchemaRegistryClient registryClient = new SchemaRegistryClient("http://localhost:9081");
    private final Scanner scanner = new Scanner(System.in);

    public void listSubjects() {
        System.out.println("📜 Danh sách subjects:");
        registryClient.listSubjects().forEach(System.out::println);
    }

    public void registerSchema() {
        System.out.print("Nhập tên subject: ");
        String subject = scanner.nextLine();
        System.out.print("Nhập đường dẫn file JSON (ví dụ: schemas/product-v1.json): ");
        String filePath = scanner.nextLine();

        String json = JsonFileUtils.readJsonFile(Path.of(filePath));
        registryClient.registerSchema(subject, json);
    }

    public void getLatestSchema() {
        System.out.print("Nhập tên subject: ");
        String subject = scanner.nextLine();
        registryClient.getLatestSchema(subject);
    }

    public void getSchemaByVersion() {
        System.out.print("Nhập tên subject: ");
        String subject = scanner.nextLine();
        System.out.print("Nhập version: ");
        int version = Integer.parseInt(scanner.nextLine());
        registryClient.getSchemaByVersion(subject, version);
    }

    public void deleteSubject() {
        System.out.print("Nhập tên subject cần xóa: ");
        String subject = scanner.nextLine();
        registryClient.deleteSubject(subject);
    }
}
