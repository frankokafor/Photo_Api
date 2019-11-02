package com.frankokafor.rest.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "addresses")
public class AddressEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private long id;
	@Column(nullable = false, length = 50)
	private String addressId;
	@Column(nullable = false, length = 50)
	private String city;
	@Column(nullable = false, length = 50)
	private String country;
	@Column(nullable = false, length = 50)
	private String type;
	@Column(nullable = false, length = 100)
	private String streetName;
	@Column(nullable = false, length = 50)
	private String postalCode;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserEntity userDetails;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public UserEntity getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserEntity userDetails) {
		this.userDetails = userDetails;
	}

}
