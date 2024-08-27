package com.harpsharp.album.application.inputport;

import com.harpsharp.album.application.usecase.ProfilePresignedUrlUsecase;
import com.harpsharp.infra_rds.dto.image.ImageType;
import com.harpsharp.infra_rds.dto.image.InProfilePresignedUrlDTO;
import com.harpsharp.infra_rds.dto.image.ResponseProfilePresignedUrlDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.time.Duration;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProfilePresignedUrlInputPort implements ProfilePresignedUrlUsecase {

    private final S3Client awsS3Client;
    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;
    @Value("${spring.cloud.aws.cdn.domain}")
    private String domain;

    private final S3Presigner s3Presigner;

    @Override
    public void deletePresignedUrl(ImageType type, String uuid) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest
                .builder()
                .bucket(bucketName)
                .key(type.toString() + "/" + uuid)
                .build();

        awsS3Client.deleteObject(deleteObjectRequest);
    }

    @Override
    public ResponseProfilePresignedUrlDTO putPresignedUrl(InProfilePresignedUrlDTO presignedUrlDTO) {
        String extension = FilenameUtils.getExtension(presignedUrlDTO.filename());

        if(!extension.equals("png") && !extension.equals("jpeg") && !extension.equals("jpg") && !extension.equals("gif"))
            throw new IllegalArgumentException("INVALID_EXTENSION");

        String uuid = UUID.randomUUID() + "." + extension;
        String key  = String.format("profile/%s", uuid);

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        PresignedPutObjectRequest presignedRequest =
                s3Presigner.presignPutObject(r -> r
                        .putObjectRequest(objectRequest)
                        .signatureDuration(Duration.ofMinutes(10)));  

        return new ResponseProfilePresignedUrlDTO(presignedRequest.url(), uuid);
    }
}
