package com.tensing.boot.application.post.model;

import com.tensing.boot.application.post.model.dto.PostDto;
import com.tensing.boot.application.post.model.data.PostDocument;
import com.tensing.boot.application.post.model.data.PostEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;


@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(source = "memberId", target = "memberEntity.id"),
    })
    public abstract PostEntity toPost(PostDto.PostRequest postRequest, Long memberId);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "memberEntity", ignore = true),
    })
    public abstract PostEntity toPost(PostDto.PostPutRequest postPutRequest);

    @Mappings({
            @Mapping(source = "postEntity.memberEntity.username", target = "writer"),
    })
    public abstract PostDto.PostResponse toPostResponse(PostEntity postEntity);

    @Mappings({
            @Mapping(target = "writer", ignore = true),
            @Mapping(source = "postDocument.description", target = "body"),
    })
    public abstract PostDto.PostResponse toPostResponse(PostDocument postDocument);

}
