package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vo.BoardVo;

public class BoardDaoImpl implements BoardDao {
  private Connection getConnection() throws SQLException {
    Connection conn = null;
    try {
      Class.forName("oracle.jdbc.driver.OracleDriver");
      String dburl = "jdbc:oracle:thin:@localhost:1521:xe";
      conn = DriverManager.getConnection(dburl, "webdb", "1234");
    } catch (ClassNotFoundException e) {
      System.err.println("JDBC 드라이버 로드 실패!");
    }
    return conn;
  }
  
	public List<BoardVo> getList() {

		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardVo> list = new ArrayList<BoardVo>();

		try {
			conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "select b.no, b.title, b.hit, b.reg_date, b.user_no, u.name "
					     + " from board b, users u "
					     + " where b.user_no = u.no "
					     + " order by no desc";
			
			pstmt = conn.prepareStatement(query);

			rs = pstmt.executeQuery();
			// 4.결과처리
			while (rs.next()) {
				int no = rs.getInt("no");
				String title = rs.getString("title");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("reg_date");
				int userNo = rs.getInt("user_no");
				String userName = rs.getString("name");
				
				BoardVo vo = new BoardVo(no, title, hit, regDate, userNo, userName);
				list.add(vo);
				
			}
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}
		
		return list;

	}
	
