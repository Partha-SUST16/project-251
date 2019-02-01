package com.koushik.health_kit;

public class PatientInfo {
    public String name,gender,age,blood,area,phone,email;
    public String prescriptionNumber;
    public PatientInfo(){

    }

    public PatientInfo(String name, String gender, String age, String blood, String area, String phone,String email,String prescriptionNumber) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.blood = blood;
        this.area = area;
        this.phone = phone;
        this.email=email;
        this.prescriptionNumber = prescriptionNumber;
    }
}
