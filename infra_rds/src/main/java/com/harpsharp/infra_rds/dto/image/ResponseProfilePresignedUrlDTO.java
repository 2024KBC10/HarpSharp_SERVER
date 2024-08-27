package com.harpsharp.infra_rds.dto.image;

import java.net.URL;

public record ResponseProfilePresignedUrlDTO(URL presignedUrl, String uuid) {
}
