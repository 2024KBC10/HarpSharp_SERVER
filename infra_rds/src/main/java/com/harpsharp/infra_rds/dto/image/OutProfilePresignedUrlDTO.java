package com.harpsharp.infra_rds.dto.image;

import java.net.URL;

public record OutProfilePresignedUrlDTO(URL presignedUrl, String uuid) {
}
