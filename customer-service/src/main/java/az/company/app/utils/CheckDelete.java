package az.company.app.utils;

import az.company.app.errors.ErrorsFinal;
import az.company.app.exception.ApplicationException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CheckDelete {

    @PersistenceContext
    EntityManager entityManager;


    private <T> boolean countEntitiesByOneSidedEntityId(Class<T> rootClass, Class<?> oneSidedEntityClass, Long oneSidedEntityId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<T> root = cq.from(rootClass);
        cq.select(cb.count(root)).where(
                cb.and(
                        cb.equal(root.get("status"), '1'),
                        cb.equal(root.get(getAttributeNames(rootClass, oneSidedEntityClass)).get("id"), oneSidedEntityId)
                )
        );
        TypedQuery<Long> query = entityManager.createQuery(cq);
        return query.getSingleResult() < 1;
    }

    private <T> String getAttributeNames(Class<T> rootClass, Class<?> oneSidedEntityClass) {
        // Create a map to store the field names and keys
        Map<String, String> map = new HashMap<>();
        try{
            // Get all the fields of the StaffUnit class using reflection
            Field[] fields = rootClass.getDeclaredFields();
            // Loop through each field and check if it has the ManyToOne annotation
            for (Field field : fields) {
                if (field.isAnnotationPresent(ManyToOne.class)) {
                    // Add the field name to the map
                    map.put(field.getType().getName(), field.getName());
                }
            }
        } catch (Exception e){
            throw new ApplicationException(ErrorsFinal.BAD_REQUEST, Map.of("message", "attributlarının işlənməsi zamanı xəta yarandı"));
        }

        return map.get(oneSidedEntityClass.getName());
    }

}
