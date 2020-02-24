package com.rednavis.database.mapper;

import com.rednavis.database.entity.UserEntity;
import com.rednavis.shared.dto.user.RoleEnum;
import com.rednavis.shared.dto.user.User;
import com.rednavis.shared.dto.user.User.UserBuilder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-02-24T15:37:47+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 12.0.2 (Azul Systems, Inc.)"
)
public class UserMapperImpl implements UserMapper {

    @Override
    public UserEntity dtoToEntity(User user) {
        if ( user == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setId( user.getId() );
        userEntity.setFirstName( user.getFirstName() );
        userEntity.setLastName( user.getLastName() );
        userEntity.setEmail( user.getEmail() );
        userEntity.setPassword( user.getPassword() );
        Set<RoleEnum> set = user.getRoles();
        if ( set != null ) {
            userEntity.setRoles( new HashSet<RoleEnum>( set ) );
        }

        return userEntity;
    }

    @Override
    public User entityToDto(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        UserBuilder user = User.builder();

        user.id( userEntity.getId() );
        user.firstName( userEntity.getFirstName() );
        user.lastName( userEntity.getLastName() );
        user.email( userEntity.getEmail() );
        user.password( userEntity.getPassword() );
        Set<RoleEnum> set = userEntity.getRoles();
        if ( set != null ) {
            user.roles( new HashSet<RoleEnum>( set ) );
        }

        return user.build();
    }

    @Override
    public List<UserEntity> listDtoToListEntity(List<User> userList) {
        if ( userList == null ) {
            return null;
        }

        List<UserEntity> list = new ArrayList<UserEntity>( userList.size() );
        for ( User user : userList ) {
            list.add( dtoToEntity( user ) );
        }

        return list;
    }

    @Override
    public List<User> listEntityToListDto(List<UserEntity> userEntityList) {
        if ( userEntityList == null ) {
            return null;
        }

        List<User> list = new ArrayList<User>( userEntityList.size() );
        for ( UserEntity userEntity : userEntityList ) {
            list.add( entityToDto( userEntity ) );
        }

        return list;
    }
}
