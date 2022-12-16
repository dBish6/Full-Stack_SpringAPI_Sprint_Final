package com.keyin.finalSprint.order.service;

import com.keyin.finalSprint.order_detail.model.OrderDetail;
import com.keyin.finalSprint.order_detail.respository.OrderDetailRepository;
import com.keyin.finalSprint.order.model.Order;
import com.keyin.finalSprint.order.respository.OrderRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private OrderDetailRepository orderDetailsRepo;

    public void savePostRequestToDb(String file){
        try {
            JSONObject order = new JSONObject(file);

            Order thisOrder = createOrdersObject(order);

            // Saving Order Details to Order Table
            orderRepo.save(thisOrder);

            // Creating JSON Array for Order Details
            JSONArray orderDetails = createOrderDetailsJsonArray(order);

            for(int i = 0; i < orderDetails.length(); i++){
                // Getting each item for order
                // Creating an Order Details object and saving to Order Details Table
                OrderDetail orderDetailItem = createOrderDetailsObject(thisOrder, orderDetails, i);

                // Saving Order Details to OrderDetails table
                orderDetailsRepo.save(orderDetailItem);
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private static OrderDetail createOrderDetailsObject(Order thisOrder, JSONArray orderDetails, int i) throws JSONException {
        // Creates the OrderDetails Object and Returns
        OrderDetail orderDetailItem = new OrderDetail();

        orderDetailItem.setQuantity((Integer) orderDetails.getJSONObject(i).get("quantity"));
        orderDetailItem.setSword_id((Integer) orderDetails.getJSONObject(i).get("sword_id"));
        orderDetailItem.setUnit_price((Double) orderDetails.getJSONObject(i).get("unit_price"));
        orderDetailItem.setOrders(thisOrder);
        return orderDetailItem;
    }

    private static JSONArray createOrderDetailsJsonArray(JSONObject order) throws JSONException {
        // Creates & Returns orderDetails JSONArray
        JSONArray orderDetails = (JSONArray) order.get("order_details");
        return orderDetails;
    }

    private static Order createOrdersObject(JSONObject order) throws JSONException {
        // Creating the Orders Object for this order
        Order thisOrder = new Order();

        thisOrder.setTax_rate((Integer) order.get("tax_rate"));
        thisOrder.setOrder_subtotal((Double) order.get("order_subtotal"));
        thisOrder.setOrder_total((Double) order.get("order_total"));
        return thisOrder;
    }

}