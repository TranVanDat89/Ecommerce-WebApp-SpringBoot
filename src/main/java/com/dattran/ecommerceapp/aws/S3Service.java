package com.dattran.ecommerceapp.aws;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class S3Service {
    final S3Client s3Client;
    @Value("${aws.s3.bucket-name}")
    String bucketName;

    public String uploadFile(MultipartFile file, String folder)  {
        String fileName = UUID.randomUUID() + "_" +Paths.get(file.getOriginalFilename()).getFileName().toString();
        String keyName = folder + fileName;
        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();

            s3Client.putObject(request, software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));
            return generateUrl(keyName);
        } catch (S3Exception | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while uploading file to S3 ", e);
        }
    }
    public String generateUrl(String objectKey) {
        try {
            GetUrlRequest request = GetUrlRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();
            return s3Client.utilities().getUrl(request).toString();
        } catch (S3Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot generate url of image ", e);
        }
    }
}
