package mpa.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public abstract class AbstractEntityHome<T extends Entity<?>> implements EntityHome<T> {

	private final Map<String, T> keyMappedEntities = new HashMap<String, T>();
	private final List<T> entities = new ArrayList<T>();

	private static final long serialVersionUID = -6580683107467692007L;

	public T getByKey(String key) {
		T entity = keyMappedEntities.get(key);
		if (entity == null)
            throw new EntityNotFound("The " + getLabel() + " '" + key + "' was not found");
        return entity;
	}

	@Override
	public T add(T entity) {
        if (keyMappedEntities.containsKey(entity.getKey()))
            throw new EntityDuplicated(getLabel() + " " + entity.getKey() + " already exists");
		keyMappedEntities.put(entity.getKey(), entity);
		entities.add(entity);
		return entity;
	}

	@Override
	public int count() {
		return entities.size();
	}

	@Override
	public List<T> all() {
	    return Collections.unmodifiableList(entities);
	}
	
	@Override
	public String getLabel() {
	    //TODO
	    return "";
	}
}
