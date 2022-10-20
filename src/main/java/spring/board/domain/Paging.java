package spring.board.domain;

import lombok.Getter;

@Getter
public class Paging {

    /*
    10개씩 출력
    이전, 다음 페이지
    10개씩 넘기기, 맨 첫페이지, 맨 마지막페이지
     */

    // 프론트 입력 받을 데이터 2개
    private int pageSize; // 10개씩 출력, 출력할 사이즈
    private int curPage = 1;  // 현재 페이지 2
    
    
    private int totalCnt; // 총 게시글 수 143개라면
    private int pageCnt; // 총 페이지 수 15
    
    // 20부터 30까지 출력
    
    private int startPage = 1;
    private int endPage;
    private int startIndex = 0;
    private int endIndex;
    private int prevPage;
    private int nextPage;

    private int blockSize;
    
    private int totalBlockCnt; // 총 블록 사이즈

    public void setPaging(int page, int size, int blockSize, int totalCnt) { // 총 게시물수, 현재 페이지
        setCurPage(page); // 현재 페이지
        setPageSize(size);
        setTotalCnt(totalCnt); // 총 게시물 수
        setPageCnt(totalCnt); // 페이지수 계산
        setEndPage(this.pageCnt);
        setStartIndex(page); // startIdx
        setEndIndex(page); // endIdx
        setBlockSize(blockSize);
        setTotalBlockCnt(this.pageCnt);
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public void setTotalCnt(int totalCnt) {
        this.totalCnt = totalCnt;
    }

    public void setPageCnt(int totalCnt) { // 총 페이지수
        this.pageCnt = (int)Math.ceil(totalCnt/pageSize)+1;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public void setEndPage(int pageCnt) {
        this.endPage = pageCnt;
    }

    public void setStartIndex(int curPage) {
        this.startIndex = (curPage - 1) * pageSize;
    }

    public void setEndIndex(int curPage) {
        this.endIndex = curPage * pageSize;
    }

//    public void setPrevPage(int curPage) {
//        if (curPage == 1) this.prevPage = 1;
//        else this.prevPage = curPage - 1;
//    }
//
//    public void setNextPage(int curPage) {
//        if (curPage == endPage) this.nextPage = curPage;
//        else this.nextPage = curPage + 1;
//    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public void setTotalBlockCnt(int pageCnt) {
        this.totalBlockCnt =  (int)Math.ceil(pageCnt/blockSize)+1;
    }
}
