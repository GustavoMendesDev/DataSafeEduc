package school.sptech;


import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.InputStream;



public class S3Downloader {
    private final S3Client s3;
    private final String bucket;

    public S3Downloader(String bucket) {
        this.bucket = bucket;
        this.s3 = S3Client.builder()
                .region(Region.SA_EAST_1) // região da sua EC2/bucket
                .build();
    }

    public InputStream baixarArquivo(String chave) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(chave)
                .build();

        System.out.println("[] - (S3Downloader) - Baixando arquivo: " + chave);
        return s3.getObject(request);
    }

}
