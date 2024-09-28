package com.liga.appparcelsloading.fabric;

import com.liga.appparcelsloading.validator.ParcelValidator;
import com.liga.appparcelsloading.validator.TruckCountValidate;

public class ValidateFactory {
    public TruckCountValidate createTruckCountValidate() {
        return new TruckCountValidate();
    }
    public ParcelValidator createParcelValidator(){
        return new ParcelValidator();
    }
}
