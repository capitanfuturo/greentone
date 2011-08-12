package it.greentone.persistence;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.joda.time.DateTime;

/**
 * <code>
 * GreenTone - gestionale per geometri italiani.<br>
 * Copyright (C) 2011 GreenTone Developer Team.<br>
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * </code>
 * <br>
 * <br>
 * 
 * @author Giuseppe Caliendo
 */
@PersistenceCapable(table = "GT_PERSON", detachable = "true")
public class Person
{
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	String name;
	@Persistent
	String address;
	@Persistent
	String city;
	@Persistent
	String province;
	@Persistent
	String cap;
	@Persistent
	String cf;
	@Persistent
	String piva;
	@Persistent
	String telephone1;
	@Persistent
	String telephone2;
	@Persistent
	String fax;
	@Persistent
	String email;
	@Persistent
	boolean isLegal;
	@Persistent
	String identityCard;
	@Persistent
	String username;
	@Persistent
	String password;
	@Persistent
	UserProfile profile;
	@Persistent
	boolean isActive;
	@Persistent(defaultFetchGroup = "true")
	DateTime activationDate;

	public Person()
	{
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getProvince()
	{
		return province;
	}

	public void setProvince(String province)
	{
		this.province = province;
	}

	public String getCap()
	{
		return cap;
	}

	public void setCap(String cap)
	{
		this.cap = cap;
	}

	public String getCf()
	{
		return cf;
	}

	public void setCf(String cf)
	{
		this.cf = cf;
	}

	public String getPiva()
	{
		return piva;
	}

	public void setPiva(String piva)
	{
		this.piva = piva;
	}

	public String getTelephone1()
	{
		return telephone1;
	}

	public void setTelephone1(String telephone1)
	{
		this.telephone1 = telephone1;
	}

	public String getTelephone2()
	{
		return telephone2;
	}

	public void setTelephone2(String telephone2)
	{
		this.telephone2 = telephone2;
	}

	public String getFax()
	{
		return fax;
	}

	public void setFax(String fax)
	{
		this.fax = fax;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public boolean getIsLegal()
	{
		return isLegal;
	}

	public void setLegal(boolean isLegal)
	{
		this.isLegal = isLegal;
	}

	public String getIdentityCard()
	{
		return identityCard;
	}

	public void setIdentityCard(String identityCard)
	{
		this.identityCard = identityCard;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public UserProfile getProfile()
	{
		return profile;
	}

	public void setProfile(UserProfile profile)
	{
		this.profile = profile;
	}

	public boolean getIsActive()
	{
		return isActive;
	}

	public void setIsActive(boolean isActive)
	{
		this.isActive = isActive;
	}

	public DateTime getActivationDate()
	{
		return activationDate;
	}

	public void setActivationDate(DateTime activationDate)
	{
		this.activationDate = activationDate;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	@Override
	public String toString()
	{
		return getName();
	}
}
