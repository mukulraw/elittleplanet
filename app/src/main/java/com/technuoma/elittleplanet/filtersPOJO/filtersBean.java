package com.technuoma.elittleplanet.filtersPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class filtersBean {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("ram")
    @Expose
    private List<Ram> ram = null;
    @SerializedName("internal_storage")
    @Expose
    private List<InternalStorage> internalStorage = null;
    @SerializedName("network")
    @Expose
    private List<Network> network = null;
    @SerializedName("os")
    @Expose
    private List<O> os = null;
    @SerializedName("camera")
    @Expose
    private List<Camera> camera = null;
    @SerializedName("battery")
    @Expose
    private List<Battery> battery = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Ram> getRam() {
        return ram;
    }

    public void setRam(List<Ram> ram) {
        this.ram = ram;
    }

    public List<InternalStorage> getInternalStorage() {
        return internalStorage;
    }

    public void setInternalStorage(List<InternalStorage> internalStorage) {
        this.internalStorage = internalStorage;
    }

    public List<Network> getNetwork() {
        return network;
    }

    public void setNetwork(List<Network> network) {
        this.network = network;
    }

    public List<O> getOs() {
        return os;
    }

    public void setOs(List<O> os) {
        this.os = os;
    }

    public List<Camera> getCamera() {
        return camera;
    }

    public void setCamera(List<Camera> camera) {
        this.camera = camera;
    }

    public List<Battery> getBattery() {
        return battery;
    }

    public void setBattery(List<Battery> battery) {
        this.battery = battery;
    }

}
