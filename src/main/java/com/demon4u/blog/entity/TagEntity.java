package com.demon4u.blog.entity;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "blog_tag")
@Entity
public class TagEntity implements Serializable {
    private static final long serialVersionUID = 8488225086402564805L;
    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @Column(name = "word")
    private String word;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
