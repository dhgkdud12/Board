package spring.board.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
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

    public Paging(int curPage, int pageSize, int blockSize, int totalCnt) {
        this.pageSize = pageSize;
        this.curPage = curPage;
        this.totalCnt = totalCnt;
        this.pageCnt = (int)Math.ceil(totalCnt/pageSize)+1;
        this.endPage = this.pageCnt;
        this.startIndex = (curPage - 1) * pageSize;
        this.endIndex = curPage * pageSize;
        this.blockSize = blockSize;
        this.totalBlockCnt =  (int)Math.ceil(this.pageCnt/blockSize)+1;
    }
}
