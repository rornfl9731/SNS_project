package com.jinwoo.sns.service;

import com.jinwoo.sns.exception.SnsApplicationException;
import com.jinwoo.sns.model.User;
import com.jinwoo.sns.model.entity.UserEntity;
import com.jinwoo.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;
    // TODO : implement
    public User join(String userName,String password){
        // 이미 가입된 회원인지

        Optional<UserEntity> userEntity = userEntityRepository.findByUserName(userName);
        // 진행
        userEntityRepository.save(new UserEntity());


        return new User();
    }


    // TODO : implement
    public String login(String userName,String password){
        // 회원가압을 했는지
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(()->new SnsApplicationException());

        // 비밀번호 체크
        if(!userEntity.getPassword().equals(password)){
            throw new SnsApplicationException();
        }

        // 토큰 생성

        return "";
    }
}
