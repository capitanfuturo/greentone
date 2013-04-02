package it.greentone.persistence;

import java.awt.image.BufferedImage;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

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
 * Lo studio professionale al quale è rivolto il programma di gestione.
 * 
 * @author Giuseppe Caliendo
 */
@PersistenceCapable(table = "GT_OFFICE", detachable = "true")
public class Office
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
	@Persistent(defaultFetchGroup = "true")
	BufferedImage logo;

	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof Office)
		{
			Office candidate = (Office) obj;
			return id.equals(candidate.getId());
		}
		else
			return super.equals(obj);
	}

	/**
	 * Restituisce la ragione sociale della persona.
	 * 
	 * @return la ragione sociale della persona
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Imposta la ragione sociale della persona.
	 * 
	 * @param name
	 *          la ragione sociale della persona
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Restituisce l'indirizzo della persona.
	 * 
	 * @return l'indirizzo della persona
	 */
	public String getAddress()
	{
		return address;
	}

	/**
	 * Imposta l'indirizzo della persona.
	 * 
	 * @param address
	 *          l'indirizzo della persona
	 */
	public void setAddress(String address)
	{
		this.address = address;
	}

	/**
	 * Restituisce la città della persona.
	 * 
	 * @return la città della persona
	 */
	public String getCity()
	{
		return city;
	}

	/**
	 * Imposta la città della persona.
	 * 
	 * @param city
	 *          la città della persona
	 */
	public void setCity(String city)
	{
		this.city = city;
	}

	/**
	 * Restituisce la provincia della persona.
	 * 
	 * @return la provincia della persona
	 */
	public String getProvince()
	{
		return province;
	}

	/**
	 * Imposta la provincia della persona.
	 * 
	 * @param province
	 *          la provincia della persona
	 */
	public void setProvince(String province)
	{
		this.province = province;
	}

	/**
	 * Restituisce il codice di avviamento postale della persona.
	 * 
	 * @return il codice di avviamento postale della persona
	 */
	public String getCap()
	{
		return cap;
	}

	/**
	 * Imposta il codice di avviamento postale della persona.
	 * 
	 * @param cap
	 *          il codice di avviamento postale della persona
	 */
	public void setCap(String cap)
	{
		this.cap = cap;
	}

	/**
	 * Restituisce il codice fiscale della persona.
	 * 
	 * @return il codice fiscale della persona
	 */
	public String getCf()
	{
		return cf;
	}

	/**
	 * Imposta il codice fiscale della persona
	 * 
	 * @param cf
	 *          il codice fiscale della persona
	 */
	public void setCf(String cf)
	{
		this.cf = cf;
	}

	/**
	 * Restituisce la partita IVA dello studio.
	 * 
	 * @return la partita IVA dello studio
	 */
	public String getPiva()
	{
		return piva;
	}

	/**
	 * Imposta la partita IVA dello studio.
	 * 
	 * @param piva
	 *          la partita IVA dello studio
	 */
	public void setPiva(String piva)
	{
		this.piva = piva;
	}

	/**
	 * Restituisce il numero di telefono principale della persona.
	 * 
	 * @return il numero di telefono principale della persona
	 */
	public String getTelephone1()
	{
		return telephone1;
	}

	/**
	 * Imposta il numero di telefono principale della persona.
	 * 
	 * @param telephone1
	 *          il numero di telefono principale della persona
	 */
	public void setTelephone1(String telephone1)
	{
		this.telephone1 = telephone1;
	}

	/**
	 * Restituisce il numero di telefono secondario della persona.
	 * 
	 * @return il numero di telefono secondario della persona
	 */
	public String getTelephone2()
	{
		return telephone2;
	}

	/**
	 * Imposta il numero di telefono secondario della persona.
	 * 
	 * @param telephone2
	 *          il numero di telefono secondario della persona
	 */
	public void setTelephone2(String telephone2)
	{
		this.telephone2 = telephone2;
	}

	/**
	 * Restituisce il numero di fax della persona.
	 * 
	 * @return il numero di fax della persona
	 */
	public String getFax()
	{
		return fax;
	}

	/**
	 * Imposta il numero di fax della persona.
	 * 
	 * @param fax
	 *          il numero di fax della persona
	 */
	public void setFax(String fax)
	{
		this.fax = fax;
	}

	/**
	 * Restituisce l'indirizzo email della persona.
	 * 
	 * @return l'indirizzo email della persona
	 */
	public String getEmail()
	{
		return email;
	}

	/**
	 * Imposta l'indirizzo email della persona
	 * 
	 * @param email
	 *          l'indirizzo email della persona
	 */
	public void setEmail(String email)
	{
		this.email = email;
	}

	/**
	 * Restituisce l'identificativo del record.
	 * 
	 * @return l'identificativo del record
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * Imposta l'identificativo del record.
	 * 
	 * @param id
	 *          l'identificativo del record
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * Restituisce il logo dello studio professionale.
	 * 
	 * @return il logo dello studio professionale
	 */
	public BufferedImage getLogo()
	{
		return logo;
	}

	/**
	 * Imposta il logo dello studio professionale.
	 * 
	 * @param logo
	 *          il logo dello studio professionale
	 */
	public void setLogo(BufferedImage logo)
	{
		this.logo = logo;
	}

	@Override
	public String toString()
	{
		return getName();
	}
}
