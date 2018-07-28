package test.t.sql.dto;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import t.sql.interfaces.DTO;
import t.sql.validates.StringVerification;

@Table
public class Test implements DTO{
	@Id
	@Column(name="id")
	private String id;
	@Column(name="name")
	private String name;
	@Column(name="value")
	private String value;
	@Column(name="commen")
	private String commen;
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getCommen() {
		return commen;
	}
	public void setCommen(String commen) {
		this.commen = commen;
	}
	
}
