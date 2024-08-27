package com.harpsharp.album.application.usecase;

import com.harpsharp.infra_rds.dto.image.ImageType;
import com.harpsharp.infra_rds.dto.image.InProfilePresignedUrlDTO;
import com.harpsharp.infra_rds.dto.image.ResponseProfilePresignedUrlDTO;

public interface ProfilePresignedUrlUsecase {
    public ResponseProfilePresignedUrlDTO putPresignedUrl(InProfilePresignedUrlDTO presignedUrlDTO);
    public void deletePresignedUrl(ImageType type, String staticUrl);
}