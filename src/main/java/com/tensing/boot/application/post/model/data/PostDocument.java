package com.tensing.boot.application.post.model.data;

import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDateTime;

@FieldNameConstants
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "post")
@Mapping(mappingPath = "elastic/post-mapping.json")
@Setting(settingPath = "elastic/post-setting.json")
public class PostDocument {

    @Id
    @Field(value = "id", type = FieldType.Integer)
    private Long id;

    @Field(value = "title", type = FieldType.Text, analyzer = "korean_analyzer")
    private String title;

    @Field(value = "description", type = FieldType.Text, analyzer = "korean_analyzer")
    private String description;

    @Field(value = "created_at", type = FieldType.Date)
    private LocalDateTime createdDate;

    @Field(value = "updated_at", type = FieldType.Date)
    private LocalDateTime updatedDate;
}
