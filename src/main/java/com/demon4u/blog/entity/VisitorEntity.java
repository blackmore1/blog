package com.demon4u.blog.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
@Entity
@Table(name = "visitor")
public class VisitorEntity implements Serializable {
    private static final long serialVersionUID = 5839280022989764317L;
    @Id
    @Column
    private String date;
    @Column(name = "clickNum")
    private Integer pv;
    @Column(name = "visitorNum")
    private Integer uv;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getPv() {
        return pv;
    }

    public void setPv(Integer pv) {
        this.pv = pv;
    }

    public Integer getUv() {
        return uv;
    }

    public void setUv(Integer uv) {
        this.uv = uv;
    }
}
