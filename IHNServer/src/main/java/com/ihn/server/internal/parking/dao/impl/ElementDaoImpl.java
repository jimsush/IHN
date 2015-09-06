package com.ihn.server.internal.parking.dao.impl;

import java.sql.Types;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.ihn.server.internal.parking.dao.ElementDao;
import com.ihn.server.internal.parking.model.Element;

public class ElementDaoImpl implements ElementDao {

	private JdbcTemplate jdbcTemplate;
	private RowMapper rowMapper=new PojoRowMapper(Element.class);
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Element> getElementsByParkId(String parkId, String category) {
		List objs = null;
		if(category==null || category.length()==0){
			objs=jdbcTemplate.query("select * from element where parkid=?", new Object[]{parkId}, new int[]{Types.VARCHAR}, this.rowMapper);
		}else{
			objs=jdbcTemplate.query("select * from element where parkid=? and category=?", new Object[]{parkId, category}, new int[]{Types.VARCHAR, Types.VARCHAR}, this.rowMapper);
		}
		if(objs==null || objs.size()==0)
			return null;
		
		return objs;
	}

	
	
}
