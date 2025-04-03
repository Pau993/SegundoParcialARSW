package edu.eci.arsw.myrestaurant.beans.impl.colombia;

import edu.eci.arsw.myrestaurant.model.RestaurantProduct;
import edu.eci.arsw.myrestaurant.beans.TaxesCalculator;

public class StandardTaxesCalculator implements TaxesCalculator {

	@Override
	public float getProductTaxes(RestaurantProduct p) {
		return 0.16f;
	}

}
