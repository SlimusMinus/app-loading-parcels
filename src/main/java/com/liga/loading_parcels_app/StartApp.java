package com.liga.loading_parcels_app;

import com.liga.loading_parcels_app.util.FileReader;
import com.liga.loading_parcels_app.util.LoadingTrucks;
import com.liga.loading_parcels_app.util.ValidationData;

import java.util.List;

public class StartApp {
    public static void main(String[] args) {
        List<int[][]> packages = FileReader.getAllPackages("parcels.txt");


        if(ValidationData.isValidation(packages)){
            List<char[][]> trucks = LoadingTrucks.packPackages(packages);
            LoadingTrucks.printTrucks(trucks);

        }
    }
}


