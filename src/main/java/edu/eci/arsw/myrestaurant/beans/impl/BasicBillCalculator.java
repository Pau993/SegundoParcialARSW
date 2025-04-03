package edu.eci.arsw.myrestaurant.beans.impl;

import edu.eci.arsw.myrestaurant.model.Order;
import edu.eci.arsw.myrestaurant.model.RestaurantProduct;
import edu.eci.arsw.myrestaurant.beans.BillCalculator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.web.bindery.requestfactory.shared.Service;


@Component("basicBillCalculator") // Define the bean with the qualifier "basicBillCalculator"
public class BasicBillCalculator implements BillCalculator {

    @Override
    public int calculateBill(Order o, Map<String, RestaurantProduct> productsMap) {
        int total = 0;
        for (Map.Entry<String, Integer> entry : o.getOrderAmountsMap().entrySet()) {
            String productName = entry.getKey();
            int quantity = entry.getValue();
            RestaurantProduct product = productsMap.get(productName);
            if (product != null) {
                total += product.getPrice() * quantity;
            }
        }
        return total;
    }
}