package com.liga.loadingParcelsApp.model;

import lombok.Getter;

@Getter
public class Package {
    private final int[][] content;

    public Package(int[][] content) {
        this.content = content;
    }

}
