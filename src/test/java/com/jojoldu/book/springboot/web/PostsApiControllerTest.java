package com.jojoldu.book.springboot.web;


import com.jojoldu.book.springboot.domain.posts.Posts;
import com.jojoldu.book.springboot.domain.posts.PostsRepository;
import com.jojoldu.book.springboot.web.dto.PostsSaveRequestDto;
import com.jojoldu.book.springboot.web.dto.PostsUpdateRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @AfterEach
    void tearDown() throws Exception{
        postsRepository.deleteAll();
    }

    private String url;

    @BeforeEach
    void setUp(){
        url = "http://localhost:" + port;
    }

    @Test
    void postEnroll(){
        //given
        String title = "Post Title";
        String content = "Post Content";

        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("Airvw")
                .build();
        url += "/api/v1/posts";

        //when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);
        List<Posts> postsAll = postsRepository.findAll();
        assertThat(postsAll.get(0).getTitle()).isEqualTo(title);
        assertThat(postsAll.get(0).getContent()).isEqualTo(content);
    }

    @Test
    void postUpdate() throws Exception{
        //given
        Posts savedPosts = postsRepository.save(Posts.builder()
                .title("Post Title")
                .content("Post Content")
                .author("Airvw")
                .build());

        Long updateId = savedPosts.getId();
        String expectedTitle = "Title Changed";
        String expectedContent = "Content Changed";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        url += "/api/v1/posts/" + updateId;

        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        //when
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> postsAll = postsRepository.findAll();

        assertThat(postsAll.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(postsAll.get(0).getContent()).isEqualTo(expectedContent);
    }
}
