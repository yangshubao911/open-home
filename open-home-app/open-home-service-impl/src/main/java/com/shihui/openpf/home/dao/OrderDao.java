package com.shihui.openpf.home.dao;

import com.shihui.openpf.home.model.Order;
import org.springframework.stereotype.Repository;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhoutc on 2016/1/21.
 */
@Repository
public class OrderDao extends AbstractDao<Order> {

    public List<Order> queryOrder(Order order , int page , int size) {
        StringBuilder sql = new StringBuilder("select * from home_order where 1 = 1 ");
        Field[] fields = Order.class.getDeclaredFields();
        try {
            ArrayList<Object> valus = new ArrayList<Object>();
            for (Field field : fields) {
                field.setAccessible(true);
                Transient transientAno = field.getAnnotation(Transient.class);
                if (transientAno != null) {
                    continue;
                }
                Object value = field.get(order);
                String fieldName = null;
                Column columnAno = field.getAnnotation(Column.class);
                if (columnAno != null)
                    fieldName = columnAno.name();
                if (fieldName == null)
                    fieldName = field.getName();
                sql.append("and ").append(fieldName).append(" = ? ");
                valus.add(value);

            }
            sql.append("limit ").append(page*size).append(",").append(size);
            return super.queryForList(sql.toString(), valus.toArray(), Order.class);
        } catch (Exception e) {
        }
        return null;
    }

    public int countQueryOrder(Order order) {
        StringBuilder sql = new StringBuilder("select * from home_order where 1 = 1 ");
        Field[] fields = Order.class.getDeclaredFields();
        try {
            ArrayList<Object> valus = new ArrayList<Object>();
            for (Field field : fields) {
                field.setAccessible(true);
                Transient transientAno = field.getAnnotation(Transient.class);
                if (transientAno != null) {
                    continue;
                }
                Object value = field.get(order);
                String fieldName = null;
                Column columnAno = field.getAnnotation(Column.class);
                if (columnAno != null)
                    fieldName = columnAno.name();
                if (fieldName == null)
                    fieldName = field.getName();
                sql.append("and ").append(fieldName).append(" = ? ");
                valus.add(value);
            }
            return jdbcTemplate.queryForInt(sql.toString());
        } catch (Exception e) {

        }
        return -1;
    }
}
