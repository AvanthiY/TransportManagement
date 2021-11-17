package com.tms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
public class Roles {
   @Id
   @Column(name = "role_id")
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer id;
    
   private String name;

   public Roles() {
	   
   }
   public Roles(Integer id, String name) {
	   this.id = id;
	   this.name = name;
   }
   
   public Integer getId() {
       return id;
   }
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setId(Integer id) {
		this.id = id;
	}
    

}