package com.antiform.allenPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class CustomItem extends ItemStack
{
	// i made this so it will be easy to make custom items
	
	private HashMap<Character, String> customValues = new HashMap<Character, String>();
	
	private Boolean stackable = true;
	
	private String[] disc = null;
	private String rarity = null;
	private ChatColor newRarityColor = null;
	private String[] abilityDisc = null;
	private String abilityName = null;
	
	// the constructer just takes in a base material and name and puts that into the item.
	public CustomItem(Material mat, String displayName)
	{
		this.setType(mat);
		if (!this.getType().equals(Material.AIR))
		{			
			ItemMeta meta = this.getItemMeta();
			meta.setDisplayName(displayName);
			meta.getPersistentDataContainer().set(allenPlugin.customItemBaseName, PersistentDataType.STRING, meta.getDisplayName());
			this.setItemMeta(meta);
		}
	}
	
	// if we want it to be a custom player head we can use this method
	// (i copied this from the internet btw i have no idea how this works)
	public void setCustomSkull(String url) {

        this.setType(Material.PLAYER_HEAD);
        if (url.isEmpty()) return;

        SkullMeta skullMeta = (SkullMeta) this.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null); // voodoo magic i found online

        profile.getProperties().put("textures", new Property("textures", url));

        try {
            Method mtd = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
            mtd.setAccessible(true);
            mtd.invoke(skullMeta, profile);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            ex.printStackTrace();
        }

        this.setItemMeta(skullMeta);
    }
	
	public void setNotStackable()
	{
		stackable = false;
	}
	
	public Boolean getStackable()
	{
		return stackable;
	}
	
	// i reperposed this for persistent data on itemstacks, i will do more with this soon.
	public void setCustomValue(NamespacedKey key, String value)
	{
		stackable = false;
		ItemMeta meta = this.getItemMeta();
		meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
		this.setItemMeta(meta);
	}
	
	public String getCustomValue(NamespacedKey key)
	{
		return this.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
	}
	
	public Boolean hasCustomValue(NamespacedKey key)
	{
		return this.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING);
	}
	
	// some getters and setters for the lore variables
	public void setDiscription(String... lines) { disc = lines; }
	public void setRarity(String value) { rarity = value; }
	public void setRarityColor(ChatColor value) { newRarityColor = value; }
	public void setAbilityDisc(String... lines) { abilityDisc = lines; }
	public void setAbilityName(String value) { abilityName = value; }
	
	public String[] getDiscription() { return disc; }
	public String getRarity() { return rarity; }
	public ChatColor getRarityColor() { return newRarityColor; }
	public String[] getAbilityDisc() { return abilityDisc; }
	public String getAbilityName() { return abilityName; }
	
	public void updateLore()
	{
		// used to create custom item lore easily
		ItemMeta itemMeta = this.getItemMeta();
		// gets what rarity it is using a switch statement.
		String rarityColor = ChatColor.LIGHT_PURPLE.toString();
		switch (rarity) {
		case "common":
			rarityColor = ChatColor.WHITE.toString();
			break;
		case "uncommon":
			rarityColor = ChatColor.GREEN.toString();
			break;
		case "rare":
			rarityColor = ChatColor.BLUE.toString();
			break;
		case "epic":
			rarityColor = ChatColor.DARK_PURPLE.toString();
			break;
		case "legendary":
			rarityColor = ChatColor.GOLD.toString();
			break;
		case "mythic":
			rarityColor = ChatColor.RED.toString();
			break;
		case "evil":
			rarityColor = ChatColor.DARK_RED.toString();
			break;
			
		default:
			rarityColor = ChatColor.LIGHT_PURPLE.toString();
			break;
		}
		
		// see if there is a custom rarity color specified, and if so set rarityColor to it
		if (newRarityColor != null) rarityColor = newRarityColor.toString();
		
		// now we construct the lore list
		List<String> lore = new ArrayList<String>();
		// any of the values can be null and then they will not be put onto the lore
		if (disc != null)
		{
			lore.add("");
			// for every line of the discription
			for (int i = 0; i < disc.length; i++)
			{			
				String line = disc[i];
				// for every charecter in that line
				for (int o = 0; o < line.length(); o++)
				{
					// if we have that special char and the other thing
					if (line.charAt(o) == '@' && customValues.containsKey(line.charAt(o + 1)))
					{
						// replace that stuff with the right value
						line = line.replace("@" + line.charAt(o + 1), customValues.get(line.charAt(o + 1)));
					}
				}
				lore.add(ChatColor.GRAY + line);
			}
		}
		if (abilityName != null && abilityDisc != null)
		{
			lore.add("");
			lore.add(rarityColor + "Ability: " + abilityName);
			for (int i = 0; i < abilityDisc.length; i++)
			{			
				String line = disc[i];
				// for every charecter in that line
				for (int o = 0; o < line.length(); o++)
				{
					// if we have that special char and the other thing
					if (line.charAt(o) == '@' && customValues.containsKey(line.charAt(o + 1)))
					{
						// replace that stuff with the right value
						line = line.replace("@" + line.charAt(o + 1), customValues.get(line.charAt(o + 1)));
					}
				}
				lore.add(ChatColor.GRAY + line);
			}
		}
		// just in case it's renamed, put it's name right here
		lore.add(ChatColor.DARK_GRAY + itemMeta.getDisplayName());
		if (rarity != null)
		{
			lore.add("");
			lore.add(rarityColor + ChatColor.BOLD + rarity.toUpperCase());
		}
		
		// now lets just finish it up
		itemMeta.setLore(lore);
		
		itemMeta.setDisplayName(rarityColor + itemMeta.getDisplayName());
		
		this.setItemMeta(itemMeta);
	}
	
	// probably not needed, but it's here just in case.
	public ItemStack getItemStack()
	{
		return (ItemStack) this;
	}
	
	// sadly i have to hard code all the enchantments in to make it not enchantable i think (if there is an easier way to do this please update it)
	public void setGlowing()
	{
		ItemMeta itemMeta = this.getItemMeta();
		itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 0, true);
		itemMeta.addEnchant(Enchantment.ARROW_FIRE, 0, true);
		itemMeta.addEnchant(Enchantment.ARROW_INFINITE, 0, true);
		itemMeta.addEnchant(Enchantment.ARROW_KNOCKBACK, 0, true);
		itemMeta.addEnchant(Enchantment.CHANNELING, 0, true);
		itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 0, true);
		itemMeta.addEnchant(Enchantment.DAMAGE_ARTHROPODS, 0, true);
		itemMeta.addEnchant(Enchantment.DAMAGE_UNDEAD, 0, true);
		itemMeta.addEnchant(Enchantment.DEPTH_STRIDER, 0, true);
		itemMeta.addEnchant(Enchantment.DIG_SPEED, 0, true);
		itemMeta.addEnchant(Enchantment.DURABILITY, 0, true);
		itemMeta.addEnchant(Enchantment.FIRE_ASPECT, 0, true);
		itemMeta.addEnchant(Enchantment.FROST_WALKER, 0, true);
		itemMeta.addEnchant(Enchantment.IMPALING, 0, true);
		itemMeta.addEnchant(Enchantment.KNOCKBACK, 0, true);
		itemMeta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 0, true);
		itemMeta.addEnchant(Enchantment.LOOT_BONUS_MOBS, 0, true);
		itemMeta.addEnchant(Enchantment.LOYALTY, 0, true);
		itemMeta.addEnchant(Enchantment.LUCK, 0, true);
		itemMeta.addEnchant(Enchantment.LURE, 0, true);
		itemMeta.addEnchant(Enchantment.MENDING, 0, true);
		itemMeta.addEnchant(Enchantment.MULTISHOT, 0, true);
		itemMeta.addEnchant(Enchantment.OXYGEN, 0, true);
		itemMeta.addEnchant(Enchantment.PIERCING, 0, true);
		itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 0, true);
		itemMeta.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 0, true);
		itemMeta.addEnchant(Enchantment.PROTECTION_FALL, 0, true);
		itemMeta.addEnchant(Enchantment.PROTECTION_PROJECTILE, 0, true);
		itemMeta.addEnchant(Enchantment.QUICK_CHARGE, 0, true);
		itemMeta.addEnchant(Enchantment.RIPTIDE, 0, true);
		itemMeta.addEnchant(Enchantment.SILK_TOUCH, 0, true);
		itemMeta.addEnchant(Enchantment.SOUL_SPEED, 0, true);
		itemMeta.addEnchant(Enchantment.SWEEPING_EDGE, 0, true);
		itemMeta.addEnchant(Enchantment.THORNS, 0, true);
		itemMeta.addEnchant(Enchantment.WATER_WORKER, 0, true);
		itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		this.setItemMeta(itemMeta);
	}
	
	// a quick way to add some modifiers
	public void addDamageModifier(double number, Operation operation, EquipmentSlot slot)
	{
		ItemMeta meta = this.getItemMeta();
		meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "GENERIC_ATTACK_DAMAGE", number, operation, slot));
		this.setItemMeta(meta);
	}
	
	public void addSpeedModifier(double number, Operation operation, EquipmentSlot slot)
	{
		ItemMeta meta = this.getItemMeta();
		meta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(UUID.randomUUID(), "GENERIC_MOVEMENT_SPEED", number, operation, slot));
		this.setItemMeta(meta);
	}
	
	public void addArmorModifier(double number, Operation operation, EquipmentSlot slot)
	{
		ItemMeta meta = this.getItemMeta();
		meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "GENERIC_ARMOR", number, operation, slot));
		this.setItemMeta(meta);
	}
	
	public void addArmorToughnessModifier(double number, Operation operation, EquipmentSlot slot)
	{
		ItemMeta meta = this.getItemMeta();
		meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "GENERIC_ARMOR_TOUGHNESS", number, operation, slot));
		this.setItemMeta(meta);
	}
}