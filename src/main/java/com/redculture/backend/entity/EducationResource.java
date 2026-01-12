package com.redculture.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDate;

@Data
@TableName("education_resource")
public class EducationResource {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private String resourceType; // ENUM ('Book', 'Article', 'Image', 'Video', 'Audio')
    private String contentUrl;
    private String description;
    private LocalDate publishDate;
    private Integer redSpotId;
}
