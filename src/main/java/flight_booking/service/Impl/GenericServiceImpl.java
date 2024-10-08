package flight_booking.service.Impl;



import flight_booking.repositories.genericrepository.GenericRepository;
import flight_booking.service.GenericService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class GenericServiceImpl<T, ID, DTO> implements GenericService<ID,DTO> {
    private final GenericRepository<T, ID> repository;
    private final ModelMapper modelMapper;
    private final Class<T> entityClass;
    private final Class<DTO> dtoClass;

    @Override
    public DTO save(DTO dto) {
        T entity = modelMapper.map(dto, entityClass);
        entity = repository.save(entity);
        return modelMapper.map(entity, dtoClass);
    }

    @Override
    public List<DTO> findAll() {
        List<T> entities = repository.findAll();
        List<DTO> dtos = new ArrayList<>();
        for (T entity : entities) {
            dtos.add(modelMapper.map(entity, dtoClass));
        }
        return dtos;
    }

    @Override
    public Optional<DTO> findById(ID id) {
        Optional<T> entity = repository.findById(id);

            return entity.map(e -> modelMapper.map(e, dtoClass));


    }

    @Override
    public DTO update(ID id, DTO dto) {
        if (repository.existsById(id)) {
            T entity = modelMapper.map(dto, entityClass);
            entity = repository.save(entity);
            return modelMapper.map(entity, dtoClass);
        }
        return null; // or throw an exception
    }

    @Override
    public void deleteById(ID id) {
        repository.deleteById(id);
    }

    @Override
    public Page<DTO> getPaginated(Pageable pageable){
        Page<T> entities = repository.findAll(pageable);
        List<DTO> dtos = new ArrayList<>();
        for (T entity : entities) {
            DTO dto = modelMapper.map(entity, dtoClass);
            dtos.add(dto);
        }
        return new PageImpl<>(dtos, pageable, entities.getTotalElements());
    }




}