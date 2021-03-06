/**
 * 
 */
package com.shihui.openpf.home.dao;

import com.shihui.openpf.home.model.Category;
import org.springframework.stereotype.Repository;

/**
 * @author zhouqisheng
 *
 * @version 1.0 Created at: 2016年1月21日 下午4:54:38
 */
@Repository
public class CategoryDao extends AbstractDao<Category> {
    public int querySold(long categoryId){
        String sql="select sold from category where id=?";
        return super.jdbcTemplate.queryForInt(sql,new Object[] {categoryId});
    }
}
