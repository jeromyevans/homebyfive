package com.blueskyminds.homebyfive.business.pricing;

import com.blueskyminds.homebyfive.business.Enterprise;
import com.blueskyminds.homebyfive.business.Schedule;
import com.blueskyminds.homebyfive.framework.core.journal.Journal;

import javax.persistence.*;
import java.io.PrintStream;
import java.util.List;

/**
 *
 * A list of orders in an enterprise
 *
 * Date Started: 14/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
public class ScheduleOfOrders extends Schedule<Order>  {

    /** Create a new schedule of orders */
    public ScheduleOfOrders(Enterprise enterprise, Journal journal) {
        super(enterprise, journal);
    }

    /** Default constructor for ORM */
    protected ScheduleOfOrders() {

    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create the specified order
     * @return the order if added successfully, otherwise null
     */
    public Order createOrder(Order order) {
        return super.create(order);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Deletes the specified order.
     * The order can only be deleted if it has not been actioned.
     *
     * @param order
     * @return true if deleted successfully
     */
    public boolean deleteOrder(Order order) {

        boolean deleted;

        if (!order.isActioned()) {
            // mark as deleted
            order.delete();
        }

        deleted = super.delete(order);

        return deleted;
    }

    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="ScheduleOfOrdersEntry",
            joinColumns=@JoinColumn(name="ScheduleOfOrdersId"),
            inverseJoinColumns = @JoinColumn(name="OrderId")
    )
    protected List<Order> getOrders() {
        return super.getDomainObjects();
    }

    protected void setOrders(List<Order> orders) {
        super.setDomainObjects(orders);
    }

    // ------------------------------------------------------------------------------------------------------

    @Override
    @OneToOne()
    @JoinColumn(name="EnterpriseId")
    public Enterprise getEnterprise() {
        return super.getEnterprise();
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    public void print(PrintStream out) {
        out.println("--- Schedule of Orders ---");
        for (Order order : getOrders()) {
            order.print(out);
        }
    }
}
