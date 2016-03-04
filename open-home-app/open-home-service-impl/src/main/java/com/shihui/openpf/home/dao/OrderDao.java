package com.shihui.openpf.home.dao;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.shihui.api.order.common.enums.OrderStatusEnum;
import com.shihui.openpf.home.model.Order;

/**
 * Created by zhoutc on 2016/1/21.
 */
@Repository
public class OrderDao extends AbstractDao<Order> {
    private Logger log = LoggerFactory.getLogger(getClass());

    public List<Order> queryOrder(Order order , String startTime, String endTime, int page , int size) {
        StringBuilder sql = new StringBuilder("select * from `order` where 1 = 1 ");

        Field[] fields = Order.class.getDeclaredFields();
        try {
            ArrayList<Object> valus = new ArrayList<Object>();
            for (Field field : fields) {
                String fieldName = this.fieldNameMap.get(field.getName());
                if(fieldName == null){
                    fieldName = this.idsFieldNameMap.get(field.getName());
                }
                if (fieldName == null) {
                    continue;
                }
                field.setAccessible(true);
                Transient transientAno = field.getAnnotation(Transient.class);
                if (transientAno != null) {
                    continue;
                }
                Object value = field.get(order);
                if(value!=null) {
                    sql.append("and ").append(fieldName).append(" = ? ");
                    valus.add(value);
                }

            }
            if(startTime!=null&&!startTime.equals("")) {
                sql.append("and create_time >= ? ");
                valus.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime));
            }
            if(endTime!=null&&!endTime.equals("")) {
                sql.append("and create_time <= ? ");
                valus.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime));
            }

            sql.append("limit ").append((page-1)*size).append(",").append(size);
            return super.queryForList(sql.toString(), valus.toArray());
        } catch (Exception e) {
            log.error("OrderDao error!!",e);
        }
        return null;
    }

    public int countQueryOrder(Order order, String startTime, String endTime) {
        StringBuilder sql = new StringBuilder("select count(*) from `order` where 1 = 1 ");
        Field[] fields = Order.class.getDeclaredFields();
        try {
            ArrayList<Object> valus = new ArrayList<Object>();
            for (Field field : fields) {
                String fieldName = this.fieldNameMap.get(field.getName());
                if(fieldName == null){
                    fieldName = this.idsFieldNameMap.get(field.getName());
                }
                if (fieldName == null) {
                    continue;
                }
                field.setAccessible(true);
                Transient transientAno = field.getAnnotation(Transient.class);
                if (transientAno != null) {
                    continue;
                }
                Object value = field.get(order);

                if(value!=null) {
                    sql.append("and ").append(fieldName).append(" = ? ");
                    valus.add(value);
                }
            }
            if(startTime!=null&&!startTime.equals("")) {
                sql.append("and create_time >= ? ");
                valus.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime));
            }
            if(endTime!=null&&!endTime.equals("")) {
                sql.append("and create_time <= ? ");
                valus.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime));
            }
            return jdbcTemplate.queryForInt(sql.toString(), valus.toArray());
        } catch (Exception e) {
            log.error("OrderDao error!!",e);
        }
        return -1;
    }

    /**
     * 根据订单ID查询订单
     * @param orderId    订单Id
     * @return 订单详情
     */
    public Order queryOrder(long orderId){
        String sql = "select * from `order` where order_id = ?";
        try {
            return super.queryForObject(sql,orderId);
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
        String sql = "select count(*) from `order` where order_status = ?";
        return super.queryCount(sql, OrderStatusEnum.OrderRefunding.getValue());
    }

    /**
     * 查询异常订单数量
     *
     * @return 异常订单数量
     */
    public List<Order> queryUnusual(){
        String sql = "select * from `order` where order_status = ?";
        return super.queryForList(sql, OrderStatusEnum.OrderRefunding.getValue());
    }

}
