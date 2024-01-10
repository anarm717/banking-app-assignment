package az.company.auth.service.objectEntity;

import az.company.auth.entity.ObjectEntity;
import az.company.auth.entity.ObjectType;
import az.company.auth.repository.ObjectEntityRepository;
import az.company.auth.repository.ObjectTypeRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ObjectEntityServiceImpl implements ObjectEntityService{

    private final ObjectEntityRepository objectEntityRepository;
    private final ObjectTypeRepository objectTypeRepository;

    @Override
    public void insertMethod(String methodName) {
        ObjectEntity objectEntity = new ObjectEntity();
        ObjectType objectType = objectTypeRepository.findById(4).orElseThrow(()->new IllegalArgumentException("ObjectType not found!"));
        objectEntity.setObjectType(objectType);
        objectEntity.setObjectIdName(methodName);
//        objectEntity.setPermissionId();

    }
}
