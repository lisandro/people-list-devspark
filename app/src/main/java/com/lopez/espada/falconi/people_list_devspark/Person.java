package com.lopez.espada.falconi.people_list_devspark;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gabriel on 30/07/15.
 */
public class Person implements Parcelable {
    private int id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String dob;

    public Person(Parcel pc){
        id = pc.readInt();
        name = pc.readString();
        phone = pc.readString();
        email = pc.readString();
        address = pc.readString();
        dob = pc.readString();
    }

    public Person(){}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(address);
        dest.writeString(dob);
    }

    public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
        public Person createFromParcel(Parcel pc) {
            return new Person(pc);
        }
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
