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
 * Una persona è un elemento dell'anagrafica del programma. Può concorrere negli
 * incarichi e può ricoprire ruoli amministrativi, funzionali o di sola lettura
 * una volta attribuiti i campi di account, altrimenti è semplicemente un record
 * di anagrafica con ruolo di cliente dello studio professionale.
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

	/**
	 * Una persona è un elemento dell'anagrafica del programma. Può concorrere
	 * negli incarichi e può ricoprire ruoli amministrativi, funzionali o di sola
	 * lettura una volta attribuiti i campi di account, altrimenti è semplicemente
	 * un record di anagrafica con ruolo di cliente dello studio professionale.
	 */
	public Person()
	{
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
	 * Restituisce la partita IVA della persona.
	 * 
	 * @return la partita IVA della persona
	 */
	public String getPiva()
	{
		return piva;
	}

	/**
	 * Imposta la partita IVA della persona.
	 * 
	 * @param piva
	 *          la partita IVA della persona
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
	 * Restituisce <code>true</code> se la persona è giuridica, <code>false</code>
	 * se fisica.
	 * 
	 * @return <code>true</code> se la persona è giuridica, <code>false</code> se
	 *         fisica
	 */
	public boolean getIsLegal()
	{
		return isLegal;
	}

	/**
	 * Imposta <code>true</code> se la persona è giuridica, <code>false</code> se
	 * fisica.
	 * 
	 * @param isLegal
	 *          <code>true</code> se la persona è giuridica, <code>false</code> se
	 *          fisica
	 */
	public void setLegal(boolean isLegal)
	{
		this.isLegal = isLegal;
	}

	/**
	 * Restituisce il numero di cartà di identità della persona.
	 * 
	 * @return il numero di cartà di identità della persona
	 */
	public String getIdentityCard()
	{
		return identityCard;
	}

	/**
	 * Imposta il numero di cartà di identità della persona.
	 * 
	 * @param identityCard
	 *          il numero di cartà di identità della persona
	 */
	public void setIdentityCard(String identityCard)
	{
		this.identityCard = identityCard;
	}

	/**
	 * Restituisce il nome utente di accesso al programma.
	 * 
	 * @return il nome utente di accesso al programma
	 */
	public String getUsername()
	{
		return username;
	}

	/**
	 * Imposta il nome utente di accesso al programma.
	 * 
	 * @param username
	 *          il nome utente di accesso al programma
	 */
	public void setUsername(String username)
	{
		this.username = username;
	}

	/**
	 * Restituisce la password di accesso al programma.
	 * 
	 * @return la password di accesso al programma
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * Imposta la password di accesso al programma.
	 * 
	 * @param password
	 *          la password di accesso al programma
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}

	/**
	 * Restituisce il profilo della persona.
	 * 
	 * @return il profilo della persona
	 */
	public UserProfile getProfile()
	{
		return profile;
	}

	/**
	 * Imposta il profilo della persona.
	 * 
	 * @param profile
	 *          il profilo della persona
	 */
	public void setProfile(UserProfile profile)
	{
		this.profile = profile;
	}

	/**
	 * Restituisce <code>true</code> se l'account è attivo, <code>false</code>
	 * altrimenti.
	 * 
	 * @return <code>true</code> se l'account è attivo, <code>false</code>
	 *         altrimenti
	 */
	public boolean getIsActive()
	{
		return isActive;
	}

	/**
	 * Imposta lo stato di attivazione dell'account della persona.
	 * 
	 * @param isActive
	 *          lo stato di attivazione dell'account della persona
	 */
	public void setIsActive(boolean isActive)
	{
		this.isActive = isActive;
	}

	/**
	 * Restituisce la data di attivazione dell'account.
	 * 
	 * @return la data di attivazione dell'account
	 */
	public DateTime getActivationDate()
	{
		return activationDate;
	}

	/**
	 * Imposta la data di attivazione dell'account.
	 * 
	 * @param activationDate
	 *          la data di attivazione dell'account
	 */
	public void setActivationDate(DateTime activationDate)
	{
		this.activationDate = activationDate;
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

	@Override
	public String toString()
	{
		return getName();
	}
}
