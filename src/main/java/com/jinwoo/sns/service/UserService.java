package com.jinwoo.sns.service;

import com.jinwoo.sns.exception.ErrorCode;
import com.jinwoo.sns.exception.SnsApplicationException;
import com.jinwoo.sns.model.User;
import com.jinwoo.sns.model.entity.UserEntity;
import com.jinwoo.sns.repository.UserEntityRepository;
import com.jinwoo.sns.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;
    private final BCryptPasswordEncoder encoder;
    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;
    // TODO : implement

    public User loadUserByUserName(String userName){
        return userEntityRepository.findByUserName(userName).map(User::fromEntity)
                .orElseThrow(()->new SnsApplicationException(ErrorCode.USER_NOT_FOUND,String.format("%s not found",userName)));

    }
    @Transactional
    public User join(String userName,String password){
        // 이미 가입된 회원인지
        userEntityRepository.findByUserName(userName).ifPresent(it->{
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME,String.format("%s is duplicated",userName));
        });
        // 진행
        UserEntity userEntity = userEntityRepository.save(UserEntity.of(userName,encoder.encode(password)));
        //throw new RuntimeException();
        //throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME,String.format("%s is duplicated",userName));
        return User.fromEntity(userEntity);
    }


    // TODO : implement
    public String login(String userName,String password){
        // 회원가입을 했는지
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(()->new SnsApplicationException(ErrorCode.USER_NOT_FOUND,String.format(
                "%s not founded",userName
        )));

        // 비밀번호 체크
        if(!encoder.matches(password,userEntity.getPassword())){
            throw new SnsApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        // 토큰 생성
        String token = JwtTokenUtils.generateToken(userName,secretKey,expiredTimeMs);
        return token;
    }
}
