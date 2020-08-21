package cz.MilanT.drazby.libraries.reflection;

import java.lang.reflect.Field;
import java.util.function.Consumer;

/**
 * Created by Jan on 17-02-2017.
 */
public class ReflectionField implements IReflectionObject {
    private Field baseField;

    public ReflectionField(Field field) {
        baseField = field;
    }

    @Override
    public ReflectionField setAccessible(boolean value) {
        baseField.setAccessible(true);
        return this;
    }

    public <T> T get(Object handle) {
        try {
            return (T) baseField.get(handle);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    public <T> ReflectionField set(Object handle, T value) {
        try {
            baseField.set(handle, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public ReflectionUtil newCall() {
        return ReflectionUtil.newCall();
    }

    public ReflectionField pass(Consumer<ReflectionField> value) {
        value.accept(this);
        return this;
    }

    public ReflectionField passIfValid(Consumer<ReflectionField> value) {
        if (this.baseField == null)
            return this;
        value.accept(this);
        return this;
    }

    public Field getBase() {
        return baseField;
    }


}
