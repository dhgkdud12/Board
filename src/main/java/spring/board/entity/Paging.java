package spring.board.entity;

public class Paging {

    /*
    10개씩 출력
    이전, 다음 페이지
    10개씩 넘기기, 맨 첫페이지, 맨 마지막페이지
     */

    private int pageSize = 10; // 10개씩 출력
    private int curPage = 1;  // 현재 페이지
    private int totalCnt; // 총 게시글 수
    private int pageCnt; // 총 페이지 수
    private int startPage = 1; // 시작 페이지
    private int endPage = 1; // 마지막 페이지
    private int startIndex = 0; // 시작 index - ??
    private int prevPage; // 이전 페이지
    private int nextPage; // 다음 페이지


    public Paging(int totalCnt, int curPage) { // 총 게시물수, 현재 페이지
        setCurPage(curPage); // 현재 페이지
        setTotalCnt(totalCnt); // 총 게시물 수
        setPageCnt(totalCnt);
        setStartIndex(curPage); // startIdx
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getCurPage() {
        return curPage;
    }

    public int getTotalCnt() {
        return totalCnt;
    }

    public int getPageCnt() {
        return pageCnt;
    }

    public int getStartPage() {
        return startPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getPrevPage() {
        return prevPage;
    }

    public int getNextPage() {
        return nextPage;
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

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }

    public void setStartIndex(int curPage) {
        this.startIndex = (curPage - 1) * pageSize;
    }

    public void setPrevPage(int prevPage) {
        this.prevPage = prevPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }
}
