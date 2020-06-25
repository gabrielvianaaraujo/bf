//Método para setar valores tanto para o searchType quanto para o categoriaId
//searchType é o enum do backend que pode ser: TEXTO ou CATEGORIA
//Em caso do searchType receber algum valor, temos que passar então o valor para categoriaId para que a busca possa ser feita no BD posteriormente
function searchRest(categoriaId){

    var t = document.getElementById("searchType");

    if(categoriaId == null){
        t.value = "TEXTO";
    }else {
        t.value = "CATEGORIA"
        document.getElementById("categoriaId").value = categoriaId;
    }

    document.getElementById("form").submit();

}