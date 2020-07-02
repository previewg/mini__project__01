package com.hgr.mini1.repository;

import com.hgr.mini1.model.PostVO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostVO,Integer> {
}
