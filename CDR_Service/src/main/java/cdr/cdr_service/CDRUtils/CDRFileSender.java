package cdr.cdr_service.CDRUtils;

import okhttp3.*;

import java.io.File;
import java.io.IOException;


public class CDRFileSender {
    private static final String DESTINATION_URL = "http://localhost:2002/catchCDR";
    public void sendFile(File file) {
        OkHttpClient client = new OkHttpClient();
        String fileName = file.toString().substring(file.toString().lastIndexOf("/") + 1);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(MediaType.parse("text/plain"), file))
                .addFormDataPart("fileName", fileName)
                .build();

        Request request = new Request.Builder()
                .url(DESTINATION_URL)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.out.println("error during sending");
            }
            System.out.println(response.body().string() + "\n" + file);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
