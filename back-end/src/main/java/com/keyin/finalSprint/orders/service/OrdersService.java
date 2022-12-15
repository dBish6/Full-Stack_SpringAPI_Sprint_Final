package com.keyin.finalSprint.orders.service;

import com.keyin.finalSprint.order_details.model.OrderDetails;
import com.keyin.finalSprint.order_details.respository.OrderDetailsRepository;
import com.keyin.finalSprint.orders.model.Orders;
import com.keyin.finalSprint.orders.respository.OrdersRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrdersService {

    @Autowired
    private OrdersRepository orderRepo;

    @Autowired
    private OrderDetailsRepository orderDetailsRepo;

    public void savePostRequestToDb(String file){
        try {
            JSONObject order = new JSONObject(file);

            // Creating the orders object for this order
            Orders thisOrder = new Orders();

            thisOrder.setTax_rate((Integer) order.get("tax_rate"));
            thisOrder.setOrder_subtotal((Double) order.get("order_subtotal"));
            thisOrder.setOrder_total((Double) order.get("order_total"));

            // Saving Order Details to Order Table
            orderRepo.save(thisOrder);

            saveOrderDetailsItems(order, thisOrder);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }

    private void saveOrderDetailsItems(JSONObject order, Orders thisOrder) throws JSONException {
        // Creating JSON Array for Order Details
        JSONArray orderDetails = (JSONArray) order.get("order_details");

        for(int i = 0; i < orderDetails.length(); i++){
            // Getting each item for order
            // Creating an Order Details object and saving to Order Details Table
            OrderDetails orderDetailsItem = new OrderDetails();

            orderDetailsItem.setQuantity((Integer) orderDetails.getJSONObject(i).get("quantity"));
            orderDetailsItem.setSword_id((Integer) orderDetails.getJSONObject(i).get("sword_id"));
            orderDetailsItem.setUnit_price((Double) orderDetails.getJSONObject(i).get("unit_price"));
            orderDetailsItem.setOrders(thisOrder);

            // Saving Order Details to OrderDetails table
            orderDetailsRepo.save(orderDetailsItem);
        }
    }

}
