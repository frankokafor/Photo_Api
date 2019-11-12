/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frankokafor.rest.models;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mac
 */
@Entity
@Table(name = "roles")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Roles.findAll", query = "SELECT r FROM Roles r")
    , @NamedQuery(name = "Roles.findById", query = "SELECT r FROM Roles r WHERE r.id = :id")
    , @NamedQuery(name = "Roles.findByName", query = "SELECT r FROM Roles r WHERE r.name = :name")
    , @NamedQuery(name = "Roles.findByPrivelege", query = "SELECT r FROM Roles r WHERE r.privelege = :privelege")})
public class Roles implements Serializable {

    public static final int ADMIN = 1;
    public static final int STAFF = 2;
    public static final int SUPER_ADMIN = 3;
    public static final int HR = 4;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 50)
    @Column(name = "name")
    private String name;
    @Size(max = 50)
    @Column(name = "privelege")
    private String privelege;
    @ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private UserEntity userDetails;

    public Roles() {
    }
    
    public Roles(@Size(max = 50) String name, @Size(max = 50) String privelege, UserEntity userDetails) {
		super();
		this.name = name;
		this.privelege = privelege;
		this.userDetails = userDetails;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrivelege() {
        return privelege;
    }

    public void setPrivelege(String privelege) {
        this.privelege = privelege;
    }

    public UserEntity getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserEntity userDetails) {
		this.userDetails = userDetails;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Roles)) {
            return false;
        }
        Roles other = (Roles) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sapphital.enterprise_bean.Roles[ id=" + id + " ]";
    }

}
