package com.harpsharp.album.framework.web;

import com.harpsharp.album.application.inputport.ProfilePresignedUrlInputPort;
import com.harpsharp.album.application.inputport.ProfileImageInputPort;
import com.harpsharp.infra_rds.dto.image.*;
import com.harpsharp.infra_rds.dto.response.ResponseWithData;
import com.harpsharp.infra_rds.mapper.ImageMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileImageController {
    private final ProfilePresignedUrlInputPort presignedUrlInputPort;
    private final ProfileImageInputPort profileImageInputPort;

    @GetMapping("/presigned/{filename}")
    public ResponseEntity<ResponseWithData<ResponseProfilePresignedUrlDTO>> GenerateProfilePresignedUrl(@PathVariable String filename){

        InProfilePresignedUrlDTO inProfilePresignedUrlDTO = new InProfilePresignedUrlDTO(filename);
        ResponseProfilePresignedUrlDTO responseData = presignedUrlInputPort.putPresignedUrl(inProfilePresignedUrlDTO);

        ResponseWithData<ResponseProfilePresignedUrlDTO> response =
                new ResponseWithData<>(
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "URL_CREAETD_SUCCESSFULLY",
                        "URL이 정상적으로 발급되었습니다.",
                        responseData);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/confirm")
    public ResponseEntity<ResponseWithData<ResponseProfileImageDTO>> ConfirmProfileImage(@RequestBody RequestProfileImageDTO requestProfileIamgeDTO) throws MalformedURLException {
        ResponseProfileImageDTO data
                = profileImageInputPort.confirmProfileUrl(requestProfileIamgeDTO);

        ResponseWithData<ResponseProfileImageDTO> response =
                new ResponseWithData<>(
                        LocalDateTime.now(),
                        HttpStatus.CREATED.value(),
                        "CONFIRM_PROFILE",
                        "프로필 이미지가 성공적으로 등록되었습니다.",
                        data);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
