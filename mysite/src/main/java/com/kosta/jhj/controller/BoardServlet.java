package com.kosta.jhj.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.kosta.jhj.dao.BoardDao;
import com.kosta.jhj.dao.BoardDaoImpl;
import com.kosta.jhj.util.WebUtil;
import com.kosta.jhj.vo.BoardVo;
import com.kosta.jhj.vo.UserVo;

@WebServlet("/board")
public class BoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String ATTACHES_DIR = "/Users/jihyeonjeong/Developer/mysite/src/main/webapp/assets/attaches";
	private static final int LIMIT_SIZE_BYTES = 1024 * 1024;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String actionName = request.getParameter("a");
		System.out.println("board:" + actionName);

		if ("list".equals(actionName)) {
			// 리스트 가져오기
//			BoardDao dao = new BoardDaoImpl();
//			List<BoardVo> list = dao.getList();
//
//			System.out.println(list.toString());
//
//			// 리스트 화면에 보내기
//			request.setAttribute("list", list);
//			WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");
			
			
			// 페이징 된 상태에서 리스트 받아오기 
			BoardDaoImpl dao = new BoardDaoImpl();
			String strPg = request.getParameter("pg"); // a=list&pg=?
			
			System.out.println(strPg); 
			
			int rowSize = 10; // 한 페이지에 보여 줄 글의 수
			int pg = 1; // 페이지, 초기값=1
			
			if(strPg != null){ // ex: a=list&pg=2
				pg = Integer.parseInt(strPg); // 저장
			}
			
			int from = (pg * rowSize) - (rowSize-1); // (1*10)-(10-1)=10-9=1 (from)
			int to = (pg * rowSize); // (1*10)=10 (to)
			
			List<BoardVo> list = dao.getList(from, to);
			
			int total = dao.getTotal(); // 게시물 총 개수
			int allPage = (int) Math.ceil(total/(double)rowSize); // 페이지 수
			int block = 3; // 한 페이지에 보여줄 범위
			
			System.out.println("전체 페이지수 : "+allPage);
			System.out.println("현재 페이지 : "+ strPg);
			 
			int fromPage = ((pg-1)/block*block)+1;  //보여줄 페이지의 시작
			int toPage = ((pg-1)/block*block)+block; //보여줄 페이지의 끝
			if(toPage> allPage){ // 예) 20>17
				toPage = allPage;
			}
			   
			System.out.println("페이지시작 : "+fromPage+ " / 페이지 끝 :"+toPage);    
			
			request.setAttribute("pg", pg);
			request.setAttribute("toPage", toPage);
			request.setAttribute("block", block);
			request.setAttribute("allPage", allPage);
			request.setAttribute("fromPage", fromPage);
			
			request.setAttribute("list", list);
			
			WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");
			
		} else if ("read".equals(actionName)) {
			// 게시물 가져오기
			int no = Integer.parseInt(request.getParameter("no"));
			BoardDao dao = new BoardDaoImpl();
			BoardVo boardVo = dao.getBoard(no);
			
			// 조회수 증가
			BoardDao dao2 = new BoardDaoImpl();
			dao2.countUpdate(no);
			System.out.println(boardVo.toString());

			// 게시물 화면에 보내기
			request.setAttribute("boardVo", boardVo);
			WebUtil.forward(request, response, "/WEB-INF/views/board/view.jsp");
		} else if ("modifyform".equals(actionName)) {
			// 게시물 가져오기
			int no = Integer.parseInt(request.getParameter("no"));
			BoardDao dao = new BoardDaoImpl();
			BoardVo boardVo = dao.getBoard(no);

			// 게시물 화면에 보내기
			request.setAttribute("boardVo", boardVo);
			WebUtil.forward(request, response, "/WEB-INF/views/board/modify.jsp");
		} else if ("modify".equals(actionName)) {
			// 게시물 가져오기
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			int no = Integer.parseInt(request.getParameter("no"));
			
			BoardVo vo = new BoardVo(no, title, content);
			BoardDao dao = new BoardDaoImpl();
			
			dao.update(vo);
			
			WebUtil.redirect(request, response, "/mysite/board?a=list");
		} else if ("writeform".equals(actionName)) {
			// 로그인 여부체크
			UserVo authUser = getAuthUser(request);
			if (authUser != null) { // 로그인했으면 작성페이지로
				WebUtil.forward(request, response, "/WEB-INF/views/board/write.jsp");
			} else { // 로그인 안했으면 리스트로
				WebUtil.redirect(request, response, "/mysite/board?a=list");
			}

		} else if ("write".equals(actionName)) {
			// 파일 처리
			UserVo authUser = getAuthUser(request);
			response.setContentType("text/html; charset=UTF-8");
			request.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			
			File attachesDir = new File(ATTACHES_DIR);
			
			DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
			// DiskFileItemFactory: 업로드된 파일을 저장할 저장소와 관련된 클래스
			
			fileItemFactory.setRepository(attachesDir);
			// setRepository()메서드는 업로드 된 파일을 저장할 위치를 File객체로 지정
			
			fileItemFactory.setSizeThreshold(LIMIT_SIZE_BYTES);
			// setSizeThreshold()메서드는 저장소에 임시파일을 생성할 한계 크기를 byte단위로 지정

			ServletFileUpload fileUpload = new ServletFileUpload(fileItemFactory);
			// ServletFileUpload 클래스: HTTP 요청에 대한 HttpServletRequest 객체로부터 
			// 						   multipart/form-data형식으로 넘어온 HTTP Body 부분을 다루기 쉽게 변환(parse)해주는 역할을 수행

			String title=null, content=null, fileName1=null, fileName2=null;
			int userNo = authUser.getNo();
			
			try {
				List<FileItem> items = fileUpload.parseRequest(request);
				// parseRequest()메서드를 수행시 FileItem이라는 형식으로 변환
				// FileItem: 사용자가 업로드한 File 데이터나 사용자가 input text에 입력한 일반 요청 데이터에 대한 객
				for(FileItem item: items) {
					if(item.isFormField()) { // 리턴 값이 true: text와 같은 일반 입력 데이터, 리턴 값이 false: 파일 데이터
						// 리턴 값이 true인 경우
						System.out.printf("파라미터 명: %s, 파라미터 값: %s\n", item.getFieldName(), item.getString("utf-8"));
						if(item.getFieldName().equals("title")) {
							 title = item.getString("utf-8");
						} else if(item.getFieldName().equals("content")) {
							content = item.getString("utf-8");
						}
						
					} else { 
						// 리턴 값이 false인 경우 
						System.out.printf("파라미터 명: %s, 파라미터 값: %s, 파일 크기: %s bytes \n", item.getFieldName(), item.getName(), item.getSize());
						if(item.getSize() > 0) {
							String separator = File.separator;
							// File.separator: 운영체제별로 다른 파일 경로 구분자
							int index = item.getName().lastIndexOf(separator);
							String fileName = item.getName().substring(index + 1);
							File uploadFile = new File(ATTACHES_DIR + separator + fileName);
							item.write(uploadFile); // 업로드된 파일을 저장소 디렉터리에 저장
							
							if(item.getFieldName().equals("file1")){
								fileName1 = item.getName();
							} else if(item.getFieldName().equals("file2")){
								fileName2 = item.getName();
							}
						}
						
					}
					
				}
				System.out.println("upload sucess");
				
			} catch(Exception e) {
				e.printStackTrace();
				System.out.println("upload error");
			}
			
			BoardVo vo = new BoardVo(title, content, userNo, fileName1, fileName2);
			BoardDao dao = new BoardDaoImpl();
			dao.insert(vo);
			
			
			WebUtil.redirect(request, response, "/mysite/board?a=list");

		} else if ("delete".equals(actionName)) {
			int no = Integer.parseInt(request.getParameter("no"));

			BoardDao dao = new BoardDaoImpl();
			dao.delete(no);

			WebUtil.redirect(request, response, "/mysite/board?a=list");

		} else if("search".equals(actionName)) {
			// 검색 결과 리스트에 페이징 처리 
			BoardDao dao = new BoardDaoImpl();
			String strPg = request.getParameter("pg"); // a=list&pg=?
			
			int rowSize = 10; // 한 페이지에 보여줄 글의 수 
			int pg = 1; // 페이지, 초기값=1
			
			if(strPg != null){ // ex: a=list&pg=2
				pg = Integer.parseInt(strPg); // 저장
			}
			
			int from = (pg * rowSize) - (rowSize-1); // (1*10)-(10-1)=10-9=1 (from)
			int to = (pg * rowSize); // (1*10)=10 (to)
			List<BoardVo> list = dao.getSearch(request.getParameter("field"),request.getParameter("kwd"), from, to);
			
			System.out.println(list.toString());
//			System.out.println(list.size());

//			request.setAttribute("list", list);
//			WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");
			
			
//			BoardDaoImpl dao2 = new BoardDaoImpl();
			System.out.println(request.getParameter("field"));
			System.out.println(request.getParameter("kwd"));
			
			int total = dao.getTotal(request.getParameter("field"),request.getParameter("kwd")); // 검색 조건에 해당하는 페이지 총 갯수 
			int allPage = (int) Math.ceil(total/(double)rowSize); // 페이지 수 
			int block = 3; // 한 페이지에 보여줄 범위
			
			System.out.println("전체 페이지수 : "+allPage);
			System.out.println("현재 페이지 : "+ strPg);
			 
			int fromPage = ((pg-1)/block*block)+1;  //보여줄 페이지의 시작
			int toPage = ((pg-1)/block*block)+block; //보여줄 페이지의 끝
			if(toPage> allPage){ // 예) 20>17
				toPage = allPage;
			}
			   
			System.out.println("페이지시작 : "+fromPage+ " / 페이지 끝 :"+toPage);    
			
			request.setAttribute("pg", pg);
			request.setAttribute("toPage", toPage);
			request.setAttribute("block", block);
			request.setAttribute("allPage", allPage);
			request.setAttribute("fromPage", fromPage);
			
			request.setAttribute("a", "search");
			request.setAttribute("field", request.getParameter("field"));
			request.setAttribute("kwd", request.getParameter("kwd"));
			
			request.setAttribute("list", list);
			
			WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");
			
			
			
		} else if("download".equals(actionName)) {
			// 파일 다운로드 처리
			String fileName = request.getParameter("filename");
	         System.out.println(ATTACHES_DIR + File.separator + fileName);
	         File file = new File(ATTACHES_DIR + File.separator + fileName);
	         if(file.exists()) {
	            OutputStream os = null;
	            FileInputStream is = null;
	            
	            response.setContentType("application/octet-stream");
	            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ";");
	  
	            try {
	               os = response.getOutputStream();
	               is = new FileInputStream(file);
	               int su = 0;
	               while((su = is.read()) != -1) {
	                  os.write(su);
	               }
	               System.out.println("down");
	            }catch(IOException e) {
	               System.out.println("error");
	               e.printStackTrace();
	            }finally {
	               try {
	                  if(os != null) os.close();
	                  if(is != null) is.close();
	               }catch (IOException e) {
	                  e.printStackTrace();
	               }
	            }
	         }

	      } else {
	         WebUtil.redirect(request, response, "/mysite/board?a=list");
	    }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	// 로그인 되어 있는 정보를 가져온다.
	protected UserVo getAuthUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserVo authUser = (UserVo) session.getAttribute("authUser");

		return authUser;
	}

}
