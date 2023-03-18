package com.tensing.boot.api.search.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "search")
@Mapping(mappingPath = "elastic/search-mapping.json")
@Setting(settingPath = "elastic/search-setting.json")
public class SearchDocument {

    @Id
    @Field(value = "id", type = FieldType.Integer)
    private Long id;

    @Field(value = "title", type = FieldType.Text, analyzer = "korean_analyzer")
    private String title;

    @Field(value = "description", type = FieldType.Text, analyzer = "korean_analyzer")
    private String description;

    @Field(value = "created_at", type = FieldType.Date)
    private LocalDateTime createdAt;

    @Field(value = "updated_at", type = FieldType.Date)
    private LocalDateTime updatedAt;
}
