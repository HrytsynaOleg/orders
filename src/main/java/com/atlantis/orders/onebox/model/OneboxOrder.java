package com.atlantis.orders.onebox.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OneboxOrder {
    private String id;
    private String name;
    private String cdate;
    private OneboxOrderClient client;
    private OneboxStatus status;
    private Map<String, OneboxOrderCustomField> customfields;
    private OneboxWorkflow workflow;
    private List<OneboxOrderProduct> orderproducts;

//    @JsonProperty("status")
//    private void unpackNested(Map<String,String> status) {
//        OneboxStatus oneboxStatus = new OneboxStatus();
//        oneboxStatus.setColor(status.get("color"));
//        oneboxStatus.setName(status.get("name"));
//        oneboxStatus.setId(status.get("id"));
//        this.status = oneboxStatus;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCdate() {
        return cdate;
    }

    public void setCdate(String cdate) {
        this.cdate = cdate;
    }

    public OneboxOrderClient getClient() {
        return client;
    }

    public void setClient(OneboxOrderClient client) {
        this.client = client;
    }

    public OneboxStatus getStatus() {
        return status;
    }

    public void setStatus(OneboxStatus status) {
        this.status = status;
    }

    public Map<String, OneboxOrderCustomField> getCustomfields() {
        return customfields;
    }

    public void setCustomfields(Map<String, OneboxOrderCustomField> customfields) {
        this.customfields = customfields;
    }

    public OneboxWorkflow getWorkflow() {
        return workflow;
    }

    public void setWorkflow(OneboxWorkflow workflow) {
        this.workflow = workflow;
    }

    public List<OneboxOrderProduct> getOrderproducts() {
        return orderproducts;
    }

    public void setOrderproducts(List<OneboxOrderProduct> orderproducts) {
        this.orderproducts = orderproducts;
    }
}
