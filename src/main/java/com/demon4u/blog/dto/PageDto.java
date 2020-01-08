package com.demon4u.blog.dto;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PageDto {
    private int number;
    private int totalPages;
    private long totalElements;
    private String uri;
    private Map<Integer, String> pageUrls = new LinkedHashMap<>();
    private String lastUrl;
    private String nextUrl;
    public PageDto(Page page, String uri) {
        this.number = page.getNumber() + 1;
        this.uri = uri;
        this.totalPages = page.getTotalPages();
        if (this.number > this.totalPages) {
            this.number = this.totalPages;
        }
        this.totalElements = page.getTotalElements();
        if (totalPages > 1) {
            int temp = totalPages - number;
            int start = temp <= 2 ? number + temp - 4 : number - 2;
            while (pageUrls.size() < 5 && start <= totalPages) {
                if (start > 0) {
                    pageUrls.put(start, uri + "/" + start);
                }
                start++;
            }
            if (number > 1) {
                this.lastUrl = uri + "/" + (number - 1);
            }
            if (number < totalPages) {
                this.nextUrl = uri + "/" + (number + 1);
            }
        }
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public Map<Integer, String> getPageUrls() {
        return pageUrls;
    }

    public void setPageUrls(Map<Integer, String> pageUrls) {
        this.pageUrls = pageUrls;
    }

    public String getLastUrl() {
        return lastUrl;
    }

    public void setLastUrl(String lastUrl) {
        this.lastUrl = lastUrl;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
