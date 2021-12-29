package com.yiying.excelhelper.entities;

import lombok.Data;

import java.time.LocalDate;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
/**
 * @description user
 * @author yiying
 * @date 2021-12-25
 */
@Entity
@Data
@Table(name="t_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    /**
     * id
     */
    @Column(name="id")
    private Integer id;

    /**
     * name
     */
    @Column(name="name")
    private String name;

    /**
     * sex
     */
    @Column(name="sex")
    private String sex;

    /**
     * age
     */
    @Column(name="age")
    private Integer age;

    /**
     * birthday
     */
    @Column(name="birthday")
    private LocalDate birthday;

    /**
     * marital_status
     */
    @Column(name="marital_status")
    private String maritalStatus;

    /**
     * phone_number
     */
    @Column(name="phone_number")
    private String phoneNumber;

    /**
     * id_card_num
     */
    @Column(name="id_card_num")
    private String idCardNum;

    /**
     * email
     */
    @Column(name="email")
    private String email;

    /**
     * note
     */
    @Column(name="note")
    private String note;

    /**
     * nation
     */
    @Column(name="nation")
    private String nation;

    /**
     * address
     */
    @Column(name="address")
    private String address;

    public User() {
    }

}