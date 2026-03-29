package com.minmvp.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileVO {

    private String fileId;

    private String url;

    private String name;

    private String size;

    private String type;

    private String uploadTime;

    private String uploader;
}
