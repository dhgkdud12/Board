package spring.board.dto.common;

import lombok.Getter;
import lombok.Setter;
import spring.board.domain.Paging;

@Getter
@Setter
public class PageInfo {
    
    // 목적 : 페이지 정보 반환할 때 보여주려고
    private final int pageSize;
    private final int curPage;
    private final int totalCnt;
    private final int pageCnt;
    private final int blockSize;


    public PageInfo(Paging paging) {
        this.pageSize = paging.getPageSize();
        this.curPage = paging.getCurPage();
        this.totalCnt = paging.getTotalCnt();
        this.pageCnt = paging.getPageCnt();
        this.blockSize = paging.getBlockSize();
    }
}
