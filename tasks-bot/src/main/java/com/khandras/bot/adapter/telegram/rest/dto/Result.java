package com.khandras.bot.adapter.telegram.rest.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Result {
    @JsonProperty("file_id")
    String fileId;
    @JsonProperty("file_unique_id")
    String fileUniqueId;
    @JsonProperty("file_size")
    String fileSize;
    @JsonProperty("file_path")
    String filePath;
}
