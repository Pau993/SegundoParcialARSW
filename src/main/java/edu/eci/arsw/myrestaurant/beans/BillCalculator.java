package edu.eci.arsw.myrestaurant.beans;

import edu.eci.arsw.myrestaurant.model.Order;
import edu.eci.arsw.myrestaurant.model.RestaurantProduct;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("billCalculator")
public interface BillCalculator {
	@Autowired
	public int calculateBill(Order o,Map<String,RestaurantProduct> productsMap) ;
	
}


