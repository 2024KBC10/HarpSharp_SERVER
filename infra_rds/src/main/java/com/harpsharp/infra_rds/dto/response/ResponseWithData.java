package com.harpsharp.infra_rds.dto.response;

public record ResponseWithData<T>(String code,
                                  String message,
                                  T data) { }
