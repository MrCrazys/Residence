package com.bekvon.bukkit.residence.allNms;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.bekvon.bukkit.cmiLib.CMIEffect;
import com.bekvon.bukkit.residence.containers.NMS;

import net.minecraft.server.v1_9_R1.EnumParticle;
import net.minecraft.server.v1_9_R1.Packet;
import net.minecraft.server.v1_9_R1.PacketPlayOutWorldParticles;

public class v1_9_R1 implements NMS {

    @Override
    public void playEffect(Player player, Location location, CMIEffect ef) {
	if (location == null || ef == null || location.getWorld() == null)
	    return;
	if (!location.getWorld().equals(player.getWorld()))
	    return;
	Packet<?> packet = null;
	if (ef.getParticle().getEffect() == null)
	    return;
	if (!ef.getParticle().isParticle()) {
//	    int packetData = effect.getId();
//	    packet = new PacketPlayOutWorldEvent(packetData, new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()), id, false);
	} else {
	    Particle effect = ef.getParticle().getParticle();

	    EnumParticle particle = ef.getParticle().getEnumParticle() == null ? null : (EnumParticle) ef.getParticle().getEnumParticle();
	    int[] extra = ef.getParticle().getExtra();
	    if (particle == null) {
		for (EnumParticle p : EnumParticle.values()) {
		    if (effect.name().replace("_", "").equalsIgnoreCase((p.toString().replace("_", ""))) ||
			ef.getParticle().getName().replace("_", "").equalsIgnoreCase((p.toString().replace("_", ""))) ||
			ef.getParticle().getSecondaryName().replace("_", "").equalsIgnoreCase((p.toString().replace("_", "")))) {
			particle = p;
			if (ef.getParticle().getEffect().getData() != null) {
			    extra = new int[] { (0 << 12) | (0 & 0xFFF) };
			}
			break;
		    }
		}
		if (extra == null) {
		    extra = new int[0];
		}
	    }

	    if (particle == null)
		return;

	    if (ef.getParticle().getEnumParticle() == null) {
		ef.getParticle().setEnumParticle(particle);
		ef.getParticle().setExtra(extra);
	    }

	    packet = new PacketPlayOutWorldParticles(particle, true, (float) location.getX(), (float) location.getY(), (float) location.getZ(), (float) ef.getOffset().getX(), (float) ef.getOffset().getY(),
		(float) ef.getOffset().getZ(), ef.getSpeed(), ef.getAmount(), extra);
	}
	CraftPlayer cPlayer = (CraftPlayer) player;
	if (cPlayer.getHandle().playerConnection == null)
	    return;

	if (packet == null)
	    return;
	cPlayer.getHandle().playerConnection.sendPacket(packet);
    }
}
