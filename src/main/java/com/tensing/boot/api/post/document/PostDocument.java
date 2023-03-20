package com.tensing.boot.api.post.document;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDateTime;

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

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Field(value = "created_at", type = FieldType.Date)
    private LocalDateTime createdAt;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Field(value = "updated_at", type = FieldType.Date)
    private LocalDateTime updatedAt;
}
