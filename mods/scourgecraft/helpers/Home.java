package mods.scourgecraft.helpers;

import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;

public class Home {
	public String ownerUsername;
	public String name;
	public int xCoord;
	public int yCoord;
	public int zCoord;
	public int level;
	public int dimensionId;
	public long shieldTimeStamp;
	
	public List<Raid> raidList = Lists.newArrayList();
}
