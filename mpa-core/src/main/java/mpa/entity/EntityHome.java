package mpa.entity;

import java.io.Serializable;
import java.util.List;

public interface EntityHome<T extends Entity<?>> extends Serializable{
    
    public T getByKey(String key);
    
    public T add(T entity);

    public int count();
    
    public List<T> all();

    public abstract String getLabel();
}
