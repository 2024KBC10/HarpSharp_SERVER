package com.harpsharp.album.application.usecase;


import com.harpsharp.infra_rds.dto.image.RequestProfileImageDTO;
import com.harpsharp.infra_rds.dto.image.ResponseProfileImageDTO;

import java.net.MalformedURLException;

public interface UploadProfileImageUsecase {
    public ResponseProfileImageDTO confirmProfileUrl(RequestProfileImageDTO requestProfileImageDTO) throws MalformedURLException;
}
