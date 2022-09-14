package com.jinwoo.sns.controller;

import com.jinwoo.sns.controller.request.PostCreateRequest;
import com.jinwoo.sns.controller.request.PostModifyRequest;
import com.jinwoo.sns.controller.response.PostResponse;
import com.jinwoo.sns.controller.response.Response;
import com.jinwoo.sns.model.Post;
import com.jinwoo.sns.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    final PostService postService;

    @PostMapping
    public Response<Void> create(@RequestBody PostCreateRequest request, Authentication authentication){
        postService.create(request.getTitle(),request.getBody(),authentication.getName());
        return Response.success();
    }
    @PutMapping("/{postId}")
    public Response<PostResponse> modify(@PathVariable Integer postId, @RequestBody PostModifyRequest request, Authentication authentication){
        Post post = postService.modify(request.getTitle(),request.getBody(),authentication.getName(),postId);
        return Response.success(PostResponse.fromPost(post));
    }

    @DeleteMapping("/{postId}")
    public Response<Void> delete(@PathVariable Integer postId,Authentication authentication){
        postService.delete(postId,authentication.getName());
        return Response.success();
    }

    @GetMapping
    public Response<Page<PostResponse>> list(Pageable pageable, Authentication authentication){
        return Response.success(postService.list(pageable).map(PostResponse::fromPost));

    }

    @GetMapping("/my")
    public Response<Page<PostResponse>> my(Pageable pageable, Authentication authentication){
        return Response.success(postService.my(authentication.getName(),pageable).map(PostResponse::fromPost));

    }

}
