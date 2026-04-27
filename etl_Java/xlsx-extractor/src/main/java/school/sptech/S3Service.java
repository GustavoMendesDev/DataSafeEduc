package school.sptech;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.InputStream;

public class S3Service {

    private static final S3Client s3 = S3Client.create();

    public static InputStream getArquivo(String nomeArquivo){

        GetObjectRequest request = GetObjectRequest.builder()
                .bucket("data-safe-s3")
                .key(nomeArquivo)
                .build();

        return s3.getObject(request);
    }
}