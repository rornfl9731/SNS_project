package com.jinwoo.sns.service;

import com.jinwoo.sns.exception.ErrorCode;
import com.jinwoo.sns.exception.SnsApplicationException;
import com.jinwoo.sns.model.entity.PostEntity;
import com.jinwoo.sns.model.entity.UserEntity;
import com.jinwoo.sns.repository.PostEntityRepository;
import com.jinwoo.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
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
}
