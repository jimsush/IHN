package com.ihn.server.internal.parking.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ihn.server.internal.parking.model.PropertyAsset;

public class PropertyAssetMapper implements RowMapper {

	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		PropertyAsset asset=new PropertyAsset();
		asset.setId(rs.getString("id"));
		asset.setCity(rs.getString("city"));
		asset.setName(rs.getString("name"));
		asset.setCorp(rs.getString("corp"));
		asset.setAddress(rs.getString("address"));
		asset.setLatitude(rs.getDouble("latitude"));
		asset.setLongitude(rs.getDouble("longitude"));
		return asset;
	}

}
