package com.hng.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationMeta {


    @Builder.Default
    @JsonProperty("total")
    private int total = 1;

    @Builder.Default
    @JsonProperty("limit")
    private int limit = 1;

    @Builder.Default
    @JsonProperty("page")
    private int page = 1;

    @Builder.Default
    @JsonProperty("total_pages")
    private int totalPages = 1;


    @Builder.Default
    @JsonProperty("has_next")
    private boolean hasNext = false;

    @Builder.Default
    @JsonProperty("has_previous")
    private boolean hasPrevious = false;

}
