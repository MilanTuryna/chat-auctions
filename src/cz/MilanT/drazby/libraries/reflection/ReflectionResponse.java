package cz.MilanT.drazby.libraries.reflection;

/**
 * Created by Jan on 18-02-2017.
 */
public class ReflectionResponse<T> {

    private T object;

    public ReflectionResponse(T object) {
        this.object = object;
    }


    public boolean isValid() {
        return object != null;
    }


    public T get() {
        return object;
    }
}
