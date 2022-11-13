package com.example.internallfinal.models;

public class Orders {
    private String id;
    private Clients client;
    private String orderInfo;

    public Orders(String id, Clients client, String orderInfo) {
        this.id = id;
        this.client = client;
        this.orderInfo = orderInfo;
    }

    public Orders() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Clients getClient() {
        return client;
    }

    public void setClient(Clients client) {
        this.client = client;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }
}
