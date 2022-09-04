package com.jinwoo.sns.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
@Data
public class UserEntity {

    @Id
    private Integer id;
    private String userName;
    private String password;

}
