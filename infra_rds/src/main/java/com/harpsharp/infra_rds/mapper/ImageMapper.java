package com.harpsharp.infra_rds.mapper;

import com.harpsharp.infra_rds.dto.image.RequestProfileImageDTO;
import com.harpsharp.infra_rds.dto.image.ResponseProfileImageDTO;
import com.harpsharp.infra_rds.entity.album.ProfileImage;
import com.harpsharp.infra_rds.entity.album.ProfileUUID;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ImageMapper {
    public ResponseProfileImageDTO profileToResponseDTO(ProfileImage profileImage){
        return new ResponseProfileImageDTO(
                profileImage.getUrl(),
                profileImage.getUuid(),
                profileImage.getType(),
                profileImage.getCreatedAt(),
                profileImage.getUpdatedAt());
    }

    public ProfileImage responseProfileImageToEntity(ResponseProfileImageDTO responseProfileImageDTO){
        return ProfileImage
                .builder()
                .url(responseProfileImageDTO.url())
                .uuid(responseProfileImageDTO.uuid())
                .build();
    }

    public List<ResponseProfileImageDTO> profilestoList(List<ProfileImage> profileImages) {
        return profileImages.stream()
                .map(this::profileToResponseDTO)
                .collect(Collectors.toList());
    }

    public Map<Long, ResponseProfileImageDTO> profilesToMap(List<ProfileImage> profiles) {
        return profiles.stream()
                .collect(Collectors.toMap(ProfileImage::getProfileId, this::profileToResponseDTO));
    }

}

