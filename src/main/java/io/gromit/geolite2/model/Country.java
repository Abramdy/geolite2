/**
 * Copyright 2016 gromit.it
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gromit.geolite2.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * The Class Country.
 */
public class Country {

	/** The geoname id. */
	private Integer geonameId;
	
	/** The iso. */
	private String iso;
	
	/** The iso3. */
	private String iso3;
	
	/** The name. */
	private String name;
	
	/** The capital. */
	private String capital;
	
	/** The continent. */
	private String continent;
	
	/** The currency code. */
	private String currencyCode;
	
	/** The currency name. */
	private String currencyName;
	
	/** The phone. */
	private String phone;
	
	/** The language. */
	private String language;
	
	/**
	 * Gets the geoname id.
	 *
	 * @return the geoname id
	 */
	public Integer getGeonameId() {
		return geonameId;
	}

	/**
	 * Sets the geoname id.
	 *
	 * @param geonameId the new geoname id
	 */
	public void setGeonameId(Integer geonameId) {
		this.geonameId = geonameId;
	}

	/**
	 * Gets the iso.
	 *
	 * @return the iso
	 */
	public String getIso() {
		return iso;
	}

	/**
	 * Sets the iso.
	 *
	 * @param iso the new iso
	 */
	public void setIso(String iso) {
		this.iso = iso;
	}

	/**
	 * Gets the iso3.
	 *
	 * @return the iso3
	 */
	public String getIso3() {
		return iso3;
	}

	/**
	 * Sets the iso3.
	 *
	 * @param iso3 the new iso3
	 */
	public void setIso3(String iso3) {
		this.iso3 = iso3;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the capital.
	 *
	 * @return the capital
	 */
	public String getCapital() {
		return capital;
	}

	/**
	 * Sets the capital.
	 *
	 * @param capital the new capital
	 */
	public void setCapital(String capital) {
		this.capital = capital;
	}

	/**
	 * Gets the continent.
	 *
	 * @return the continent
	 */
	public String getContinent() {
		return continent;
	}

	/**
	 * Sets the continent.
	 *
	 * @param continent the new continent
	 */
	public void setContinent(String continent) {
		this.continent = continent;
	}

	/**
	 * Gets the currency code.
	 *
	 * @return the currency code
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}

	/**
	 * Sets the currency code.
	 *
	 * @param currencyCode the new currency code
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	/**
	 * Gets the currency name.
	 *
	 * @return the currency name
	 */
	public String getCurrencyName() {
		return currencyName;
	}

	/**
	 * Sets the currency name.
	 *
	 * @param currencyName the new currency name
	 */
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	/**
	 * Gets the phone.
	 *
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Sets the phone.
	 *
	 * @param phone the new phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * Gets the language.
	 *
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * Sets the language.
	 *
	 * @param language the new language
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
