package com.shihui.openpf.home.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * The persistent class for the order database table.
 *
 */
@Entity(name="order_history")
public class OrderHistory {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="history_id")
    private long historyId;

    @Column(name="order_id")
    private long order_id;

    @Column(name="change_time")
    private Date change_time;

    @Column(name="order_status")
    private int order_status;

    public long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(long historyId) {
        this.historyId = historyId;
    }

    public long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public Date getChange_time() {
        return change_time;
    }

    public void setChange_time(Date change_time) {
        this.change_time = change_time;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }
}
