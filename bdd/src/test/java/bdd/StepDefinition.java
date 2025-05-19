package bdd;

import io.cucumber.java.Before;
import io.cucumber.java.pt.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class StepDefinition {

    private Response resposta;
    private Integer idPedido;

    @Before
    public void configurar() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Quando("eu iniciar um pedido")
    public void iniciarPedido() {
        resposta = RestAssured
                .given()
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .body("{\"customerId\": 1}")
                .when()
                .post("/orders")
                .then()
                .extract().response();

        System.out.println("Status da resposta: " + resposta.statusCode());
        System.out.println("Corpo da resposta: " + resposta.asString());

        Assertions.assertEquals(200, resposta.statusCode(), "Erro ao criar pedido");

        if (!resposta.jsonPath().getMap("").containsKey("id")) {
            throw new IllegalStateException("Campo 'id' não encontrado na resposta: " + resposta.asString());
        }

        idPedido = resposta.jsonPath().getInt("id");
        Assertions.assertNotNull(idPedido, "Pedido não foi criado corretamente.");
    }

    @E("eu adiciono o produto {int} com nome {string}, preço {double} e quantidade {int} ao pedido")
    public void adicionarProdutoAoPedido(Integer productId, String nome, Double preco, Integer quantidade) {
        Map<String, Object> produto = new HashMap<>();
        produto.put("productId", productId);
        produto.put("name", nome);
        produto.put("price", preco);
        produto.put("quantity", quantidade);

        resposta = RestAssured
                .given()
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .body(produto)
                .when()
                .put("/orders/" + idPedido + "/products")
                .then()
                .extract().response();

        System.out.println("Envio produto → status: " + resposta.statusCode());
        System.out.println("Corpo: " + resposta.asString());

        Assertions.assertEquals(200, resposta.statusCode(), "Erro ao adicionar produto");
    }

    @Dado("que existe um pedido com id {int}")
    public void existePedidoComId(Integer pedidoId) {
        this.idPedido = pedidoId;
    }

    @Quando("realizo o pagamento")
    public void realizarPagamento() {
        Map<String, Object> pagamento = new HashMap<>();
        pagamento.put("paymentMethod", "debit_card");

        resposta = RestAssured
                .given()
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .body(pagamento)
                .when()
                .post("/orders/" + idPedido + "/payment")
                .then()
                .extract().response();

        System.out.println("Pagamento iniciado - Status: " + resposta.statusCode());
        System.out.println("Resposta: " + resposta.asString());

        Assertions.assertEquals(200, resposta.statusCode(), "Erro ao iniciar pagamento");

        resposta = RestAssured
                .given()
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .body(String.format("{\"orderId\": %d, \"authorized\": true}", idPedido))
                .when()
                .post("/orders/payment")
                .then()
                .extract().response();

        System.out.println("Webhook chamado - Status: " + resposta.statusCode());
        System.out.println("Resposta: " + resposta.asString());

        Assertions.assertEquals(200, resposta.statusCode(), "Erro ao chamar webhook de pagamento");
    }

    @Então("o status do pedido atualiza para {string}")
    public void verificarStatusPedidoAtualizado(String statusEsperado) {
        resposta = RestAssured
                .given()
                .accept(String.valueOf(MediaType.APPLICATION_JSON))
                .when()
                .get("/orders/" + idPedido)
                .then()
                .extract().response();

        String status = resposta.jsonPath().getString("orderStatus");
        System.out.println("Status atual do pedido: " + status);
        Assertions.assertEquals(statusEsperado, status);
    }
}

//    @Dado("que existe um pedido com id {int} e status {string}")
//    public void que_existe_um_pedido_com_id_e_status(Integer id, String status) {
//        this.idPedido = id;
//
//        resposta = RestAssured
//                .given()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .body(String.format("""
//                    {
//                        "orderStatus": "%s"
//                    }
//                """, status))
//                .when()
//                .patch("/orders/{idPedido}/status", idPedido)
//                .then()
//                .extract().response();
//    }
//
//    @Quando("a cozinha atualiza o status do pedido para {string}")
//    public void a_cozinha_atualiza_o_status_do_pedido_para(String novoStatus) {
//        resposta = RestAssured
//                .given()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .body(String.format("""
//                    {
//                        "orderStatus": "%s"
//                    }
//                """, novoStatus))
//                .when()
//                .patch("/orders/{idPedido}/status", idPedido)
//                .then()
//                .extract().response();
//    }
//
//    @Quando("o cliente retira o pedido")
//    public void o_cliente_retira_o_pedido() {
//        resposta = RestAssured
//                .given()
//                .when()
//                .patch("/orders/{idPedido}/retirada", idPedido)
//                .then()
//                .extract().response();
//    }
//}
