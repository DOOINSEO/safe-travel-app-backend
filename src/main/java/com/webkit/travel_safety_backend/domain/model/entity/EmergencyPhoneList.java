//package com.webkit.travel_safety_backend.domain.model.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//import org.hibernate.annotations.OnDelete;
//import org.hibernate.annotations.OnDeleteAction;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class EmergencyPhoneList {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String phone;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "emergency_id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private Emergency emergencyContact;
//}
