package com.kosta.jhj.vo;

public class BoardVo {

	private int no;
	private String title;
	private String content;
	private int hit;
	private String regDate;
	private int userNo;
	private String userName;
	private String fileName1;
	private String fileName2;
	
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getHit() {
		return hit;
	}
	public void setHit(int hit) {
		this.hit = hit;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public int getUserNo() {
		return userNo;
	}
	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFileName1() {
		return fileName1;
	}
	public void setFileName1(String fileName1) {
		this.fileName1 = fileName1;
	}
	public String getFileName2() {
		return fileName2;
	}
	public void setFileName2(String fileName2) {
		this.fileName2 = fileName2;
	}
	
	public BoardVo() {
	}
	
	public BoardVo(int no, String title, String content) {
		this.no = no;		
		this.title = title;
		this.content = content;
	}

	public BoardVo(String title, String content, int userNo) {
		this.title = title;
		this.content = content;
		this.userNo = userNo;
	}
	
	public BoardVo(String title, String content, int userNo, String fileName1, String fileName2) {
		this.title = title;
		this.content = content;
		this.userNo = userNo;
		this.fileName1 = fileName1;
		this.fileName2 = fileName2;
	}
	
	public BoardVo(int no, String title, int hit, String regDate, int userNo, String userName) {
		this.no = no;
		this.title = title;
		this.hit = hit;
		this.regDate = regDate;
		this.userNo = userNo;
		this.userName = userName;
	}
	
	public BoardVo(int no, String title, String content, int hit, String regDate, int userNo, String userName) {
		this(no, title, hit, regDate, userNo, userName);
		this.content = content;
	}
	
	public BoardVo(int no, String title, String content, int hit, String regDate, int userNo, String userName, String fileName1, String fileName2) {
		this.no = no;
		this.title = title;
		this.hit = hit;
		this.regDate = regDate;
		this.userNo = userNo;
		this.userName = userName;
		this.content = content;
		this.fileName1 = fileName1;
		this.fileName2 = fileName2;
	}
	
	@Override
	public String toString() {
		return "BoardVo [no=" + no + ", title=" + title + ", content=" + content + ", hit=" + hit + ", regDate="
				+ regDate + ", userNo=" + userNo + ", userName=" + userName + ", fileName1=" + fileName1
				+ ", fileName2=" + fileName2 + "]";
	}

}
