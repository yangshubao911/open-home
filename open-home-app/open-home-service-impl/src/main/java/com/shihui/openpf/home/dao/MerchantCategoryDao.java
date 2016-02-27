package com.shihui.openpf.home.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.shihui.openpf.home.model.MerchantCategory;

/**
 * Created by zhoutc on 2016/1/27.
 */
@Repository
public class MerchantCategoryDao extends AbstractDao<MerchantCategory> {

 public int batchSave(List<MerchantCategory> merchantCategorys) throws SQLException{
	 String sql = "insert into merchant_category(merchant_id, category_id, status, service_id) values(?,?,?,?)";
	 DataSource dataSource = this.jdbcTemplate.getDataSource();
	 int result = 0;
	 try (Connection conn = dataSource.getConnection()){
		 conn.setAutoCommit(false);
		 try (PreparedStatement ps = conn.prepareStatement(sql)){
			 for(MerchantCategory cate : merchantCategorys){
				 ps.setInt(1, cate.getMerchantId());
				 ps.setInt(2, cate.getCategoryId());
				 ps.setInt(3, cate.getStatus());
				 ps.setInt(4, cate.getServiceId());
				 
				 ps.addBatch();
			 }
			 int[] resultarr = ps.executeBatch();
			 
			 for(int i=0; i<resultarr.length; i++){
				 result += resultarr[i];
			 }
			 conn.commit();
		 }catch(SQLException e){
			 conn.rollback();
			 throw e;
		 }finally{
			 conn.setAutoCommit(true);
		 }
		 return result;
	} catch (SQLException e) {
		throw e;
	}
 }

	public List<Integer> queryAvailableMerchantId(int categoryId, int serviceId) {
		String sql = "select * from merchant_category where category_id = ? and service_id = ?";
		return jdbcTemplate.queryForList(sql, new Object[]{categoryId, serviceId});

	}

}
