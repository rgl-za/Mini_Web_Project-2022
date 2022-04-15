package com.kosta.jhj.dao;

import java.util.List;

import com.kosta.jhj.vo.GuestbookVo;

import java.sql.Connection;

public interface GuestbookDao {
  
	public List<GuestbookVo> getList();

	public int insert(GuestbookVo vo);

	public int delete(GuestbookVo vo);

}
