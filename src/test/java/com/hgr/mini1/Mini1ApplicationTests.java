package com.hgr.mini1;

import com.hgr.mini1.model.PostVO;
import com.hgr.mini1.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Mini1ApplicationTests {
    @Autowired
    PostRepository pr;

    @Test
    void contextLoads() {
        PostVO pv = new PostVO();

        pv.setPostContent("test");
        pv.setUserId("userId");
        pv.setPostPicture("postPicture");


        pr.save(pv);
    }

}
