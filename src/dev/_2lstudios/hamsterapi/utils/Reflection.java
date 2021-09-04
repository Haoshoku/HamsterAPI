package dev._2lstudios.hamsterapi.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Reflection {
	private final String version;
	private final Map<String, Class<?>> classes = new HashMap<>();

	public Reflection(String version) {
		this.version = version;
	}

	public Object getField(final Object object, final String fieldName)
			throws NoSuchFieldException, IllegalAccessException {
		if (object == null) {
			throw new IllegalAccessException("Tried to access field from a null object");
		}

		final Object fieldValue;
		final Field field = object.getClass().getField(fieldName);
		final boolean accessible = field.isAccessible();

		field.setAccessible(true);
		fieldValue = field.get(object);
		field.setAccessible(accessible);

		return fieldValue;
	}

	public Class<?> getNMSClassForLatestVersion( String path ) {
		if (this.classes.containsKey( path ) )
			return this.classes.get( path );

		try {
			Class<?> clazz = Class.forName( path );
			this.classes.put( path, clazz );
			return clazz;
		} catch ( ClassNotFoundException ignore ) {

		}
		return null;
	}

	public Class<?> getNMSClass(String key) {
		if (this.classes.containsKey(key)) {
			return this.classes.get(key);
		}

		try {
			Class<?> nmsClass = null;

			if ( !this.version.equals( "v1_17_R1" ) )
				nmsClass = Class.forName("net.minecraft.server." + this.version + "." + key);
			else
				nmsClass = Class.forName( "net.minecraft.network.protocol.game." + key );

			this.classes.put(key, nmsClass);
			return nmsClass;
		} catch (final ClassNotFoundException e) {
		}

		return null;
	}

	public Class<?> getCraftBukkitClass(String key) {
		if (this.classes.containsKey(key)) {
			return this.classes.get(key);
		}

		try {
			Class<?> craftBukkitClass = Class.forName("org.bukkit.craftbukkit." + this.version + "." + key);
			this.classes.put(key, craftBukkitClass);

			return craftBukkitClass;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	public String getVersion() {
		return this.version;
	}
}
