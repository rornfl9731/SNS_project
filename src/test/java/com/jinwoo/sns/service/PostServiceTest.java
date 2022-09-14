package com.jinwoo.sns.service;

import com.jinwoo.sns.exception.ErrorCode;
import com.jinwoo.sns.exception.SnsApplicationException;
import com.jinwoo.sns.fixture.PostEntityFixture;
import com.jinwoo.sns.model.entity.PostEntity;
import com.jinwoo.sns.model.entity.UserEntity;
import com.jinwoo.sns.repository.PostEntityRepository;
import com.jinwoo.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockBean
    private PostEntityRepository postEntityRepository;
    @MockBean
    private UserEntityRepository userEntityRepository;

    @Test
    void 포스트작성이_정상(){
        String title = "title";
        String body = "body";
        String userName = "userName";

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));


        Assertions.assertDoesNotThrow(()->postService.create(title,body,userName));

    }

    @Test
    void 포스트작성시_요청한유저가_존재하지않은경우(){
        String title = "title";
        String body = "body";
        String userName = "userName";

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));


        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class,()->postService.create(title,body,userName));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND,e.getErrorCode());

    }

    @Test
    void 포스트쑤정이_정상(){
        String title = "title";
        String body = "body";
        String userName = "userName";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(userName,postId);
        UserEntity userEntity  = postEntity.getUser();

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(any())).thenReturn(Optional.of(postEntity));


        Assertions.assertDoesNotThrow(()->postService.modify(title,body,userName,postId));

    }
}
