package com.tensing.boot.application.post.model;

import com.tensing.boot.application.post.model.dto.PostDto;
import com.tensing.boot.application.post.model.vo.document.PostDocument;
import com.tensing.boot.application.post.model.vo.entity.PostEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;


@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(source = "memberId", target = "member.id"),
    })
    public abstract PostEntity toPost(PostDto.PostRequest postRequest, Long memberId);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "member", ignore = true),
    })
    public abstract PostEntity toPost(PostDto.PostPutRequest postPutRequest);

    @Mappings({
            @Mapping(source = "post.member.username", target = "writer"),
    })
    public abstract PostDto.PostResponse toPostResponse(PostEntity postEntity);

    @Mappings({
            @Mapping(target = "writer", ignore = true),
            @Mapping(source = "postDocument.description", target = "body"),
    })
    public abstract PostDto.PostResponse toPostResponse(PostDocument postDocument);

}
