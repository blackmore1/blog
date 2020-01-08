package com.demon4u.blog.dto;

public class TagDto {
    private long count;
    private String word;

    public TagDto(long count, String word) {
        this.count = count;
        this.word = word;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
