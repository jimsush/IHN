package com.ihn.server.internal.parking.dao.impl;

import java.sql.Types;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.ihn.server.internal.parking.dao.ParkingDao;
import com.ihn.server.internal.parking.model.PropertyAsset;

public class ParkingDaoImpl implements ParkingDao {

	private JdbcTemplate jdbcTemplate;
	private PropertyAssetMapper propertyAssetMapper=new PropertyAssetMapper();
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public PropertyAsset getByKey(String propertyAssetId) {
		List objs = jdbcTemplate.query("select * from propertyasset where id=?", new Object[]{propertyAssetId}, new int[]{Types.VARCHAR}, this.propertyAssetMapper);
		if(objs==null || objs.size()==0)
			return null;
		return (PropertyAsset)objs.get(0);
	}
	
}
