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
package io.gromit.geolite2;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maxmind.db.NoCache;
import com.maxmind.db.NodeCache;
import com.maxmind.db.Reader.FileMode;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;

import io.gromit.geolite2.model.Location;

/**
 * The Class ScheduledDatabaseReader.
 */
public class GeoLocation {

	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(GeoLocation.class);

	/** The scheduled executor service. */
	private ScheduledExecutorService scheduledExecutorService;

	/** The reader. */
	private DatabaseReader databaseReader;

	/** The local m d5 checksum. */
	private String localMD5Checksum;

	/** The md5 checksum url. */
	private String md5ChecksumUrl = "http://geolite.maxmind.com/download/geoip/database/GeoLite2-City.md5";

	/** The database url. */
	private String databaseUrl = "http://geolite.maxmind.com/download/geoip/database/GeoLite2-City.mmdb.gz";

	/** The locales. */
	private List<String> locales = Collections.singletonList("en");
	
	/** The cache. */
	private NodeCache cache = NoCache.getInstance();

	/**
	 * Instantiates a new scheduled database reader.
	 */
	public GeoLocation() {
	}

	/**
	 * Md5 checksum url.
	 *
	 * @param md5ChecksumUrl
	 *            the md5 checksum url
	 * @return the scheduled database reader
	 */
	public GeoLocation md5ChecksumUrl(String md5ChecksumUrl) {
		this.md5ChecksumUrl = md5ChecksumUrl;
		return this;
	}

	/**
	 * Database url.
	 *
	 * @param databaseUrl
	 *            the database url
	 * @return the scheduled database reader
	 */
	public GeoLocation databaseUrl(String databaseUrl) {
		this.databaseUrl = databaseUrl;
		return this;
	}

	/**
	 * Locales.
	 *
	 * @param val the val
	 * @return the scheduled database reader
	 */
	public GeoLocation locales(List<String> val) {
		this.locales = val;
		return this;
	}

	/**
	 * Cache.
	 *
	 * @param cache the cache
	 * @return the scheduled database reader
	 */
	public GeoLocation cache(NodeCache cache) {
		this.cache = cache;
		return this;
	}


	public Location location(String ip){
		CityResponse cityResponse;
		try {
			cityResponse = this.databaseReader.city(InetAddress.getByName(ip));
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException(ip+" is not valid",e);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(),e);
		}
		if(cityResponse==null){
			return null;
		}
		Location location = new Location();
		if(cityResponse.getCity()!=null){
			location.setCityName(cityResponse.getCity().getName());
			location.setCityGeonameId(cityResponse.getCity().getGeoNameId());
		}
		if(cityResponse.getContinent()!=null){
			location.setContinentCode(cityResponse.getContinent().getCode());
			location.setContinentName(cityResponse.getContinent().getName());
			location.setContinentGeonameId(cityResponse.getContinent().getGeoNameId());
		}
		if(cityResponse.getCountry()!=null){
			location.setCountryIsoCode(cityResponse.getCountry().getIsoCode());
			location.setCountryName(cityResponse.getCountry().getName());
			location.setCountryGeonameId(cityResponse.getCountry().getGeoNameId());
		}
		if(cityResponse.getLocation()!=null){
			location.setLatitude(cityResponse.getLocation().getLatitude());
			location.setLongitude(cityResponse.getLocation().getLongitude());
		}
		if(cityResponse.getSubdivisions()!=null 
				&& cityResponse.getSubdivisions().size()>0){
			location.setSubdivisionIsoCode(cityResponse.getSubdivisions().get(0).getIsoCode());
			location.setSubdivisionName(cityResponse.getSubdivisions().get(0).getName());
			location.setSubdivisionGeonameId(cityResponse.getSubdivisions().get(0).getGeoNameId());
		}
		
		return location;
	}
	
	/**
	 * Start.
	 *
	 * @return the scheduled database reader
	 * @throws IllegalStateException the illegal state exception
	 */
	public GeoLocation start() throws IllegalStateException{
		if (scheduledExecutorService != null) {
			throw new IllegalStateException("it is already started");
		}
		readDatabase();
		scheduledExecutorService = Executors.newScheduledThreadPool(1);
		scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				readDatabase();
			}
		}, 1, 1, TimeUnit.DAYS);
		return this;
	}

	/**
	 * Stop.
	 *
	 * @return the scheduled database reader
	 * @throws IllegalStateException the illegal state exception
	 */
	public GeoLocation stop() throws IllegalStateException{
		if (scheduledExecutorService == null) {
			throw new IllegalStateException("it was never started");
		}
		scheduledExecutorService.shutdown();
		try {
			if (databaseReader != null) {
				this.databaseReader.close();
			}
		} catch (IOException e) {
			logger.warn("error closing reader: {}", e.getMessage());
		}
		return this;
	}

	/**
	 * Read database.
	 */
	public void readDatabase() {
		String onlineMD5Checksum = null;
		try{
			onlineMD5Checksum = IOUtils.toString(new URL(md5ChecksumUrl).openStream()).trim();
		}catch(Exception e){
			logger.error("could not read MD5 online: {}",e.getMessage());
			return;
		}
		if(!onlineMD5Checksum.equals(localMD5Checksum)){
			try{
				logger.info("UPDATING local database with online database");
				DatabaseReader newReader = new DatabaseReader.Builder(new GZIPInputStream(new URL(databaseUrl).openStream())).locales(locales).fileMode(FileMode.MEMORY).withCache(cache).build();
				if(databaseReader!=null){
					final DatabaseReader readerToClose = databaseReader;
					new Timer().schedule(new TimerTask() {				
						@Override
						public void run() {
							try {
								readerToClose.close();
							} catch (IOException e) {
								logger.warn("error closing reader: {}",e.getMessage());
							}
						}
					}, 60*1000);
				}
				this.localMD5Checksum=onlineMD5Checksum;
				this.databaseReader = newReader;
				logger.info("UPDATED local database with online database");
			}catch(Exception e){
				logger.error("could not read Database online: {}",e.getMessage());
			}
		}else{
			logger.info("local and online database are the same");
		}
	}

}