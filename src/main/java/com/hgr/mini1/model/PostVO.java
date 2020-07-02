package com.hgr.mini1.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Data
@Entity
public class PostVO {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private int Id;
    private String userId;
    private String postContent;
    private String postPicture;
}

