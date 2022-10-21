package spring.board.dto.board;

import lombok.Data;
import lombok.NoArgsConstructor;
import spring.board.domain.Paging;


@Data
@NoArgsConstructor
public class BoardSearchRequest extends Paging {
    private String searchType;
    private String keyword;

    public BoardSearchRequest(String searchType, String keyword, int curPage, int pageSize, int blockSize, int totalCnt) {
        this.searchType = searchType;
        this.keyword = keyword;
        super(curPage, pageSize, blockSize, totalCnt);
    }
}
