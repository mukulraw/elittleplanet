package com.technuoma.elittleplanet.seingleProductPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("cat")
    @Expose
    private String cat;
    @SerializedName("subcat1")
    @Expose
    private String subcat1;
    @SerializedName("subcat2")
    @Expose
    private String subcat2;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image")
    @Expose
    private List<String> image = null;
    @SerializedName("key_features")
    @Expose
    private String keyFeatures;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("packaging_type")
    @Expose
    private String packagingType;
    @SerializedName("shelf_life")
    @Expose
    private String shelfLife;
    @SerializedName("seller")
    @Expose
    private String seller;
    @SerializedName("disclaimer")
    @Expose
    private String disclaimer;
    @SerializedName("wishlist")
    @Expose
    private String wishlist;
    @SerializedName("related")
    @Expose
    private List<Related> related = null;
    @SerializedName("additional_size")
    @Expose
    private List<AdditionalSize> additionalSize = null;
    @SerializedName("additional_color")
    @Expose
    private List<AdditionalColor> additionalColor = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("stock")
    @Expose
    private String stock;
    @SerializedName("created")
    @Expose
    private String created;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getSubcat1() {
        return subcat1;
    }

    public void setSubcat1(String subcat1) {
        this.subcat1 = subcat1;
    }

    public String getSubcat2() {
        return subcat2;
    }

    public void setSubcat2(String subcat2) {
        this.subcat2 = subcat2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public String getKeyFeatures() {
        return keyFeatures;
    }

    public void setKeyFeatures(String keyFeatures) {
        this.keyFeatures = keyFeatures;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPackagingType() {
        return packagingType;
    }

    public void setPackagingType(String packagingType) {
        this.packagingType = packagingType;
    }

    public String getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(String shelfLife) {
        this.shelfLife = shelfLife;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public String getWishlist() {
        return wishlist;
    }

    public void setWishlist(String wishlist) {
        this.wishlist = wishlist;
    }

    public List<Related> getRelated() {
        return related;
    }

    public void setRelated(List<Related> related) {
        this.related = related;
    }

    public List<AdditionalSize> getAdditionalSize() {
        return additionalSize;
    }

    public void setAdditionalSize(List<AdditionalSize> additionalSize) {
        this.additionalSize = additionalSize;
    }

    public List<AdditionalColor> getAdditionalColor() {
        return additionalColor;
    }

    public void setAdditionalColor(List<AdditionalColor> additionalColor) {
        this.additionalColor = additionalColor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

}
