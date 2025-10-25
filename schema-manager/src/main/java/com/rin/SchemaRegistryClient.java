package com.rin;


import com.google.gson.JsonObject;
import okhttp3.*;
import java.io.IOException;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SchemaRegistryClient {
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    private final String baseUrl;

    public SchemaRegistryClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public List<String> listSubjects() {
        Request request = new Request.Builder()
                .url(baseUrl + "/subjects")
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            String body = response.body().string();
            if (response.isSuccessful()) {
                return gson.fromJson(body, new TypeToken<List<String>>(){}.getType());
            } else {
                System.err.println("❌ Lỗi: " + body);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return List.of();
    }

    public void registerSchema(String subject, String jsonSchema) {
        JsonObject payload = new JsonObject();
        payload.addProperty("schemaType", "JSON");
        payload.addProperty("schema", jsonSchema);

        Request request = new Request.Builder()
                .url(baseUrl + "/subjects/" + subject + "/versions")
                .post(RequestBody.create(payload.toString(), MediaType.get("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println("📤 Kết quả đăng ký: " + response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getLatestSchema(String subject) {
        Request request = new Request.Builder()
                .url(baseUrl + "/subjects/" + subject + "/versions/latest")
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println("📥 Schema mới nhất:");
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getSchemaByVersion(String subject, int version) {
        Request request = new Request.Builder()
                .url(baseUrl + "/subjects/" + subject + "/versions/" + version)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println("📥 Schema version " + version + ":");
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteSubject(String subject) {
        Request request = new Request.Builder()
                .url(baseUrl + "/subjects/" + subject)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println("🗑️ Kết quả xóa: " + response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
