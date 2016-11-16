package com.github.nukc.recycleradapter;

import android.view.View;

import java.lang.reflect.Constructor;

/**
 * Created by C on 11/10/2016.
 */

class HolderHelper {

    static <VH extends RecyclerHolder> VH newInstance(Class<?> holderClass, View view, Object listener) {
        Constructor<?>[] constructors = holderClass.getDeclaredConstructors();

        if (constructors == null) {
            throw new IllegalArgumentException(
                    "Impossible to found a constructor for " + holderClass.getSimpleName());
        }

        for (Constructor<?> constructor : constructors) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();

            if (parameterTypes == null) {
                continue;
            }

            if (listener != null && parameterTypes.length == 1) {
                continue;
            }

            try {
                Object viewHolder = null;
                if (isAssignableFrom(parameterTypes, View.class)) {
                    constructor.setAccessible(true);
                    viewHolder = constructor.newInstance(view);
                } else if (isAssignableFrom(parameterTypes, View.class, Object.class)) {
                    constructor.setAccessible(true);
                    viewHolder = constructor.newInstance(view, listener);
                }

                if (viewHolder instanceof RecyclerHolder) {
                    return (VH) viewHolder;
                }

            } catch (Exception e) {
                throw new RuntimeException(
                        "Impossible to instantiate " + holderClass.getSimpleName(), e);
            }

        }

        throw new IllegalArgumentException(
                "Impossible to found a constructor with a view for "
                        + holderClass.getSimpleName());
    }

    private static boolean isAssignableFrom(Class<?>[] parameterTypes, Class<?>... classes) {
        if (parameterTypes == null || classes == null || parameterTypes.length != classes.length) {
            return false;
        }
        for (int i = 0; i < parameterTypes.length; i++) {
            if (!classes[i].isAssignableFrom(parameterTypes[i])) {
                return false;
            }
        }
        return true;
    }
}
