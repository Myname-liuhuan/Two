package com.example.two.chinaCity;

/**
 * Created by liuhuan1 on 2018/6/16.
 */

public class Provinces {//建立与数据库的表对应的数据类，供List的泛型使用，然后用于对数据库数据的存储
    int id;
    String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
