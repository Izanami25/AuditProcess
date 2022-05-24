package com.AUI.auditprocess.Entities;

import javax.persistence.*;

@Entity
@Table(name = "audit")
public class AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String short_name;
    public String full_name;
    public String paragraph;
    public String requirement;
    public String description;
    public String uploadDate;
}
