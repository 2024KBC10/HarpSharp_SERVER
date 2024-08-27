package com.harpsharp.album.application.inputport;

import com.harpsharp.album.application.outputport.ProfileImageOutputPort;
import com.harpsharp.album.application.usecase.UploadProfileImageUsecase;
import com.harpsharp.infra_rds.dto.image.RequestProfileImageDTO;
import com.harpsharp.infra_rds.dto.image.ResponseProfileImageDTO;
import com.harpsharp.infra_rds.entity.album.ProfileImage;
import com.harpsharp.infra_rds.mapper.ImageMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileImageInputPort implements UploadProfileImageUsecase {
    private final ProfileImageOutputPort profileImageOutputPort;
    private final ImageMapper imageMapper;

    @Value("${spring.cloud.aws.cdn.domain}")
    private String domain;


    @Override
    public ResponseProfileImageDTO confirmProfileUrl(RequestProfileImageDTO requestProfileImageDTO) throws MalformedURLException {
        String uuid = requestProfileImageDTO.uuid();

        String profileKey = String.format("profile/%s", uuid);
        URL staticUrl = new URL(String.format("https://%s.cloudfront.net/%s", domain, profileKey));

        ProfileImage profileImage = ProfileImage
                .builder()
                .url(staticUrl.toString())
                .uuid(uuid)
                .build();
        
        ProfileImage profile = profileImageOutputPort.save(profileImage);

        System.out.println("profile = " + profile);
        return imageMapper.profileToResponseDTO(profile);
    }
}
