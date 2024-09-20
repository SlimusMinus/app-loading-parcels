package com.liga.appparcelsloading.fabric;

import com.liga.appparcelsloading.util.JsonFileReader;

public class JsonFileReaderFactory {
    public JsonFileReader createJsonFileReader() {
        return new JsonFileReader();
    }

}
