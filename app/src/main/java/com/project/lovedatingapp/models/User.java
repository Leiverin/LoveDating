package com.project.lovedatingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class User implements Parcelable {
    private String id;
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String status;
    private int age;
    private String gender;
    private String search;
    private String address;
    private String objectFind;
    private String height;
    private String weight;
    private String level;
    private String introduce;
    private String work;
    private String salary;
    private List<Image> mListImage;

    public User() {
    }

    public User(List<Image> mListImage) {
        this.mListImage = mListImage;
    }

    public User(String id, List<Image> mListImage) {
        this.id = id;
        this.mListImage = mListImage;
    }

    public User(String id, String username, String password, String email, String fullName, String status, int age, String gender, String search, String address, String objectFind, String height, String weight, String level, String introduce, String work, String salary, List<Image> mListImage) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.status = status;
        this.age = age;
        this.gender = gender;
        this.search = search;
        this.address = address;
        this.objectFind = objectFind;
        this.height = height;
        this.weight = weight;
        this.level = level;
        this.introduce = introduce;
        this.work = work;
        this.salary = salary;
        this.mListImage = mListImage;
    }

    public User(String id, String username, String password, String email, String fullName, String status, int age, String search) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.status = status;
        this.age = age;
        this.search=search;
    }

    protected User(Parcel in) {
        id = in.readString();
        username = in.readString();
        password = in.readString();
        email = in.readString();
        fullName = in.readString();
        status = in.readString();
        age = in.readInt();
        gender = in.readString();
        search = in.readString();
        address = in.readString();
        objectFind = in.readString();
        height = in.readString();
        weight = in.readString();
        level = in.readString();
        work = in.readString();
        salary = in.readString();
        introduce = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getObjectFind() {
        return objectFind;
    }

    public void setObjectFind(String objectFind) {
        this.objectFind = objectFind;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }


    public List<Image> getListImage() {
        return mListImage;
    }

    public void setListImage(List<Image> mListImage) {
        this.mListImage = mListImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(username);
        parcel.writeString(password);
        parcel.writeString(email);
        parcel.writeString(fullName);
        parcel.writeString(status);
        parcel.writeInt(age);
        parcel.writeString(gender);
        parcel.writeString(search);
        parcel.writeString(address);
        parcel.writeString(objectFind);
        parcel.writeString(height);
        parcel.writeString(weight);
        parcel.writeString(level);
        parcel.writeString(work);
        parcel.writeString(salary);
        parcel.writeString(introduce);
    }
}
