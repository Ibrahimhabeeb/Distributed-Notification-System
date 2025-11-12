package com.hng.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;


@Builder
public class ApiResponse<T> {

    private boolean success;



    private T data;


    private String error;


    private String message;

    private PaginationMeta meta;
}
