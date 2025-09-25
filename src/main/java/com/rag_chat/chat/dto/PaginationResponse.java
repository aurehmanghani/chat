package com.rag_chat.chat.dto;


public class PaginationResponse {
    private int page;
    private int limit;
    private long total;
    private int pages;

    public PaginationResponse(int page, int limit, long total, int pages) {
        this.page = page;
        this.limit = limit;
        this.total = total;
        this.pages = pages;
    }

    // getters
    public int getPage() { return page; }
    public int getLimit() { return limit; }
    public long getTotal() { return total; }
    public int getPages() { return pages; }
}

