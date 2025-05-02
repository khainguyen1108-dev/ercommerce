package storemanagement.example.group_15.domain.collections.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import storemanagement.example.group_15.app.dto.request.collections.CollectionRequestDTO;
import storemanagement.example.group_15.domain.collections.entity.CollectionEntity;
import storemanagement.example.group_15.domain.collections.repository.CollectionRepository;
import storemanagement.example.group_15.infrastructure.error.AppException;

import java.util.List;
import java.util.Optional;

@Service
public class CollectionService {
    @Autowired
    private CollectionRepository collectionRepository;

    public CollectionEntity create(CollectionRequestDTO input){
        CollectionEntity data = new CollectionEntity();
        data.setName(input.getName());
        return this.collectionRepository.save(data);
    }
    public Long update(Long id, CollectionRequestDTO input){
        Optional<CollectionEntity> data = this.collectionRepository.findById(id);
        if (data.isEmpty()){
            throw new AppException(HttpStatus.BAD_REQUEST, "collection_id.not_found");
        }
        data.get().setName(input.getName());
        CollectionEntity output = this.collectionRepository.save(data.get());
        return output.getId();
    }
    public Long delete(Long id){
        Optional<CollectionEntity> data = this.collectionRepository.findById(id);
        if (data.isEmpty()){
            throw new AppException(HttpStatus.BAD_REQUEST, "collection_id.not_found");
        }
        this.collectionRepository.delete(data.get());
        return id;
    }
    public List<CollectionEntity> getAll(){
        return this.collectionRepository.findAll();
    }
}
