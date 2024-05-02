package com.eland.pojo.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "user_information", schema = "dbo", catalog = "testdb")
public class UserInformationEntity {
    private int id;
    private String account;
    private String name;
    private int age;
    private Date createTime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 讓資料庫自行生成 ID
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "account")
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "age")
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Basic
    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInformationEntity that = (UserInformationEntity) o;
        return id == that.id &&
                age == that.age &&
                Objects.equals(account, that.account) &&
                Objects.equals(name, that.name) &&
                Objects.equals(createTime, that.createTime);
    }

    @Override
    public String toString() {
        return "UserInformationEntity{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", createTime=" + createTime +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, account, name, age, createTime);
    }
}
