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
     RestaurantOrderServices ros;
  
     @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
     public ResponseEntity<?> allOrders() {
         try {
             if (ros.getTablesWithOrders().isEmpty()) {
                 return new ResponseEntity<>("No hay órdenes registradas", HttpStatus.NO_CONTENT);
             }
             JSONArray jsonArray = new JSONArray();
             for (Integer orderId : ros.getTablesWithOrders()) {
                 JSONObject json = new JSONObject(ros.getTableOrder(orderId));
                 json.put("mesa", orderId);
                 json.put("valorTotal", ros.calculateTableBill(orderId));
                 jsonArray.put(json);
             }
             return new ResponseEntity<>(jsonArray.toString(), HttpStatus.OK);
  
         } catch (Exception ex) {
             return new ResponseEntity<>("Error interno del servidor: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
         }
     }
}
