package com.example.cartproject.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class StoreModel implements Parcelable {

    private String name, desc, image;
    private float shipping;
    private List<Product> products;

    protected StoreModel(Parcel in) {
        name = in.readString();
        desc = in.readString();
        image = in.readString();
        shipping = in.readFloat();
        products = in.createTypedArrayList(Product.CREATOR);
    }

    public static final Creator<StoreModel> CREATOR = new Creator<StoreModel>() {
        @Override
        public StoreModel createFromParcel(Parcel in) {
            return new StoreModel(in);
        }

        @Override
        public StoreModel[] newArray(int size) {
            return new StoreModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(desc);
        dest.writeString(image);
        dest.writeFloat(shipping);
        dest.writeTypedList(products);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public float getShipping() {
        return shipping;
    }

    public void setShipping(float shipping) {
        this.shipping = shipping;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
