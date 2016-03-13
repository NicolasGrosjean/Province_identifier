package tests;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import base.Terrain;
import config.ConfigStorage;
import config.WorkingSession;

public class TestTerrain {
	private static final String configFile = "config_test.xml";
	private static ConfigStorage configuration;
	private static WorkingSession swmh;
	private static WorkingSession vanilla;
	
	@BeforeClass
	public static void SetUp() {
		configuration = new ConfigStorage(configFile);
		// Use the 2nd configuration
		Iterator<WorkingSession> it = configuration.iterator();
		swmh = it.next();
		vanilla = it.next();
		try {
			swmh.initialize();
			vanilla.initialize();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	/**
	 * Test some province terrains
	 */
	public void vanillaProvinceTerrain() {
		Assert.assertEquals("1st province terrain", Terrain.arctic,
				vanilla.getProvinces().getProvince(1).getTerrain());
		Assert.assertEquals("2nd province terrain", Terrain.mountain,
				vanilla.getProvinces().getProvince(2).getTerrain()); 
		Assert.assertEquals("5th province terrain", Terrain.hills,
				vanilla.getProvinces().getProvince(5).getTerrain()); 
		Assert.assertEquals("7th province terrain", Terrain.plains,
				vanilla.getProvinces().getProvince(7).getTerrain()); 
		Assert.assertEquals("13th province terrain", Terrain.forest,
				vanilla.getProvinces().getProvince(13).getTerrain()); 
		Assert.assertEquals("14th province terrain", Terrain.farmlands,
				vanilla.getProvinces().getProvince(14).getTerrain()); 
		Assert.assertEquals("937th province terrain", Terrain.coastal_desert,
				vanilla.getProvinces().getProvince(937).getTerrain()); 
		Assert.assertEquals("299th province terrain", Terrain.desert,
				vanilla.getProvinces().getProvince(299).getTerrain()); 
		Assert.assertEquals("1118th province terrain", Terrain.jungle,
				vanilla.getProvinces().getProvince(1118).getTerrain()); 
		Assert.assertEquals("512th province terrain", Terrain.steppe,
				vanilla.getProvinces().getProvince(512).getTerrain()); 
		Assert.assertEquals("130th province terrain", Terrain.plains,
				vanilla.getProvinces().getProvince(130).getTerrain()); 
	}

	@Test
	/**
	 * Test some province terrains
	 */
	public void swmhProvinceTerrain() {
		Assert.assertEquals("1st province terrain", Terrain.arctic,
				swmh.getProvinces().getProvince(1).getTerrain());
		Assert.assertEquals("2nd province terrain", Terrain.mountain,
				swmh.getProvinces().getProvince(2).getTerrain()); 
		Assert.assertEquals("5th province terrain", Terrain.hills,
				swmh.getProvinces().getProvince(5).getTerrain()); 
		Assert.assertEquals("7th province terrain", Terrain.plains,
				swmh.getProvinces().getProvince(7).getTerrain()); 
		Assert.assertEquals("22th province terrain", Terrain.forest,
				swmh.getProvinces().getProvince(22).getTerrain()); 
		Assert.assertEquals("27th province terrain", Terrain.farmlands,
				swmh.getProvinces().getProvince(27).getTerrain()); 
		Assert.assertEquals("79th province terrain", Terrain.coastal_desert,
				swmh.getProvinces().getProvince(79).getTerrain()); 
		Assert.assertEquals("624th province terrain", Terrain.desert,
				swmh.getProvinces().getProvince(624).getTerrain()); 
		Assert.assertEquals("1617th province terrain", Terrain.jungle,
				swmh.getProvinces().getProvince(1617).getTerrain()); 
		Assert.assertEquals("1680th province terrain", Terrain.steppe,
				swmh.getProvinces().getProvince(1680).getTerrain());
	}
}
