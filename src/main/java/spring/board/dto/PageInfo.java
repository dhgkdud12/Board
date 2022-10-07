package spring.board.dto;

import lombok.Getter;
import lombok.Setter;
import spring.board.entity.Paging;

@Getter
@Setter
public class PageInfo {
    private final int pageSize;
    private final int curPage;
    private final int totalCnt;
    private final int pageCnt;
    private final int endPage;
    private final int blockSize;


    public PageInfo(Paging paging) {
        this.pageSize = paging.getPageSize();
        this.curPage = paging.getCurPage();
        this.totalCnt = paging.getTotalCnt();
        this.pageCnt = paging.getPageCnt();
        this.endPage = paging.getEndPage();
        this.blockSize = paging.getBlockSize();
    }
}
