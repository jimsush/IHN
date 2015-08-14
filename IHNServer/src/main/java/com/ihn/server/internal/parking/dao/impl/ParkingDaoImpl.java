package com.ihn.server.internal.parking.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;

import com.ihn.server.internal.parking.dao.ParkingDao;

public class ParkingDaoImpl implements ParkingDao {

	private JdbcTemplate jdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
}
