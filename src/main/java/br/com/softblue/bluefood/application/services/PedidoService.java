package br.com.softblue.bluefood.application.services;

import java.time.LocalDateTime;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import br.com.softblue.bluefood.domain.pagamento.DadosCartao;
import br.com.softblue.bluefood.domain.pagamento.StatusPagamento;
import br.com.softblue.bluefood.domain.pedido.Carrinho;
import br.com.softblue.bluefood.domain.pedido.ItemPedido;
import br.com.softblue.bluefood.domain.pedido.ItemPedidoPK;
import br.com.softblue.bluefood.domain.pedido.ItemPedidoRepository;
import br.com.softblue.bluefood.domain.pedido.Pedido;
import br.com.softblue.bluefood.domain.pedido.PedidoRepository;
import br.com.softblue.bluefood.domain.pedido.Pedido.Status;
import br.com.softblue.bluefood.utils.SecurityUtils;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pr;

    @Autowired
    private ItemPedidoRepository ipr;

    @Value("${bluefood.sbpay.url}")
    private String sbPayUrl;

    @Value("${bluefood.sbpay.token}")
    private String sbPayToken;

    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = PagamentoException.class)
    public Pedido criarEPagar(Carrinho carrinho, String numCartao) throws PagamentoException {

        Pedido pedido = new Pedido();
        pedido.setData(LocalDateTime.now());
        pedido.setCliente(SecurityUtils.loggedCliente());
        pedido.setRestaurante(carrinho.getRestaurante());
        pedido.setStatus(Status.Producao);
        pedido.setTaxaEntrega(carrinho.getRestaurante().getTaxaEntrega());
        pedido.setSubTotal(carrinho.getPrecoTotal(false));
        pedido.setTotal(carrinho.getPrecoTotal(true));

        pedido = pr.save(pedido);

        int ordem = 1;

        for(ItemPedido item : carrinho.getItens()){
            item.setId(new ItemPedidoPK(pedido, ordem++));
            ipr.save(item);
        }

        //Chamando o webservice
            DadosCartao dadosCartao = new DadosCartao();
            dadosCartao.setNumCartao(numCartao);

            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add("Token", sbPayToken);

            HttpEntity<DadosCartao> requestEntity = new HttpEntity<>(dadosCartao, headers);

            RestTemplate restTemplate = new RestTemplate();

            Map<String, String> response;
            try {
                response = restTemplate.postForObject(sbPayUrl, requestEntity, Map.class);
            } catch (Exception e) {
                throw new PagamentoException("Erro no servidor de pagamento");
            }

            StatusPagamento statusPagamento = StatusPagamento.valueOf(response.get("status"));

            if(statusPagamento != StatusPagamento.Autorizado){
                throw new PagamentoException(statusPagamento.getDescricao());
            }

        return pedido;
    }
}