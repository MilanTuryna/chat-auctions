package cz.MilanT.drazby.libraries.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;

/**
 * Created by Jan on 17-02-2017.
 */
public class ReflectionMethod implements IReflectionObject {

    private Method baseMethod;

    public ReflectionMethod(Method method) {
        this.baseMethod = method;
    }

    public <T> T invoke(Object handle, Object... args) {
        try {
            return (T) baseMethod.invoke(handle, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }


    public <T> T invokeIfValid(Object handle, Object... args) {
        if (baseMethod == null)
            return null;
        try {
            return (T) baseMethod.invoke(handle, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ReflectionMethod setAccessible(boolean value) {
        baseMethod.setAccessible(value);
        return this;
    }

    public Method getBase() {
        return baseMethod;
    }

    public ReflectionMethod pass(Consumer<ReflectionMethod> value) {
        value.accept(this);
        return this;
    }

    public ReflectionMethod passIfValid(Consumer<ReflectionMethod> value) {
        if (this.baseMethod == null)
            return this;
        value.accept(this);
        return this;
    }

    @Override
    public ReflectionUtil newCall() {
        return ReflectionUtil.newCall();
    }

}
