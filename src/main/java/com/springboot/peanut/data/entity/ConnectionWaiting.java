package com.springboot.peanut.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionWaiting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String patientName;

    private String patientEmail;

    private String inviteCode;

    private String guardianEmail;

    private String status;

    public ConnectionWaiting(String patientName, String patientEmail, String inviteCode, String status, String guardianEmail) {
        this.patientName = patientName;
        this.patientEmail = patientEmail;
        this.inviteCode = inviteCode;
        this.status = status;
        this.guardianEmail = guardianEmail;
    }
}
