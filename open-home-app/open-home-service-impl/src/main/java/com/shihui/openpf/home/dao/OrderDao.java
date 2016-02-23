package com.shihui.openpf.home.dao;

import com.shihui.api.common.model.OrderStatusEnum;
import com.shihui.openpf.home.model.Order;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhoutc on 2016/1/21.
 */
@Repository
public class OrderDao extends AbstractDao<Order> {

    public List<Order> queryOrder(Order order , String startTime, String endTime, int page , int size) {
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
            if(startTime!=null||!startTime.equals("")) {
                sql.append("and create_time >= ? ");
                valus.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime));
            }
            if(endTime!=null||!endTime.equals("")) {
                sql.append("and create_time <= ? ");
                valus.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime));
            }

            sql.append("limit ").append(page*size).append(",").append(size);
            return super.queryForList(sql.toString(), valus.toArray(), Order.class);
        } catch (Exception e) {
        }
        return null;
    }

    public int countQueryOrder(Order order, String startTime, String endTime) {
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
                if(value!=null) {
                    sql.append("and ").append(fieldName).append(" = ? ");
                    valus.add(value);
                }
            }
            if(startTime!=null||!startTime.equals("")) {
                sql.append("and create_time >= ? ");
                valus.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime));
            }
            if(endTime!=null||!endTime.equals("")) {
                sql.append("and create_time <= ? ");
                valus.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime));
            }
            return jdbcTemplate.queryForInt(sql.toString(), valus.toArray());
        } catch (Exception e) {

        }
        return -1;
    }

    /**
     * 根据订单ID查询订单
     * @param orderId    订单Id
     * @return 订单详情
     */
    public Order queryOrder(long orderId){
        String sql = "select * from order where order_id = " + orderId;
        try {
            return super.queryForObject(sql, Order.class);
        }catch (Exception e){

        }
        return  null;
    }

    /**
     * 查询异常订单数量
     *
     * @return 异常订单数量
     */
    public int countUnusual(){
        String sql = "select count(*) from order where order_status = ?";
        return super.queryCount(sql, OrderStatusEnum.OrderRefunding.getValue());
    }

    /**
     * 查询异常订单数量
     *
     * @return 异常订单数量
     */
    public List<Order> queryUnusual(){
        String sql = "select * from order where order_status = ?";
        return super.queryForList(sql, OrderStatusEnum.OrderRefunding.getValue());
    }

}
