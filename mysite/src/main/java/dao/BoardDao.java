package dao;

import java.util.List;
import vo.BoardVo;

public interface BoardDao {
	public List<BoardVo> getList();  // 게시물 전체 목록 조회
	public BoardVo getBoard(int no); // 게시물 상세 조회
	public int insert(BoardVo vo);   // 게시물 등록
	public int delete(int no);       // 게시물 삭제
	public int update(BoardVo vo);   // 게시물 수정
	public int countUpdate(int no); // 조회수 업데이트 
	public int getTotal(); // 총 게시물 개수 
	public int getTotal(String field, String kwd); // 검색된 총 게시물 개수 
	public List<BoardVo> getList(int begin, int end); // 게시물 페이징 처리 
	public List<BoardVo> getSearch(String field, String kwd, int begin, int end); // 게시물 검색 
}