	public List<BoardVo> getList(int begin, int end) {

		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardVo> list = new ArrayList<BoardVo>();

		try {
			conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = " SELECT * "
		                 + " FROM (SELECT ROWNUM AS RN, A.* "
		                     + " FROM (SELECT b.no, b.title, b.hit, b.reg_date, b.user_no, u.name, b.content "
		                     + " FROM board b, users u where b.user_no = u.no ORDER BY NO DESC) A"
		                     + " )"
		               + " WHERE RN >= ? AND RN <= ?";
			
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, begin);
			pstmt.setInt(2, end);

			rs = pstmt.executeQuery();
			// 4.결과처리
			while (rs.next()) {
				int no = rs.getInt("no");
				String title = rs.getString("title");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("reg_date");
				int userNo = rs.getInt("user_no");
				String userName = rs.getString("name");
				
				BoardVo vo = new BoardVo(no, title, hit, regDate, userNo, userName);
				list.add(vo);
				
			}
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}
		return list;
	}
	
	public List<BoardVo> getSearch(String field, String kwd, int begin, int end) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardVo> list = new ArrayList<BoardVo>();
		
		try {
				conn = getConnection();
				if((kwd != null && !kwd.equals(""))&& field != "0") {
					if(field.equals("title")) {
						String query = " SELECT * " +
									   " FROM (SELECT ROWNUM AS RN, A.* " +
									         " FROM (SELECT b.no, b.title, b.hit, b.reg_date, b.user_no, u.name, b.content " +
											       " FROM board b, users u " +
									               " where b.user_no = u.no " +
									               " ORDER BY NO DESC " +
									         " ) A " +
									          " where title like ? " +
									   " ) " +
									    " WHERE RN >= ? AND RN <= ? ";
						pstmt = conn.prepareStatement(query);
						pstmt.setString(1, "%"+kwd+"%");
						pstmt.setInt(2, begin);
						pstmt.setInt(3, end);
					} else if(field.equals("content")) {
						String query = " SELECT * " +
								   " FROM (SELECT ROWNUM AS RN, A.* " +
								         " FROM (SELECT b.no, b.title, b.hit, b.reg_date, b.user_no, u.name, b.content " +
										       " FROM board b, users u " +
								               " where b.user_no = u.no " +
								               " ORDER BY NO DESC " +
								         " ) A " +
								          " where content like ? " +
								   " ) " +
								    " WHERE RN >= ? AND RN <= ? ";
					pstmt = conn.prepareStatement(query);
					pstmt.setString(1, "%"+kwd+"%");
					pstmt.setInt(2, begin);
					pstmt.setInt(3, end);
					} else if(field.equals("userName")) {
						String query = " SELECT * " +
								   " FROM (SELECT ROWNUM AS RN, A.* " +
								         " FROM (SELECT b.no, b.title, b.hit, b.reg_date, b.user_no, u.name, b.content " +
										       " FROM board b, users u " +
								               " where b.user_no = u.no " +
								               " ORDER BY NO DESC " +
								         " ) A " +
								          " where name like ? " +
								   " ) " +
								    " WHERE RN >= ? AND RN <= ? ";
					pstmt = conn.prepareStatement(query);
					pstmt.setString(1, "%"+kwd+"%");
					pstmt.setInt(2, begin);
					pstmt.setInt(3, end);
					} else if(field.equals("reg_date")) {
						String query = " SELECT * " +
								   " FROM (SELECT ROWNUM AS RN, A.* " +
								         " FROM (SELECT b.no, b.title, b.hit, b.reg_date, b.user_no, u.name, b.content " +
										       " FROM board b, users u " +
								               " where b.user_no = u.no " +
								               " ORDER BY NO DESC " +
								         " ) A " +
								          " where reg_date like ? " +
								   " ) " +
								    " WHERE RN >= ? AND RN <= ? ";
					pstmt = conn.prepareStatement(query);
					pstmt.setString(1, "%"+kwd+"%");
					pstmt.setInt(2, begin);
					pstmt.setInt(3, end);
					
					} else if(field.equals("file")) {
							String query = " SELECT * " +
								   " FROM (SELECT ROWNUM AS RN, A.* " +
								         " FROM (SELECT b.no, b.title, b.hit, b.reg_date, b.user_no, u.name, b.content " +
										       " FROM board b, users u " +
								               " where b.user_no = u.no and fileName1 like ? or fileName2 like ?" +
								               " ORDER BY NO DESC " +
								         " ) A " +
								   " ) " +
								    " WHERE RN >= ? AND RN <= ? ";
						pstmt = conn.prepareStatement(query);
						pstmt.setString(1, "%"+kwd+"%");
						pstmt.setString(2, "%"+kwd+"%");
						pstmt.setInt(3, begin);
						pstmt.setInt(4, end);
					}
				
				rs=pstmt.executeQuery();
				
				}
				
				while(rs.next()) {
					int no = rs.getInt("no");
					String title = rs.getString("title");
					int hit = rs.getInt("hit");
					String regDate = rs.getString("reg_date");
					int userNo = rs.getInt("user_no");
					String userName = rs.getString("name");
					
					BoardVo vo = new BoardVo(no, title, hit, regDate, userNo, userName);
					list.add(vo);
				}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}
		}
		
		return list;
	}
	
	public int countUpdate(int no) {
		
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;
		System.out.println(no);
		try {
			conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = " update board set hit = hit+1 where no = ?";
			
			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, no);
			
			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건 조회");
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}
		}
		return count;
	}
	
	public BoardVo getBoard(int no) {

		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BoardVo boardVo = null;
		
		try {
		  conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "select b.no, b.title, b.content, b.hit, b.reg_date, b.user_no, u.name, b.filename1, b.filename2 "
					     + "from board b, users u "
					     + "where b.user_no = u.no "
					     + "and b.no = ?";
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, no);
			
			rs = pstmt.executeQuery();
			// 4.결과처리
			while (rs.next()) {
				String title = rs.getString("title");
				String content = rs.getString("content");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("reg_date");
				int userNo = rs.getInt("user_no");
				String userName = rs.getString("name");
				String fileName1 = rs.getString("fileName1");
				String fileName2 = rs.getString("fileName2");
				
				boardVo = new BoardVo(no, title, content, hit, regDate, userNo, userName, fileName1, fileName2);
			}
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}
		System.out.println(boardVo);
		return boardVo;

	}
	
	public int insert(BoardVo vo) {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
		  conn = getConnection();
		  
		  System.out.println("vo.userNo : ["+vo.getUserNo()+"]");
	      System.out.println("vo.title : ["+vo.getTitle()+"]");
	      System.out.println("vo.content : ["+vo.getContent()+"]");
	      
	      String query = "insert into board values (seq_board_no.nextval, ?, ?, 0, sysdate, ?, ?, ?)";
	      pstmt = conn.prepareStatement(query);

	      pstmt.setString(1, vo.getTitle());
	      pstmt.setString(2, vo.getContent());
	      pstmt.setInt(3, vo.getUserNo());
	      pstmt.setString(4, vo.getFileName1());
	      pstmt.setString(5, vo.getFileName2());
      
	      count = pstmt.executeUpdate();

			// 4.결과처리
	      System.out.println(count + "건 등록");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}

		return count;
	}
	
	
	public int delete(int no) {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
		  conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "delete from board where no = ?";
			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, no);

			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건 삭제");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}

		return count;
	}
	
	
	public int update(BoardVo vo) {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
		  conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "update board set title = ?, content = ? where no = ? ";
			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContent());
			pstmt.setInt(3, vo.getNo());

			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건 수정");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}

		return count;
	}
	
	public int getTotal() {
		int cnt = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			  conn = getConnection();

				// 3. SQL문 준비 / 바인딩 / 실행
				String query = "select count(*) cnt from board";
				
				pstmt = conn.prepareStatement(query);
				
				rs = pstmt.executeQuery();
				// 4.결과처리
				if (rs.next()) {
					cnt = rs.getInt("cnt");
				}
				
			} catch (SQLException e) {
				System.out.println("error:" + e);
			} finally {
				// 5. 자원정리
				try {
					if (pstmt != null) {
						pstmt.close();
					}
					if (conn != null) {
						conn.close();
					}
			} catch (SQLException e) {
					System.out.println("error:" + e);
		}

	}
		return cnt;

	}
	
	public int getTotal(String field, String kwd) {
		int cnt = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			if((kwd != null && !kwd.equals(""))&& field != "0") {
				if(field.equals("title")) {
					String query = " SELECT count(*) cnt" +
								   " FROM (SELECT ROWNUM AS RN, A.* " +
								         " FROM (SELECT b.no, b.title, b.hit, b.reg_date, b.user_no, u.name, b.content " +
										       " FROM board b, users u " +
								               " where b.user_no = u.no " +
								               " ORDER BY NO DESC " +
								         " ) A " +
								          " where title like ? " +
								   " ) ";
					pstmt = conn.prepareStatement(query);
					pstmt.setString(1, "%"+kwd+"%");
				
				} else if(field.equals("content")) {
					String query = "SELECT count(*) cnt" +
							   " FROM (SELECT ROWNUM AS RN, A.* " +
							         " FROM (SELECT b.no, b.title, b.hit, b.reg_date, b.user_no, u.name, b.content " +
									       " FROM board b, users u " +
							               " where b.user_no = u.no " +
							               " ORDER BY NO DESC " +
							         " ) A " +
							          " where content like ? " +
							   " )";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, "%"+kwd+"%");
				
				} else if(field.equals("userName")) {
					String query = " SELECT count(*) cnt" +
							   " FROM (SELECT ROWNUM AS RN, A.* " +
							         " FROM (SELECT b.no, b.title, b.hit, b.reg_date, b.user_no, u.name, b.content " +
									       " FROM board b, users u " +
							               " where b.user_no = u.no " +
							               " ORDER BY NO DESC " +
							         " ) A " +
							          " where name like ? " +
							   " ) ";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, "%"+kwd+"%");
				
				} else if(field.equals("reg_date")) {
					String query = " SELECT count(*) cnt" +
							   " FROM (SELECT ROWNUM AS RN, A.* " +
							         " FROM (SELECT b.no, b.title, b.hit, b.reg_date, b.user_no, u.name, b.content " +
									       " FROM board b, users u " +
							               " where b.user_no = u.no " +
							               " ORDER BY NO DESC " +
							         " ) A " +
							          " where reg_date like ? " +
							   " ) ";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, "%"+kwd+"%");
				
				} else if(field.equals("file")) {
						String query = " SELECT count(*) " +
							   " FROM (SELECT ROWNUM AS RN, A.* " +
							         " FROM (SELECT b.no, b.title, b.hit, b.reg_date, b.user_no, u.name, b.content " +
									       " FROM board b, users u " +
							               " where b.user_no = u.no and fileName1 like ? or fileName2 like ?" +
							               " ORDER BY NO DESC " +
							         " ) A " +
							   " ) ";
					pstmt = conn.prepareStatement(query);
					pstmt.setString(1, "%"+kwd+"%");
					pstmt.setString(2, "%"+kwd+"%");
				}
			
			rs=pstmt.executeQuery();
			
			}
				// 4.결과처리
				if (rs.next()) {
					cnt = rs.getInt("cnt");
				}
				
			} catch (SQLException e) {
				System.out.println("error:" + e);
			} finally {
				// 5. 자원정리
				try {
					if (pstmt != null) {
						pstmt.close();
					}
					if (conn != null) {
						conn.close();
					}
			} catch (SQLException e) {
					System.out.println("error:" + e);
		}

	}
		return cnt;

	}
		
}
	

