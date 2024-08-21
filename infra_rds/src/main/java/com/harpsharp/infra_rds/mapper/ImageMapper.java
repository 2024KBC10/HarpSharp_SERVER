package com.harpsharp.infra_rds.mapper;

import com.harpsharp.infra_rds.dto.image.ResponseGeneratedImageDTO;
import com.harpsharp.infra_rds.dto.image.ResponsePostImageDTO;
import com.harpsharp.infra_rds.dto.image.ResponseProfileImageDTO;
import com.harpsharp.infra_rds.entity.Image;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ImageMapper {
    public ResponseProfileImageDTO profileToDTO(Image image){
        return new ResponseProfileImageDTO(
                image.getPresignedURL(),
                image.getOwner().getUsername(),
                image.getType(),
                image.getCreatedAt(),
                image.getUpdatedAt());
    }

    public ResponsePostImageDTO postImageToDTO(Image image){
        return new ResponsePostImageDTO(
                image.getPresignedURL(),
                image.getOwner().getUsername(),
                image.getPost().getPostId(),
                image.getType(),
                image.getCreatedAt(),
                image.getUpdatedAt());
    }

    public ResponseGeneratedImageDTO generatedImageToDTO(Image image){
        return new ResponseGeneratedImageDTO(
                image.getPresignedURL(),
                image.getOwner().getUsername(),
                image.getModel(),
                image.getPrompt(),
                image.getType(),
                image.getCreatedAt(),
                image.getUpdatedAt());
    }


    public List<ResponsePostImageDTO> postImagestoList(List<Image> postImages) {
        return postImages.stream() // 스트림 시작
                .map(this::postImageToDTO) // 각 Post 객체에 postToDTO 함수 적용
                .collect(Collectors.toList());
    }
    public List<ResponseProfileImageDTO> profilestoList(List<Image> profileImages) {
        return profileImages.stream() // 스트림 시작
                .map(this::profileToDTO) // 각 Post 객체에 postToDTO 함수 적용
                .collect(Collectors.toList());
    }

    public List<ResponseGeneratedImageDTO> generatedImagesToList(List<Image> generatedImages) {
        return generatedImages.stream() // 스트림 시작
                .map(this::generatedImageToDTO) // 각 Post 객체에 postToDTO 함수 적용
                .collect(Collectors.toList());
    }

    public Map<Long, ResponseProfileImageDTO> profilesToMap(List<Image> profiles) {
        return profiles.stream()
                .collect(Collectors.toMap(Image::getImageId, this::profileToDTO));
    }

    public Map<Long, ResponsePostImageDTO> postImagesToMap(List<Image> postImages) {
        return postImages.stream()
                .collect(Collectors.toMap(Image::getImageId, this::postImageToDTO));
    }

    public Map<Long, ResponseGeneratedImageDTO> generatedImagesToMap(List<Image> generatedImages) {
        return generatedImages.stream()
                .collect(Collectors.toMap(Image::getImageId, this::generatedImageToDTO));
    }
}

