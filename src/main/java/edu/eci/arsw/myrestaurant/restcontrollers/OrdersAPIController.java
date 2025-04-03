/*
 * Copyright (C) 2016 Pivotal Software, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.eci.arsw.myrestaurant.restcontrollers;

import java.util.Set;
 
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
import edu.eci.arsw.myrestaurant.beans.BillCalculator;
import edu.eci.arsw.myrestaurant.model.Order;
import edu.eci.arsw.myrestaurant.model.RestaurantProduct;
import edu.eci.arsw.myrestaurant.services.RestaurantOrderServices;
 
@RestController
@RequestMapping(value = "/orders")
public class OrdersAPIController {
    @Autowired
    private RestaurantOrderServices orderServices;
    @Autowired
    @Qualifier("basicBillCalculator")
    private BillCalculator billCalculator;
 
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllOrders() {
        try {
            Set<Integer> tables = orderServices.getTablesWithOrders();
            if (tables.isEmpty()) {
                return new ResponseEntity<>("No hay órdenes registradas", HttpStatus.NO_CONTENT);
            }
 
            JSONArray ordersArray = new JSONArray();
            for (Integer table : tables) {
                Order order = orderServices.getTableOrder(table);
                JSONObject orderJson = new JSONObject();
                // Add table information
                orderJson.put("mesa", table);
                // Add products information
                JSONArray productsArray = new JSONArray();
                for (String productName : order.getOrderedDishes()) {
                    JSONObject productJson = new JSONObject();
                    RestaurantProduct product = orderServices.getProductByName(productName);
                    productJson.put("producto", productName);
                    productJson.put("cantidad", order.getDishOrderedAmount(productName));
                    productJson.put("precio", product.getPrice());
                    productsArray.put(productJson);
                }
                orderJson.put("productos", productsArray);
                // Calculate and add total using BasicBillCalculator
                int total = orderServices.calculateTableBill(table);
                orderJson.put("valorTotal", total);
                ordersArray.put(orderJson);
            }
            return new ResponseEntity<>(ordersArray.toString(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al procesar las órdenes: " + e.getMessage(), 
                                     HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
