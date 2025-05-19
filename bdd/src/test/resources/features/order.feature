# language: pt

  Funcionalidade: Pedido de combo de produtos

  Como cliente da MazyFood
  Quero poder montar um combo com hambúrguer, acompanhamento e bebida
  Para realizar um pedido completo

    Cenário: Iniciar pedido e adicionar produto
      Quando eu iniciar um pedido
      E eu adiciono o produto 10 com nome "Pizza", preço 25.00 e quantidade 2 ao pedido

    Cenário: Pagar meu pedido
      Dado que existe um pedido com id 1
      Quando realizo o pagamento
      Então o status do pedido atualiza para "RECEBIDO"

#    Cenário: Atualizar status de pedido
#      Dado que existe um pedido com id 1 e status "RECEBIDO"
#      Quando a cozinha atualiza o status do pedido para "EM_PREPARACAO"
#      Então o código de resposta deve ser 200
#      E o status do pedido deve ser "EM_PREPARACAO"
#
#    Cenário: Cliente retira o pedido
#      Dado que existe um pedido com id 1 e status "PRONTO"
#      Quando o cliente retira o pedido
#      Então o código de resposta deve ser 200
#      E o status do pedido deve ser "FINALIZADO"

