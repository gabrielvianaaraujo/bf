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

    public void processFilter(){

        if(searchType == searchType.TEXTO){
            categoriaId = null;
        }
        else if(searchType == searchType.CATEGORIA){
            texto = null;
        }
    }
}