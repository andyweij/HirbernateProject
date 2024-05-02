package com.eland.pojo.vo;

import java.util.ArrayList;
import java.util.List;

public class GenericPageable {
    private int currentPageNo;// 當前頁面
    private int pageDataSize = 5;// 每頁最大筆數
    private int pagesIconSize;// 頁次數量
    private int totalItems;
    private List<Integer> pagination;//分頁標籤列表
    private List<Integer> rowNum;//當前分頁頭尾序號
    private int endPage;//最末頁



    //分頁標籤列表
    public List<Integer> pagination(GenericPageable genericPageable, int totalPages) {
        List<Integer> pagination = new ArrayList<>();
        if (genericPageable.getPagesIconSize() >= totalPages) {
            for (int i = 1; i <= totalPages; i++) {
                pagination.add(i);
            }
        } else {
            int frontPages;
            int behindPages;
            if (genericPageable.getPagesIconSize() % 2 == 0) {
                frontPages = genericPageable.getPagesIconSize() / 2 - 1;
                behindPages = genericPageable.getPagesIconSize() / 2;
            } else {
                frontPages = genericPageable.getPagesIconSize() / 2;
                behindPages = frontPages;
            }
            if (genericPageable.getCurrentPageNo() - frontPages > 0 && genericPageable.getCurrentPageNo() + behindPages <= totalPages) {
                for (int i = genericPageable.getCurrentPageNo() - frontPages; i <= genericPageable.getCurrentPageNo() + behindPages; i++) {
                    pagination.add(i);
                }
            } else if (genericPageable.getCurrentPageNo() + frontPages >= totalPages && genericPageable.getCurrentPageNo() != totalPages) {
                for (int i = genericPageable.getCurrentPageNo() - behindPages - (genericPageable.getCurrentPageNo() + frontPages - totalPages); i <= totalPages; i++) {
                    pagination.add(i);
                }
            } else if (genericPageable.getCurrentPageNo() == 1 || genericPageable.getCurrentPageNo() - frontPages == 0) {
                for (int i = 1; i <= genericPageable.getPagesIconSize(); i++) {
                    pagination.add(i);
                }
            } else {
                for (int i = totalPages - genericPageable.getPagesIconSize() + 1; i <= totalPages; i++) {
                    pagination.add(i);
                }
            }


        }
        return pagination;

    }

    //當前分頁頭尾序號
    public List<Integer> rowNum(GenericPageable genericPageable) {
        List<Integer> rowNum = new ArrayList<>();
        int EndNo=0;
        int StartNo=0;
        if(genericPageable.getTotalItems()>genericPageable.getPageDataSize()*genericPageable.getCurrentPageNo()) {
           EndNo = genericPageable.getCurrentPageNo() * genericPageable.getPageDataSize();
        }else{
            EndNo=genericPageable.getTotalItems();
        }
        if(EndNo<=genericPageable.getPageDataSize()){
            StartNo=0;
        }else {
            StartNo = (genericPageable.getCurrentPageNo()-1)*genericPageable.getPageDataSize();
        }
        rowNum.add(StartNo);
        rowNum.add(EndNo);
        return rowNum;
    }

    //    計算總頁數
    public int totalPages(GenericPageable genericPageable, int itemsMounts) {
        int totalPages = (int) Math.ceil((double) itemsMounts / genericPageable.getPageDataSize());
        return totalPages;
    }

    public int getCurrentPageNo() {
        return currentPageNo;
    }

    public void setCurrentPageNo(int currentPageNo) {
        this.currentPageNo = currentPageNo;
    }

    public int getPageDataSize() {
        return pageDataSize;
    }

    public void setPageDataSize(int pageDataSize) {
        this.pageDataSize = pageDataSize;
    }

    public int getPagesIconSize() {
        return pagesIconSize;
    }

    public void setPagesIconSize(int pagesIconSize) {
        this.pagesIconSize = pagesIconSize;
    }

    public List<Integer> getPagination() {
        return pagination;
    }

    public void setPagination(List<Integer> pagination) {
        this.pagination = pagination;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }

    public List<Integer> getRowNum() {
        return rowNum;
    }

    public void setRowNum(List<Integer> rowNum) {
        this.rowNum = rowNum;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }
}
