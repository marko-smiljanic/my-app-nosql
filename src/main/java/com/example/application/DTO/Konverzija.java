package com.example.application.DTO;

import org.modelmapper.ModelMapper;

public class Konverzija {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static <T, D> D konvertujUDto(T entitet, Class<D> dtoClass) {
        return modelMapper.map(entitet, dtoClass);
    }

    public static <T, D> T konvertujUEntitet(D dto, Class<T> entitetClass) {
        return modelMapper.map(dto, entitetClass);
    }

    //ovo sve moze da se objedini u jednu funkciju, ali za sada zbog preglednosti cu ostaviti ovako
//    public static <S, T> T konvertuj(S source, Class<T> targetClass) {
//        return modelMapper.map(source, targetClass);
//    }

}
