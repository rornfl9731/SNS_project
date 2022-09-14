package com.jinwoo.sns.service;

import com.jinwoo.sns.exception.ErrorCode;
import com.jinwoo.sns.exception.SnsApplicationException;
import com.jinwoo.sns.model.Post;
import com.jinwoo.sns.model.entity.PostEntity;
import com.jinwoo.sns.model.entity.UserEntity;
import com.jinwoo.sns.repository.PostEntityRepository;
import com.jinwoo.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;

    @Transactional
    public void create(String title,String body,String username){
        // user find
        UserEntity userEntity = userEntityRepository.findByUserName(username).orElseThrow(()->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND,String.format("%s not found",username)));

        //post save
        PostEntity saved = postEntityRepository.save(PostEntity.of(
                title,body,userEntity
        ));

        //return
    }

    @Transactional
    public Post modify(String title, String body, String username, Integer postId){
        UserEntity userEntity = userEntityRepository.findByUserName(username).orElseThrow(()->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND,String.format("%s not found",username)));

        //post exit
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(()->
            new SnsApplicationException(ErrorCode.POST_NOT_FOUND,String.format("%s not founded",postId))
        );

        //post permission
        if(postEntity.getUser() != userEntity){
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION,String.format("%s has no permission with %s",username,postId));
        }

        postEntity.setTitle(title);
        postEntity.setBody(body);

        return Post.fromEntity(postEntityRepository.saveAndFlush(postEntity));
    }

    @Transactional
    public void delete(Integer postId,String username){
        UserEntity userEntity = userEntityRepository.findByUserName(username).orElseThrow(()->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND,String.format("%s not found",username)));

        //post exit
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(()->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND,String.format("%s not founded",postId))
        );

        //post permission
        if(postEntity.getUser() != userEntity){
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION,String.format("%s has no permission with %s",username,postId));
        }

        postEntityRepository.delete(postEntity);

    }

    public Page<Post> list(Pageable pageable){
        return postEntityRepository.findAll(pageable).map(Post::fromEntity);
    }

    public Page<Post> my(String username,Pageable pageable){
        UserEntity userEntity = userEntityRepository.findByUserName(username).orElseThrow(()->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND,String.format("%s not found",username)));

        return postEntityRepository.findAllBy(userEntity,pageable).map(Post::fromEntity);
    }
}
