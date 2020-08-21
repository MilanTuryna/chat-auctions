package cz.MilanT.drazby.libraries.reflection;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

/**
 * Created by Jan on 18-02-2017.
 */
public class ReflectionConstructor implements IReflectionObject {
    private final Constructor<?> baseConstructor;

    public ReflectionConstructor(Constructor<?> constructor) {
        this.baseConstructor = constructor;
    }

    @Override
    public ReflectionConstructor setAccessible(boolean value) {
        baseConstructor.setAccessible(value);
        return this;
    }

    public Constructor<?> getBase() {
        return baseConstructor;
    }


    public <T> T newInstance(Object... initArgs) {
        try {
            return (T) baseConstructor.newInstance(initArgs);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ReflectionUtil newCall() {
        return ReflectionUtil.newCall();
    }

    public ReflectionConstructor pass(Consumer<ReflectionConstructor> value) {
        value.accept(this);
        return this;
    }

    public ReflectionConstructor passIfValid(Consumer<ReflectionConstructor> value) {
        if (this.baseConstructor == null)
            return this;
        value.accept(this);
        return this;
    }


}
