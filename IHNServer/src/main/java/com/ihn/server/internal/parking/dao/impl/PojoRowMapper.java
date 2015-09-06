package com.ihn.server.internal.parking.dao.impl;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ihn.server.util.Consts;

public class PojoRowMapper implements RowMapper {

	public Class<?> clazz;
	
	public PojoRowMapper(Class<?> clazz){
		this.clazz=clazz;
	}
	
	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		try{
			Object entity = clazz.newInstance();
			
			Field[] fields = clazz.getDeclaredFields();
			for(Field field : fields){
				Object val = rs.getObject(field.getName());
				if(val!=null){
					field.setAccessible(true);
					field.set(entity, val);
				}else{
					Class<?> fclz=field.getType();
					if(fclz.equals(Double.class)){
						field.setAccessible(true);
						field.set(entity, Double.valueOf(Consts.MAGIC_NULL));
					}else if(fclz.equals(Integer.class)){
						field.setAccessible(true);
						field.set(entity, Consts.MAGIC_NULL);
					}
				}
			}
			
			return entity;
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
		
	}

}
