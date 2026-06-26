package com.ironqueue.executor;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironqueue.job.Job;
import com.ironqueue.util.Logger;
import com.ironqueue.util.PdfRest;

public class UploadJobExecutor implements JobExecutor{
    @Override
    public void execute(Job job) throws Exception {
        Map<String,Object> payload = job.getPayload();
        String fileName = (String)payload.get("arg2");
        
        //Actual work should be done here
        String apiKey = System.getenv("PDF_REST_API");

        if(apiKey == null || apiKey.isBlank()) {
            Logger.LogError("PDF_REST_API not found. \nPlease set it in the environment variables using \nexport PDF_REST_API=key");
            throw new IllegalStateException("PDF_REST_API environment variable not configured");
        }
        String url = "https://api.pdfrest.com/upload";
        String resId = uploadFile(fileName, url, apiKey);
        Logger.LogOutput(PdfRest.retrieveResource(resId, apiKey));
    }
    public static String uploadFile(String filePathString, String url, String apiKey) throws IOException, InterruptedException {
        Path filePath = Paths.get(filePathString);
        String fileName = filePath.getFileName().toString();
        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        // 3. Generate a unique boundary string for multipart/form-data
        String boundary = "Boundary-" + UUID.randomUUID().toString();

        // 4. Build the multipart byte payload
        byte[] multipartBody = createMultipartBody(filePath, fileName, contentType, boundary);

        // 5. Initialize HttpClient and send the POST request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .header("Api-Key", apiKey)
                .POST(HttpRequest.BodyPublishers.ofByteArray(multipartBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());

        JsonNode firstFile = root.path("files").path(0);
        if (firstFile.isMissingNode() || firstFile.isNull()) {
            Logger.LogError("No response returned from PDFRest");
            throw new IllegalStateException("No response returned from PDFRest");
        }
        return firstFile.path("id").asText();
    }

    private static byte[] createMultipartBody(Path filePath, String fileName, String contentType, String boundary) throws IOException {
        var byteArrays = new ArrayList<byte[]>();
        
        // Form field header metadata
        String header = "--" + boundary + "\r\n" +
                "Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"\r\n" +
                "Content-Type: " + contentType + "\r\n\r\n";
        
        byteArrays.add(header.getBytes());
        
        // Actual file payload bytes
        byteArrays.add(Files.readAllBytes(filePath));
        
        // Closing boundary indicator
        String footer = "\r\n--" + boundary + "--\r\n";
        byteArrays.add(footer.getBytes());

        // Flatten all chunks into a final byte array
        int totalLength = byteArrays.stream().mapToInt(arr -> arr.length).sum();
        byte[] result = new byte[totalLength];
        int currentIndex = 0;
        for (byte[] arr : byteArrays) {
            System.arraycopy(arr, 0, result, currentIndex, arr.length);
            currentIndex += arr.length;
        }
        
        return result;
    }
}