package six.daoyun.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

public class ObjUitl {

    private static Optional<Field> getClassDeclaredFieldRecur(Class<?> cls, final String fieldName) //{
    {
        while(cls != null) {
            try {
                Field f = cls.getDeclaredField(fieldName);
                return Optional.of(f);
            } catch (NoSuchFieldException ex) {
                cls = cls.getSuperclass();
            }
        }

        return Optional.empty();
    } //}

    public static Collection<String> assignFields(final Object target, final Object source, final Set<String> ignores, final boolean ignoreNull) //{
    {
        Collection<String> ans = new ArrayList<>();
        Set<String> fields = new TreeSet<>();

        Class<?> C = target.getClass();
        while(C != null) {
            for(Field field: C.getDeclaredFields()) {
                final String fieldName = field.getName();
                if(ignores.contains(fieldName)) continue;
                fields.add(fieldName);
            }

            C = C.getSuperclass();
        }

        for(final String fieldName: fields) {
            try {
                Field targetField = getClassDeclaredFieldRecur(target.getClass(), fieldName).get();
                Field sourceField = getClassDeclaredFieldRecur(source.getClass(), fieldName).get();

                if(targetField.getType() != sourceField.getType()) {
                    continue;
                }
                targetField.setAccessible(true);
                sourceField.setAccessible(true);

                Object v = sourceField.get(source);
                if(v != null || !ignoreNull) {
                    targetField.set(target, v);
                    ans.add(fieldName);
                }
            } catch (NoSuchElementException e) {
                continue;
            } catch (IllegalAccessException e) {
                continue;
            }
        }

        return ans;
    } //}

    public static Collection<String> assignFields(final Object target, final Object source) //{
    {
        return assignFields(target, source, new HashSet<>(), true);
    } //}

}

