package br.com.softblue.bluefood.domain.restaurante;

import lombok.Data;

@Data
public class SearchFilter {
 
    public enum SearchType{
        TEXTO,
        CATEGORIA
    }

    private String texto;
    private SearchType searchType;
    private Integer categoriaId;
}